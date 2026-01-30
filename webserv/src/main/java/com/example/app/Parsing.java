// package com.example.app;

// import java.util.*;
// import org.antlr.v4.runtime.tree.TerminalNode;

// public class Parsing extends NginxBaseListener
// {

//     // public ConfigFile configFile = new ConfigFile();      // final object to hold everything not implemmented yet
//     private ServerConfig currentServer = null;           // currently parsing server block
//     private LocationConfig currentLocation = null;       // currently parsing location block







// // void configPrint( ConfigFile listener.configFile); last func t implement and call
//     @Override
//     public void enterConfig(NginxParser.ConfigContext ctx) {
//         System.out.println("Entered config file");
//     }

//     @Override
//     public void enterStatement(NginxParser.StatementContext ctx) {
//         System.out.println("Entered a statement");
//     }

//     @Override
//     public void enterSimpleDirective(NginxParser.SimpleDirectiveContext ctx) {
//         String name = ctx.IDENT().getText();
//         System.out.println("SimpleDirective: " + name);

//         if (ctx.arguments() != null) 
//         {
//             List<TerminalNode> args = ctx.arguments().IDENT();
//             for (TerminalNode arg : args) {
//                 System.out.println("  Argument IDENT: " + arg.getText());
//             }
//             for (TerminalNode arg : ctx.arguments().STRING()) {
//                 System.out.println("  Argument STRING: " + arg.getText());
//             }
//             for (TerminalNode arg : ctx.arguments().NUMBER()) {
//                 System.out.println("  Argument NUMBER: " + arg.getText());
//             }
//         }
//     }

//     @Override
//     public void enterBlockDirective(NginxParser.BlockDirectiveContext ctx) {
//         String name = ctx.IDENT().getText();
//         String p = "\u001B[31m\t NOOOOO INWALIID\u001B[0m";
//         // chekc if is valid name block
//         if (name.equals("server") || name.equals("location") || name.equals("http") || name.equals("events"))
//         {
//             System.out.println("is valid block name BlockDirective: " + name);
//             if (ctx.arguments() != null) {
//                 System.out.println("  Arguments: " + ctx.arguments().getText());
//         }
//         }
//         else
//             System.out.println("is invalid block name BlockDirective: " + name + p);
        
//     }

//     @Override
//     public void enterArguments(NginxParser.ArgumentsContext ctx) {
//         System.out.println("Entered arguments: " + ctx.getText());
//     }


// }



package com.example.app;

import java.util.*;
import org.antlr.v4.runtime.tree.*;
    
public class Parsing extends NginxBaseListener {

    // Root AST
    private final ConfigAST ast = new ConfigAST();

    // Stack that tracks where statements are added
    private final Deque<List<AstNode>> stack = new ArrayDeque<>();

    // Expose AST after parsing
    public ConfigAST getAst()
    {
        return ast;
    }

    // ===== ROOT =====
    @Override
    public void enterConfig(NginxParser.ConfigContext ctx) {
        stack.push(ast.statements);
    }

    // ===== SIMPLE DIRECTIVE =====
    @Override
    public void enterSimpleDirective(NginxParser.SimpleDirectiveContext ctx) {
        String name = ctx.IDENT().getText();
        List<String> args = new ArrayList<>();

        if (ctx.arguments() != null) {
            for (ParseTree c : ctx.arguments().children) {
                args.add(c.getText());
            }
        }

        SimpleDirectiveNode node = new SimpleDirectiveNode(name, args);
        stack.peek().add(node);
    }

    // ===== BLOCK DIRECTIVE =====
    @Override
    public void enterBlockDirective(NginxParser.BlockDirectiveContext ctx) {
        String name = ctx.IDENT().getText();
        List<String> args = new ArrayList<>();

        if (ctx.arguments() != null) {
            for (ParseTree c : ctx.arguments().children) {
                args.add(c.getText());
            }
        }

        BlockDirectiveNode block = new BlockDirectiveNode(name, args);

        // attach to parent
        stack.peek().add(block);

        // enter this block
        stack.push(block.children);
    }

    // ===== EXIT BLOCK =====
    @Override
    public void exitBlockDirective(NginxParser.BlockDirectiveContext ctx) {
        stack.pop();
    }
}
