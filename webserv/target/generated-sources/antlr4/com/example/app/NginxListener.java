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
	 * Enter a parse tree produced by {@link NginxParser#simpleDirective}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDirective(NginxParser.SimpleDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#simpleDirective}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDirective(NginxParser.SimpleDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link NginxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(NginxParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(NginxParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link NginxParser#blockDirective}.
	 * @param ctx the parse tree
	 */
	void enterBlockDirective(NginxParser.BlockDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#blockDirective}.
	 * @param ctx the parse tree
	 */
	void exitBlockDirective(NginxParser.BlockDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link NginxParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(NginxParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link NginxParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(NginxParser.ArgumentsContext ctx);
}