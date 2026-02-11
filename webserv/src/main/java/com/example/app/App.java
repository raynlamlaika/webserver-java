package com.example.app;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.util.Scanner;
import java.util.List;
import java.io.*;



class App
{
    public static void printAst(List<AstNode> nodes, int indent)
    {
        for (AstNode node : nodes) {

            // indent
            for (int i = 0; i < indent; i++)
                System.out.print("    ");

            if (node instanceof SimpleDirectiveNode)
            {
                SimpleDirectiveNode d = (SimpleDirectiveNode) node;
                System.out.print(d.name);
                for (String arg : d.arguments)
                    System.out.print(" " + arg);
                System.out.println(";");
            }

            else if (node instanceof BlockDirectiveNode)
            {
                BlockDirectiveNode b = (BlockDirectiveNode) node;
                System.out.print(b.name);
                for (String arg : b.arguments)
                    System.out.print(" " + arg);
                System.out.println(" {");

                printAst(b.children, indent + 1);

                for (int i = 0; i < indent; i++)
                    System.out.print("    ");
                System.out.println("}");
                
            }
        }
    }


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
                // Default config file for Docker/containerized environments
                pathConfg = "test.conf";// hard code willb e dynamic based on the input from the user or the default one
                System.out.println("No config file specified, using default: " + pathConfg);
                
                // Only try interactive input if we're in a real terminal
                if (System.console() != null) {
                    Scanner objScan = new Scanner(System.in);
                    System.out.print("Enter config file path (or press Enter for default): ");
                    String input = objScan.nextLine().trim();
                    if (!input.isEmpty()) {
                        pathConfg = input;
                    }
                    objScan.close();
                }
            }

            System.out.println("thinis te path: "+ pathConfg);


            BufferedReader reader = new BufferedReader(new FileReader(pathConfg));

            CharStream input = CharStreams.fromReader(reader);

            // Create Lexer and Parser
            NginxLexer lexer = new NginxLexer(input); // split test based on he nginx.g4 file its like tokenizer cerat from alter4 based on filr.g4 
            CommonTokenStream tokens = new CommonTokenStream(lexer); // parce the token test based on he nginx.g4 file 
            NginxParser parser = new NginxParser(tokens);

            NginxParser.ConfigContext tree = parser.config();



            Parsing listener = new Parsing();
            ParseTreeWalker.DEFAULT.walk(listener, tree);
            ConfigAST ast = listener.getAst();
            // run the server here 
            printAst(ast.statements, 0);

            List<ServerConfig>  serv = Server.build(ast.statements);
            Server.runServers(serv);
             
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
}