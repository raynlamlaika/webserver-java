// Generated from com/example/app/Nginx.g4 by ANTLR 4.13.0
package com.example.app;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class NginxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LBRACE=1, RBRACE=2, SEMI=3, IDENT=4, NUMBER=5, STRING=6, COMMENT=7, WS=8;
	public static final int
		RULE_config = 0, RULE_simpleDirective = 1, RULE_statement = 2, RULE_blockDirective = 3, 
		RULE_arguments = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"config", "simpleDirective", "statement", "blockDirective", "arguments"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LBRACE", "RBRACE", "SEMI", "IDENT", "NUMBER", "STRING", "COMMENT", 
			"WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Nginx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public NginxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConfigContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(NginxParser.EOF, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ConfigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_config; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).enterConfig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).exitConfig(this);
		}
	}

	public final ConfigContext config() throws RecognitionException {
		ConfigContext _localctx = new ConfigContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_config);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENT) {
				{
				{
				setState(10);
				statement();
				}
				}
				setState(15);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(16);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleDirectiveContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(NginxParser.IDENT, 0); }
		public TerminalNode SEMI() { return getToken(NginxParser.SEMI, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public SimpleDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).enterSimpleDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).exitSimpleDirective(this);
		}
	}

	public final SimpleDirectiveContext simpleDirective() throws RecognitionException {
		SimpleDirectiveContext _localctx = new SimpleDirectiveContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_simpleDirective);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(IDENT);
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 112L) != 0)) {
				{
				setState(19);
				arguments();
				}
			}

			setState(22);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public SimpleDirectiveContext simpleDirective() {
			return getRuleContext(SimpleDirectiveContext.class,0);
		}
		public BlockDirectiveContext blockDirective() {
			return getRuleContext(BlockDirectiveContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		try {
			setState(26);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				simpleDirective();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(25);
				blockDirective();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockDirectiveContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(NginxParser.IDENT, 0); }
		public TerminalNode LBRACE() { return getToken(NginxParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(NginxParser.RBRACE, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).enterBlockDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).exitBlockDirective(this);
		}
	}

	public final BlockDirectiveContext blockDirective() throws RecognitionException {
		BlockDirectiveContext _localctx = new BlockDirectiveContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_blockDirective);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			match(IDENT);
			setState(30);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 112L) != 0)) {
				{
				setState(29);
				arguments();
				}
			}

			setState(32);
			match(LBRACE);
			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENT) {
				{
				{
				setState(33);
				statement();
				}
				}
				setState(38);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(39);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public List<TerminalNode> IDENT() { return getTokens(NginxParser.IDENT); }
		public TerminalNode IDENT(int i) {
			return getToken(NginxParser.IDENT, i);
		}
		public List<TerminalNode> STRING() { return getTokens(NginxParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(NginxParser.STRING, i);
		}
		public List<TerminalNode> NUMBER() { return getTokens(NginxParser.NUMBER); }
		public TerminalNode NUMBER(int i) {
			return getToken(NginxParser.NUMBER, i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NginxListener ) ((NginxListener)listener).exitArguments(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(41);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 112L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(44); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 112L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\b/\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0005\u0000\f\b\u0000\n\u0000\f\u0000\u000f\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u0001\u0015\b\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0003\u0002\u001b\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0003\u0003\u001f\b\u0003\u0001\u0003\u0001\u0003\u0005\u0003"+
		"#\b\u0003\n\u0003\f\u0003&\t\u0003\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0004\u0004+\b\u0004\u000b\u0004\f\u0004,\u0001\u0004\u0000\u0000\u0005"+
		"\u0000\u0002\u0004\u0006\b\u0000\u0001\u0001\u0000\u0004\u0006/\u0000"+
		"\r\u0001\u0000\u0000\u0000\u0002\u0012\u0001\u0000\u0000\u0000\u0004\u001a"+
		"\u0001\u0000\u0000\u0000\u0006\u001c\u0001\u0000\u0000\u0000\b*\u0001"+
		"\u0000\u0000\u0000\n\f\u0003\u0004\u0002\u0000\u000b\n\u0001\u0000\u0000"+
		"\u0000\f\u000f\u0001\u0000\u0000\u0000\r\u000b\u0001\u0000\u0000\u0000"+
		"\r\u000e\u0001\u0000\u0000\u0000\u000e\u0010\u0001\u0000\u0000\u0000\u000f"+
		"\r\u0001\u0000\u0000\u0000\u0010\u0011\u0005\u0000\u0000\u0001\u0011\u0001"+
		"\u0001\u0000\u0000\u0000\u0012\u0014\u0005\u0004\u0000\u0000\u0013\u0015"+
		"\u0003\b\u0004\u0000\u0014\u0013\u0001\u0000\u0000\u0000\u0014\u0015\u0001"+
		"\u0000\u0000\u0000\u0015\u0016\u0001\u0000\u0000\u0000\u0016\u0017\u0005"+
		"\u0003\u0000\u0000\u0017\u0003\u0001\u0000\u0000\u0000\u0018\u001b\u0003"+
		"\u0002\u0001\u0000\u0019\u001b\u0003\u0006\u0003\u0000\u001a\u0018\u0001"+
		"\u0000\u0000\u0000\u001a\u0019\u0001\u0000\u0000\u0000\u001b\u0005\u0001"+
		"\u0000\u0000\u0000\u001c\u001e\u0005\u0004\u0000\u0000\u001d\u001f\u0003"+
		"\b\u0004\u0000\u001e\u001d\u0001\u0000\u0000\u0000\u001e\u001f\u0001\u0000"+
		"\u0000\u0000\u001f \u0001\u0000\u0000\u0000 $\u0005\u0001\u0000\u0000"+
		"!#\u0003\u0004\u0002\u0000\"!\u0001\u0000\u0000\u0000#&\u0001\u0000\u0000"+
		"\u0000$\"\u0001\u0000\u0000\u0000$%\u0001\u0000\u0000\u0000%\'\u0001\u0000"+
		"\u0000\u0000&$\u0001\u0000\u0000\u0000\'(\u0005\u0002\u0000\u0000(\u0007"+
		"\u0001\u0000\u0000\u0000)+\u0007\u0000\u0000\u0000*)\u0001\u0000\u0000"+
		"\u0000+,\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000,-\u0001\u0000"+
		"\u0000\u0000-\t\u0001\u0000\u0000\u0000\u0006\r\u0014\u001a\u001e$,";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}