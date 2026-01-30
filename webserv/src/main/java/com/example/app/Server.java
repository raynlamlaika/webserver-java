package com.example.app;

import java.io.IOException;
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
            ServerSocket socketa = new ServerSocket(Integer.parseInt(port));
            socketa.close();


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
                
                for (int j = 0 ; listen.size() > j ;j++)
                {
                    System.out.println("Configured server listening on port: " + listen.get(j));
                    try
                    {
                        listenstart(listen.get(j), cfg.server_name);
                    }
                    catch (IOException e)
                    {
                        System.out.println("Failed to start server on port: " + listen.get(j) + " - " + e.getMessage());
                    }
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