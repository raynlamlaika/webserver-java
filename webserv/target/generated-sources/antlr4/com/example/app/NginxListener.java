// Generated from com/example/app/Nginx.g4 by ANTLR 4.13.0
package com.example.app;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link NginxParser}.
 */
public interface NginxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link NginxParser#config}.
	 * @param ctx the parse tree
	 */
	void enterConfig(NginxParser.ConfigContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#config}.
	 * @param ctx the parse tree
	 */
	void exitConfig(NginxParser.ConfigContext ctx);
	/**
	 * Enter a parse tree produced by {@link NginxParser#directive}.
	 * @param ctx the parse tree
	 */
	void enterDirective(NginxParser.DirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#directive}.
	 * @param ctx the parse tree
	 */
	void exitDirective(NginxParser.DirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link NginxParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(NginxParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(NginxParser.ArgumentContext ctx);
}