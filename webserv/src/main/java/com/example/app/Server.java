package com.example.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
// import java.net.Socket;
// import java.net.*;
// import java.io.*;

// Contexts: http, server, location, events, upstream, mail, stream.
// Essential Directives: listen, server_name, root, index, proxy_pass, rewrite, try_files, include, error_log, access_log, set, if, return.
               
               
public class Server
{

    public static void listenstart(String port, String server_name) throws IOException
    {
        // check for the number format of the port
        int portNumber = Integer.parseInt(port);
        System.out.println("Starting server '" + server_name + "' on port " + portNumber);
        ServerSocket socketa = new ServerSocket();

        InetSocketAddress bindPoint = new InetSocketAddress("localhost", portNumber);
        socketa.bind(bindPoint);

        System.out.println("Server bound to address: " + socketa.getLocalSocketAddress());
        System.out.println("Server is listening on port: " + socketa.getLocalPort());

        while (true)
        {
            var clientSocket = socketa.accept();
            System.out.println("Accepted connection from: " + clientSocket.getRemoteSocketAddress());

            InputStream in = clientSocket.getInputStream();
            StringBuilder requestBuilder = new StringBuilder();
            long contentLength = 0;
            
            // Read headers byte by byte to avoid BufferedReader conflicts
            StringBuilder headerBuilder = new StringBuilder();
            int b;
            boolean foundEmptyLine = false;
            
            while ((b = in.read()) != -1) {
                char c = (char) b;
                headerBuilder.append(c);
                
                // Check for end of headers (\r\n\r\n)
                if (headerBuilder.toString().endsWith("\r\n\r\n")) {
                    foundEmptyLine = true;
                    break;
                }
            }
            
            if (!foundEmptyLine) {
                System.err.println("Error: Could not find end of headers");
                clientSocket.close();
                continue;
            }
            
            // Process headers and find Content-Length
            String headers = headerBuilder.toString();
            requestBuilder.append(headers);
            
            String[] headerLines = headers.split("\r\n");
            for (String line : headerLines) {
                System.out.println("Header line: [" + line + "]");
                if (line.toLowerCase().startsWith("content-length:")) {
                    String lengthStr = line.split(":")[1].trim();
                    contentLength = Long.parseLong(lengthStr);
                    System.out.println("✓ Found Content-Length: " + contentLength);
                    break;
                }
            }
            
            System.out.println("Total content length to read: " + contentLength);
            
            // Handle body with file stream to avoid blocking on large data
            String bodyFilePath = null;
            if (contentLength > 0) {
                // Create unique file per request to avoid conflicts - store in tmp folder
                bodyFilePath = "tmp/upload_" + System.currentTimeMillis() + "_" + 
                              Thread.currentThread().getId() + ".tmp";
                
                System.out.println("Creating body file: " + bodyFilePath);
                FileOutputStream bodyOut = new FileOutputStream(bodyFilePath);
                byte[] buffer = new byte[8192]; // 8KB buffer for non-blocking chunks
                long remaining = contentLength;
                long totalWritten = 0;
                
                // Stream data directly to file to avoid memory overflow
                while (remaining > 0) {
                    int toRead = (int) Math.min(buffer.length, remaining);
                    int bytesRead = in.read(buffer, 0, toRead);
                    
                    System.out.println("Read " + bytesRead + " bytes, remaining: " + remaining);
                    
                    if (bytesRead == -1) {
                        System.out.println("End of stream reached");
                        break;
                    }
                    
                    bodyOut.write(buffer, 0, bytesRead);
                    bodyOut.flush(); // Force write to disk
                    remaining -= bytesRead;
                    totalWritten += bytesRead;
                    
                    System.out.println("Written " + totalWritten + " bytes so far");
                }
                
                bodyOut.close();
                System.out.println("✓ Body data written to file: " + bodyFilePath + " (expected: " + contentLength + ", actual: " + totalWritten + " bytes)");
            } else {
                System.out.println("No body content to read (Content-Length: " + contentLength + ")");
            }

            // Parse the request with file path for large body data
            Request req = Request.parseRequest(requestBuilder.toString(), bodyFilePath);
            
            // Read the request body from file to include in response
            String responseBody = "Hi there!, This is " + port + "!\n\n";
            responseBody += "=== REQUEST DETAILS ===\n";
            responseBody += "Headers:\n" + requestBuilder.toString() + "\n";
            
            if (bodyFilePath != null) {
                responseBody += "Body from file (" + bodyFilePath + "):\n";
                try {
                    java.io.FileInputStream fileIn = new java.io.FileInputStream(bodyFilePath);
                    byte[] fileBuffer = new byte[8192];
                    StringBuilder bodyContent = new StringBuilder();
                    int bytesRead;
                    
                    while ((bytesRead = fileIn.read(fileBuffer)) != -1) {
                        bodyContent.append(new String(fileBuffer, 0, bytesRead));
                    }
                    fileIn.close();
                    
                    responseBody += bodyContent.toString();
                } catch (IOException e) {
                    responseBody += "Error reading body file: " + e.getMessage();
                }
            } else {
                responseBody += "No body data\n";
            }
            
            // Send response with request data
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-Type: text/plain\r\n");
            out.print("Content-Length: " + responseBody.length() + "\r\n");
            out.print("\r\n");
            out.print(responseBody);
            out.flush();

            clientSocket.close();
            
            // Clean up temporary file if created
            if (bodyFilePath != null) {
                new java.io.File(bodyFilePath).delete();
            }
        }
    }
        // simulate starting a server on a given port with a server name
        // public static void listenstart(String port,  String server_name) throws IOException //Exception Propagation
        // {
        //     // check for the number format of the port
        //     int portNumber = Integer.parseInt(port);
        // System.out.println("Starting server '" + server_name + "' on port " + portNumber);
        //     ServerSocket socketa = new ServerSocket();


        //     InetSocketAddress bindPoint = new InetSocketAddress("localhost", portNumber);
        //     socketa.bind(bindPoint);

        //     System.out.println("Server bound to address: " + socketa.getLocalSocketAddress());
        //     System.out.println("Server is listening on port: " + socketa.getLocalPort());



        //     // while (true)
        //     // {
        //     //     // Accept incoming connections
        //     //     var clientSocket = socketa.accept();
        //     //     System.out.println("Accepted connection from: " + clientSocket.getRemoteSocketAddress());

        //     //     BufferedReader reader = new BufferedReader(
        //     //         new InputStreamReader(clientSocket.getInputStream()));
        //     //     String requiestlines;
        //     //     StringBuilder requestBuilder = new StringBuilder();
        //     //     while ((requiestlines = reader.readLine()) != null && !requiestlines.isEmpty()) {
        //     //                 requestBuilder.append(requiestlines).append("\r\n");
        //     //             }
        //     //     // read request and send response
        //     //     requestBuilder.append("\r\n");

        //     //     // Request.parseRequest(requestBuilder.toString());

        //     // // Here you would handle the client connection (e.g., read request, send response)
        //     //     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // true = auto-flush
        //     //     Request req = Request.parseRequest(requestBuilder.toString());
        //     //     out.print("HTTP/1.1 200 OK\r\n");
        //     //     out.print("Content-Type: text/plain\r\n");
        //     //     out.print("Content-Length: 38\r\n");
        //     //     out.print("\r\n"); // Empty line separates headers from body
        //     //     out.print("Hi there!, This is " + port + "!");
        //     //     out.flush();
    
        //     //     clientSocket.close();
            
        //     // }
        //     while (true)
        //     {
        //         var clientSocket = socketa.accept();
        //         System.out.println("Accepted connection from: " + clientSocket.getRemoteSocketAddress());

        //         BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //         String line;
        //         int bufferSize = 8192; // 8KB buffer


        //         StringBuilder requestBuilder = new StringBuilder();
        //         int contentLength = 0;
        //         while ()
        //         // ✅ Read headers
        //         while ((line = reader.readLine()) != null && !line.isEmpty()) {
        //             requestBuilder.append(line).append("\r\n");
                    
        //             // Check for Content-Length header
        //             if (line.toLowerCase().startsWith("content-length:")) {
        //                 contentLength = Integer.parseInt(line.split(":")[1].trim());
        //             }
        //         }
                
        //         // ✅ Add separator
        //         requestBuilder.append("\r\n");
                
        //         // ✅ Read body if Content-Length exists
        //         if (contentLength > 0) {
        //             char[] bodyChars = new char[contentLength];
        //             reader.read(bodyChars, 0, contentLength);
        //             requestBuilder.append(bodyChars);
        //         }

        //         Request req = Request.parseRequest(requestBuilder.toString());
                
        //         System.out.println("Body: [" + req.getBody() + "]");
                
        //         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        //         out.print("HTTP/1.1 200 OK\r\n");
        //         out.print("Content-Type: text/plain\r\n");
        //         out.print("Content-Length: 38\r\n");
        //         out.print("\r\n");
        //         out.print("Hi there!, This is " + port + "!");
        //         out.flush();

        //         clientSocket.close();
        //     }
        // }
        
        public static void runServers(List<ServerConfig> serv)
        {
            // check for max  min size of the serv list
            for (int i = 0; i < serv.size(); i++)
            {
                ServerConfig cfg = serv.get(i);

                // lets run that servers 
                cfg.server_name = cfg.server_name.isEmpty() ? "default_server" : cfg.server_name;
                List<String> listen = cfg.listen.isEmpty() ? new ArrayList<String>() {{ add("80"); }} : cfg.listen;


                for (int j = 0 ; listen.size() > j ; j++)
                {
                    final String currentPort = listen.get(j);
                    System.out.println("Configured server listening on port: " + listen.get(j));
                    Thread thread = new Thread(() -> {
                        try
                        {
                            listenstart(currentPort, cfg.server_name);
                        } catch (IOException e) {
                            System.out.println("Failed to start server on port: " + currentPort + " - " + e.getMessage());
                        }
                    });
                    thread.start();
                }

                System.out.println("Starting server:");
                System.out.println("  Listen: " + String.join(", ", cfg.listen));
                System.out.println("  Server Name: " + cfg.server_name);
                System.out.println("  Root: " + cfg.root);
                System.out.println("  Index: " + cfg.index);
                System.out.println("  Locations: " + cfg.locations.size());
                // Here you would add the actual server start logic
            }
        }



    static ServerConfig parseServer(BlockDirectiveNode node)
    {
         ServerConfig cfg = new ServerConfig();

        for (AstNode child : node.children)
        {
           
            // SIMPLE directives: listen, server_name, root, etc.
            if (child instanceof SimpleDirectiveNode)
            {
                SimpleDirectiveNode d = (SimpleDirectiveNode) child;
                switch (d.name)
                {
                    case "listen":
                        cfg.listen.addAll(d.arguments);
                        break;

                    case "server_name":
                        cfg.server_name = d.arguments.get(0);
                        break;

                    case "root":
                        cfg.root = d.arguments.get(0);
                        break;

                    case "index":
                        cfg.index = d.arguments.get(0);
                        break;

                    case "try_files":
                        cfg.try_files.addAll(d.arguments);
                        break;

                    case "error_page":
                        cfg.error_page = d.arguments.get(0);
                        break;
                }
            }

            // BLOCK directives: location
            else if (child instanceof BlockDirectiveNode)
            {
                BlockDirectiveNode block = (BlockDirectiveNode) child;

                if (block.name.equals("location"))
                {
                    LocationConfig loc = new LocationConfig();
                    loc.path = block.arguments.get(0);

                    for (AstNode locChild : block.children)
                    {
                        if (locChild instanceof SimpleDirectiveNode)
                        {
                            SimpleDirectiveNode d = (SimpleDirectiveNode) locChild;
                            switch (d.name)
                            {
                                case "root":
                                    loc.root = d.arguments.get(0);
                                    break;

                                case "index":
                                    loc.index = d.arguments.get(0);
                                    break;

                                case "try_files":
                                    loc.try_files.addAll(d.arguments);
                                    break;

                                case "proxy_pass":
                                    loc.proxy_pass = d.arguments.get(0);
                                    break;
                            }
                        }
                    }

                    cfg.locations.add(loc);
                }
            }
        }

        return cfg;
    }

    public static List<ServerConfig> build(List<AstNode> nodes) {
        List<ServerConfig> servers = new ArrayList<>();

        for (AstNode node : nodes)
        {
            // dont froget parce the events
                System.out.print("this is the name: " + node.name + " and the type " + node.getClass().getSimpleName() + "\n");

            if (node instanceof BlockDirectiveNode // check lmochkil hnaya
            && (node.name.equals("server") || node.name.equals("http")))
            {
    
            if (node.name.equals("http"))
                {
                servers.addAll(build(((BlockDirectiveNode) node).children));
                continue;
            }
            else {
                servers.add(parseServer((BlockDirectiveNode) node));
            }
            }
            
        }
        return servers;
    }
}