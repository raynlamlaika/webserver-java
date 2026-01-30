package com.example.app;

import java.util.ArrayList;
import java.util.List;

public class BlockDirectiveNode extends AstNode {
    public final List<String> arguments;
    public final List<AstNode> children = new ArrayList<>();

    public BlockDirectiveNode(String name, List<String> arguments) {
        super(name);
        this.arguments = arguments;
    }
}
