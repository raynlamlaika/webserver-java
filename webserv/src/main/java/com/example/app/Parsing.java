package com.example.app;

import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Parsing extends NginxBaseListener
{

    // public ConfigFile configFile = new ConfigFile();      // final object to hold everything not implemmented yet
    private ServerConfig currentServer = null;           // currently parsing server block
    private LocationConfig currentLocation = null;       // currently parsing location block







// void configPrint( ConfigFile listener.configFile); last func t implement and call
    @Override
    public void enterConfig(NginxParser.ConfigContext ctx) {
        System.out.println("Entered config file");
    }

    @Override
    public void enterStatement(NginxParser.StatementContext ctx) {
        System.out.println("Entered a statement");
    }

    @Override
    public void enterSimpleDirective(NginxParser.SimpleDirectiveContext ctx) {
        String name = ctx.IDENT().getText();
        System.out.println("SimpleDirective: " + name);

        if (ctx.arguments() != null) 
        {
            List<TerminalNode> args = ctx.arguments().IDENT();
            for (TerminalNode arg : args) {
                System.out.println("  Argument IDENT: " + arg.getText());
            }
            for (TerminalNode arg : ctx.arguments().STRING()) {
                System.out.println("  Argument STRING: " + arg.getText());
            }
            for (TerminalNode arg : ctx.arguments().NUMBER()) {
                System.out.println("  Argument NUMBER: " + arg.getText());
            }
        }
    }

    @Override
    public void enterBlockDirective(NginxParser.BlockDirectiveContext ctx) {
        String name = ctx.IDENT().getText();
        String p = "\u001B[31m\t NOOOOO INWALIID\u001B[0m";
        // chekc if is valid name block
        if (name.equals("server") || name.equals("location") || name.equals("http") || name.equals("events"))
        {
            System.out.println("is valid block name BlockDirective: " + name);
            if (ctx.arguments() != null) {
                System.out.println("  Arguments: " + ctx.arguments().getText());
        }
        }
        else
            System.out.println("is invalid block name BlockDirective: " + name + p);
        
    }

    @Override
    public void enterArguments(NginxParser.ArgumentsContext ctx) {
        System.out.println("Entered arguments: " + ctx.getText());
    }


}
