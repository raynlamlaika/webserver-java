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
       
               
public class Server
{
    public static void listenstart(String port, String server_name, ServerConfig cfg) throws IOException
    {
        // check for the number format of the port
        int portNumber = Integer.parseInt(port);
        System.out.println("Starting server '" + server_name + "' on port " + portNumber);
        ServerSocket socketa = new ServerSocket();

        InetSocketAddress bindPoint = new InetSocketAddress("0.0.0.0", portNumber); //check why
        socketa.bind(bindPoint);

        while (true)
        {
            var clientSocket = socketa.accept();
            System.out.println("Accepted connection from: " + clientSocket.getRemoteSocketAddress());

            InputStream in = clientSocket.getInputStream();
            StringBuilder requestBuilder = new StringBuilder();
            long contentLength = 0;
            
            StringBuilder headerBuilder = new StringBuilder();
            int b;
            boolean foundEmptyLine = false;
            
            while ((b = in.read()) != -1) {
                char c = (char) b;
                headerBuilder.append(c);

                if (headerBuilder.toString().endsWith("\r\n\r\n")) {
                    foundEmptyLine = true;
                    break;
                }
            }
            
            if (!foundEmptyLine) {
                clientSocket.close();
                continue;
            }
            
            String headers = headerBuilder.toString();
            requestBuilder.append(headers);
            
            String[] headerLines = headers.split("\r\n");
            for (String line : headerLines)
            {
                if (line.toLowerCase().startsWith("content-length:")) {
                    String lengthStr = line.split(":")[1].trim();
                    contentLength = Long.parseLong(lengthStr);
                    System.out.println("✓ Found Content-Length: " + contentLength);
                    break;
                }
            }
            
            String bodyFilePath = null;
            if (contentLength > 0) {
                // Create tmp directory if it doesn't exist
                java.io.File tmpDir = new java.io.File("tmp");
                if (!tmpDir.exists()) {
                    tmpDir.mkdirs();
                    System.out.println("Created tmp directory: " + tmpDir.getAbsolutePath());
                }
                
                // Create unique file per request to avoid conflicts - store in tmp folder
                bodyFilePath = "tmp/upload_" + System.currentTimeMillis() + "_" + 
                              Thread.currentThread().getId() + ".tmp";
                
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
            Response response = new Response();
            String httpResponse = response.response(req, cfg); // Pass actual config
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.print(httpResponse);
            out.flush();

            clientSocket.close();
            
            if (bodyFilePath != null) {
                new java.io.File(bodyFilePath).delete();
            }
        }
    }

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
                    Thread thread = new Thread(() -> {
                        try
                        {
                            listenstart(currentPort, cfg.server_name, cfg);
                        } catch (IOException e) {
                            System.out.println("Failed to start server on port: " + currentPort + " - " + e.getMessage());
                        }
                    });
                    thread.start();
                }
            }
        }



    static ServerConfig parseServer(BlockDirectiveNode node)
    {
         ServerConfig cfg = new ServerConfig();

        for (AstNode child : node.children)
        {
           
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