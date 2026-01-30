package com.example.app;

import java.util.ArrayList;
import java.util.List;


// Contexts: http, server, location, events, upstream, mail, stream.
// Essential Directives: listen, server_name, root, index, proxy_pass, rewrite, try_files, include, error_log, access_log, set, if, return.
               
               
public class Server
{
    static ServerConfig parseServer(BlockDirectiveNode node)
    {
        // check for the locations
        // ServerConfig holder = new ServerConfig();
        // for (String arg : node.arguments)
        // {
        //     if (arg.equals("listen"))
        //     {
        //         holder.listen.add(arg);
        //         System.out.print(arg);
        //     }
        //     else if (arg.equals("server_name"))
        //     {
        //         holder.server_name = arg;
        //     }
        //     else if (arg.equals("root"))
        //     {
        //         holder.root = arg;
        //     }
        //     else if (arg.equals("index"))
        //     {
        //         holder.index = arg;
        //     }
        //     else if (arg.equals("try_files"))
        //     {
        //         holder.try_files.add(arg);
        //     }
        //     else if (arg.equals("error_page"))
        //     {
        //         holder.error_page = arg ;
        //     }
        //     // check for the location 
        //     // if (b.children.name == "location")
        //     // {
        //     //     // colect  the location vars 
        //     // }
        // }
        // return holder;

         ServerConfig cfg = new ServerConfig();

        for (AstNode child : node.children)
        {
           
            // SIMPLE directives: listen, server_name, root, etc.
            if (child instanceof SimpleDirectiveNode)
            {
                SimpleDirectiveNode d = (SimpleDirectiveNode) child;
                System.out.print("hsdsdabhdsbf sdj sihdbfsn " +d.name +"\n");
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
                    // cfg.locations.add(parseLocation(block))
                    ;
                }
            }
        }

        return cfg;
    }

    public static List<ServerConfig> build(List<AstNode> nodes) {
        List<ServerConfig> servers = new ArrayList<>();

        for (AstNode node : nodes)
        {
                System.out.print("WWA&AAAAAAAAAAAAAAAAAAAD SALAAAAM\n");

            if (node instanceof BlockDirectiveNode // check lmochkil hnaya
                && node.name.equals("server")) {
                System.out.print("22222222222WWA&AAAAAAAAAAAAAAAAAAAD SALAAAAM\n");
                
                servers.add(parseServer((BlockDirectiveNode) node));
            }
        }
        return servers;
    }
}