package com.example.app;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

// import com.example.app.NginxLexer;
// import com.example.app.NginxParser;
// import com.example.app.NginxBaseListener;
// import com.example.app.ServerInstance;




class App
{



    public static void main(String argv[])
    {
        // will init the socket here to make auto close after finish
        try 
        {
            System.out.println("the Webserver just STARTED");
            // opne  the file 
            String pathConfg;
            if (argv.length > 0)
            {
                pathConfg = argv[0];
            } 
            else
            {
                Scanner objScan = new Scanner(System.in);
                System.out.print("Enter config file path: ");
                pathConfg = objScan.nextLine();
                objScan.close();
            }

// Byte/charchter stream in java is :
/*
    the Byte  Stream are actuly user to transfer data like videos images audio ...
    work with data in the form of 8-bit bytes
    where the function they handel those operation are buffering raw 8-bit bytes o the data as chunqs | or may read and write 


    Char sttream useed to hande test they work with 16-bit Unicode characters,
                                                +----------------------+
                                                |      File/Data       |
                                                +----------------------+
                                                            |
                                                            v
                                            ---------------------------
                                            |       Stream Type       |
                                            ---------------------------
                                                /                     \
                                            /                       \
                                    +-------------------+       +---------------------+
                                    |   Byte Stream     |       |  Character Stream   |
                                    +-------------------+       +---------------------+
                                    | Reads/Writes 8-bit |       | Reads/Writes 16-bit|
                                    | bytes (raw data)  |       | Unicode characters |
                                    +-------------------+       +---------------------+
                                    | Good for binary   |       | Good for text files |
                                    | files (images,    |       | (.txt, .csv, .json)|
                                    | audio, video)     |       | Handles encoding    |
                                    +-------------------+       +---------------------+
                                    | Example classes:  |       | Example classes:    |
                                    | FileInputStream,  |       | FileReader,         |
                                    | FileOutputStream  |       | FileWriter          |
                                    +-------------------+       +---------------------+



            we are working with text so we will use the character stream 

*/
            

            // if no file pass in or not found work with the default
            System.out.println("thinis te path: "+ pathConfg);


            BufferedReader reader = new BufferedReader(new FileReader(pathConfg));

            CharStream input = CharStreams.fromReader(reader);

            // Create Lexer and Parser
            NginxLexer lexer = new NginxLexer(input); // split test based on he nginx.g4 file its like tokenizer cerat from alter4 based on filr.g4 
            CommonTokenStream tokens = new CommonTokenStream(lexer); // parce the token test based on he nginx.g4 file 
            // tokens.fill();  
            // 4️⃣ Create the parser
            NginxParser parser = new NginxParser(tokens);

            // 5️⃣ Get the parse tree starting from 'config'
            NginxParser.ConfigContext tree = parser.config();



            Parsing listener = new Parsing();

            // 7️⃣ Walk the tree
            ParseTreeWalker.DEFAULT.walk(listener, tree);

            // here need to buil in ast object or just in class object hold all of the  data to work with it
            

            // here the printer of the config file
            // void configPrint( ConfigFile listener.configFile);

             
            System.out.println("\u001B[32m\t SUCCESS: All good!\u001B[0m");
            

            reader.close();

        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error: Config file not found - " + e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.err.println("Error parsing config: " + e.getMessage());
            e.printStackTrace();
        }






    }

    // Listener to print visited nodes; helps inspect the parsed nginx config
    // static class DebugPrintListener extends NginxBaseListener
    // {
    //     private final NginxParser parser;

    //     DebugPrintListener(NginxParser parser)
    //     {
    //         this.parser = parser;
    //     }

    //     @Override
    //     public void enterDirective(NginxParser.DirectiveContext ctx)
    //     {
    //         System.out.println("[directive] " + ctx.getText());
    //     }

    //     @Override
    //     public void enterArgument(NginxParser.ArgumentContext ctx)
    //     {
    //         System.out.println("  [arg] " + ctx.getText());
    //     }
    // }

    // // Listener that builds a simple list of directives and their arguments
    // static class DirectiveCollectorListener extends NginxBaseListener
    // {
    //     final List<Directive> directives = new ArrayList<>();

    //     @Override
    //     public void enterDirective(NginxParser.DirectiveContext ctx)
    //     {
    //         Directive d = new Directive();
    //         d.name = ctx.getChild(0).getText(); // first token in rule is the directive name
    //         for (NginxParser.ArgumentContext argCtx : ctx.argument())
    //         {
    //             d.args.add(argCtx.getText());
    //         }
    //         directives.add(d);
    //     }
    // }

    // // Simple data holder for a directive
    // static class Directive
    // {
    //     String name;
    //     List<String> args = new ArrayList<>();
    // }

    // Build server instances from flat directive list (assumes directives ordered as parsed)
    // static List<ServerInstance> buildServers(List<Directive> directives)
    // {
    //     List<ServerInstance> servers = new ArrayList<>();
    //     ServerInstance current = null;

    //     for (Directive d : directives)
    //     {
    //         if ("server".equals(d.name) && d.args.size() >= 2 && "listen".equals(d.args.get(0)))
    //         {
    //             try
    //             {
    //                 int port = Integer.parseInt(d.args.get(1));
    //                 current = new ServerInstance(port);
    //                 servers.add(current);
    //             }
    //             catch (NumberFormatException ignored)
    //             {
    //                 // ignore invalid port
    //             }
    //         }
    //         else if ("server_name".equals(d.name) && current != null && !d.args.isEmpty())
    //         {
    //             current.serverName = d.args.get(0);
    //         }
    //         else if ("return".equals(d.name) && current != null && d.args.size() >= 2)
    //         {
    //             current.responseBody = d.args.get(1);
    //         }
    //     }

    //     return servers;
    // }
}