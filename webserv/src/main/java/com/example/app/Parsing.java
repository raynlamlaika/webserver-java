
package com.example.app;

import java.util.*;
import org.antlr.v4.runtime.tree.*;
    
public class Parsing extends NginxBaseListener {
    private final ConfigAST ast = new ConfigAST();

    private final Deque<List<AstNode>> stack = new ArrayDeque<>();

    public ConfigAST getAst()
    {
        return ast;
    }

    @Override
    public void enterConfig(NginxParser.ConfigContext ctx) {
        stack.push(ast.statements);
    }

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

        stack.peek().add(block);

        stack.push(block.children);
    }

    @Override
    public void exitBlockDirective(NginxParser.BlockDirectiveContext ctx) {
        stack.pop();
    }
}
