package com.example.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        // simulate starting a server on a given port with a server name
        public static void listenstart(String port,  String server_name) throws IOException //Exception Propagation
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
                // Accept incoming connections
                var clientSocket = socketa.accept();
                System.out.println("Accepted connection from: " + clientSocket.getRemoteSocketAddress());

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                String requiestlines;
                StringBuilder requestBuilder = new StringBuilder();
                while ((requiestlines = reader.readLine()) != null && !requiestlines.isEmpty()) {
                            requestBuilder.append(requiestlines).append("\n");
                        }
                System.out.println("Received request:\n" + requestBuilder.toString()+ "|YYYYYYYYYYY\n");
                // read request and send response

                // Request.parseRequest(requestBuilder.toString());


                String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                      "Content-Length: 38\r\n" +
                                      "Content-Type: text/plain\r\n" +
                                      "\r\n" +
                                      "Hi there!, This is " + port + "!\n";
                // clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                // clientSocket.getOutputStream().flush();
                // // Here you would handle the client connection (e.g., read request, send response)
                // clientSocket.close(); // Close the client socket after handling
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // true = auto-flush
    
    out.println("HTTP/1.1 200 OK");
    out.println("Content-Type: text/plain");
    out.println("Content-Length: 38");
    out.println(); // Empty line separates headers from body
    out.println("Hi there!, This is " + port + "!");
    
    clientSocket.close();
            
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