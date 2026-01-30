package com.example.app;

import java.util.List;

public class SimpleDirectiveNode extends AstNode {
    public final List<String> arguments;

    public SimpleDirectiveNode(String name, List<String> arguments) {
        super(name);
        this.arguments = arguments;
    }
}
