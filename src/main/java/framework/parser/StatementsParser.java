// Generated from Statements.g4 by ANTLR 4.9.2
package framework.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StatementsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, COLON=2, SEMICOLON=3, QUIT=4, BACKGROUND=5, JOIN=6, BLANK=7, WS=8, 
		END_LINE=9, M_COMMENT=10, S_COMMENT=11, SESSION_ID=12, ENV_VARIABLE_NAME=13, 
		ENV_SHELL_EXPR=14, TEST_ANNOTATION=15, SQL_STRING=16, SHEEL_COMMAND=17;
	public static final int
		RULE_script = 0, RULE_statement = 1, RULE_test_annotation_stmt = 2, RULE_env_var_stmt = 3, 
		RULE_env_var_value_stmt = 4, RULE_env_var_shell_stmt = 5, RULE_session_sql_stmt = 6, 
		RULE_sql_stmt = 7, RULE_sql_end = 8, RULE_empty_stmt = 9, RULE_shell_command = 10, 
		RULE_blanks = 11, RULE_ident = 12, RULE_single_comment = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"script", "statement", "test_annotation_stmt", "env_var_stmt", "env_var_value_stmt", 
			"env_var_shell_stmt", "session_sql_stmt", "sql_stmt", "sql_end", "empty_stmt", 
			"shell_command", "blanks", "ident", "single_comment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "':'", "';'", "'!'", "'&'", "'<'", "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "COLON", "SEMICOLON", "QUIT", "BACKGROUND", "JOIN", "BLANK", 
			"WS", "END_LINE", "M_COMMENT", "S_COMMENT", "SESSION_ID", "ENV_VARIABLE_NAME", 
			"ENV_SHELL_EXPR", "TEST_ANNOTATION", "SQL_STRING", "SHEEL_COMMAND"
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
	public String getGrammarFileName() { return "Statements.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public StatementsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ScriptContext extends ParserRuleContext {
		public List<Test_annotation_stmtContext> test_annotation_stmt() {
			return getRuleContexts(Test_annotation_stmtContext.class);
		}
		public Test_annotation_stmtContext test_annotation_stmt(int i) {
			return getRuleContext(Test_annotation_stmtContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ScriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_script; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterScript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitScript(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitScript(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScriptContext script() throws RecognitionException {
		ScriptContext _localctx = new ScriptContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_script);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(28);
				test_annotation_stmt();
				}
				break;
			}
			setState(32); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(31);
					statement();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(34); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TEST_ANNOTATION) {
				{
				setState(36);
				test_annotation_stmt();
				}
			}

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

	public static class StatementContext extends ParserRuleContext {
		public Sql_stmtContext sql_stmt() {
			return getRuleContext(Sql_stmtContext.class,0);
		}
		public Sql_endContext sql_end() {
			return getRuleContext(Sql_endContext.class,0);
		}
		public Session_sql_stmtContext session_sql_stmt() {
			return getRuleContext(Session_sql_stmtContext.class,0);
		}
		public Single_commentContext single_comment() {
			return getRuleContext(Single_commentContext.class,0);
		}
		public Empty_stmtContext empty_stmt() {
			return getRuleContext(Empty_stmtContext.class,0);
		}
		public Shell_commandContext shell_command() {
			return getRuleContext(Shell_commandContext.class,0);
		}
		public Env_var_stmtContext env_var_stmt() {
			return getRuleContext(Env_var_stmtContext.class,0);
		}
		public Test_annotation_stmtContext test_annotation_stmt() {
			return getRuleContext(Test_annotation_stmtContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(50);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(39);
				sql_stmt();
				setState(40);
				sql_end();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(42);
				session_sql_stmt();
				setState(43);
				sql_end();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(45);
				single_comment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(46);
				empty_stmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(47);
				shell_command();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(48);
				env_var_stmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(49);
				test_annotation_stmt();
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

	public static class Test_annotation_stmtContext extends ParserRuleContext {
		public TerminalNode TEST_ANNOTATION() { return getToken(StatementsParser.TEST_ANNOTATION, 0); }
		public Test_annotation_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_test_annotation_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterTest_annotation_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitTest_annotation_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitTest_annotation_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Test_annotation_stmtContext test_annotation_stmt() throws RecognitionException {
		Test_annotation_stmtContext _localctx = new Test_annotation_stmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_test_annotation_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			match(TEST_ANNOTATION);
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

	public static class Env_var_stmtContext extends ParserRuleContext {
		public Sql_endContext sql_end() {
			return getRuleContext(Sql_endContext.class,0);
		}
		public Env_var_value_stmtContext env_var_value_stmt() {
			return getRuleContext(Env_var_value_stmtContext.class,0);
		}
		public Env_var_shell_stmtContext env_var_shell_stmt() {
			return getRuleContext(Env_var_shell_stmtContext.class,0);
		}
		public Env_var_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_env_var_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterEnv_var_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitEnv_var_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitEnv_var_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Env_var_stmtContext env_var_stmt() throws RecognitionException {
		Env_var_stmtContext _localctx = new Env_var_stmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_env_var_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(54);
				env_var_value_stmt();
				}
				break;
			case 2:
				{
				setState(55);
				env_var_shell_stmt();
				}
				break;
			}
			setState(58);
			sql_end();
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

	public static class Env_var_value_stmtContext extends ParserRuleContext {
		public TerminalNode ENV_VARIABLE_NAME() { return getToken(StatementsParser.ENV_VARIABLE_NAME, 0); }
		public TerminalNode SQL_STRING() { return getToken(StatementsParser.SQL_STRING, 0); }
		public Env_var_value_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_env_var_value_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterEnv_var_value_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitEnv_var_value_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitEnv_var_value_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Env_var_value_stmtContext env_var_value_stmt() throws RecognitionException {
		Env_var_value_stmtContext _localctx = new Env_var_value_stmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_env_var_value_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(ENV_VARIABLE_NAME);
			setState(61);
			match(T__0);
			setState(62);
			match(SQL_STRING);
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

	public static class Env_var_shell_stmtContext extends ParserRuleContext {
		public TerminalNode ENV_VARIABLE_NAME() { return getToken(StatementsParser.ENV_VARIABLE_NAME, 0); }
		public TerminalNode ENV_SHELL_EXPR() { return getToken(StatementsParser.ENV_SHELL_EXPR, 0); }
		public Env_var_shell_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_env_var_shell_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterEnv_var_shell_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitEnv_var_shell_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitEnv_var_shell_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Env_var_shell_stmtContext env_var_shell_stmt() throws RecognitionException {
		Env_var_shell_stmtContext _localctx = new Env_var_shell_stmtContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_env_var_shell_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(ENV_VARIABLE_NAME);
			setState(65);
			match(T__0);
			setState(66);
			match(ENV_SHELL_EXPR);
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

	public static class Session_sql_stmtContext extends ParserRuleContext {
		public IdentContext ident() {
			return getRuleContext(IdentContext.class,0);
		}
		public TerminalNode JOIN() { return getToken(StatementsParser.JOIN, 0); }
		public TerminalNode QUIT() { return getToken(StatementsParser.QUIT, 0); }
		public Sql_stmtContext sql_stmt() {
			return getRuleContext(Sql_stmtContext.class,0);
		}
		public TerminalNode BACKGROUND() { return getToken(StatementsParser.BACKGROUND, 0); }
		public Session_sql_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_session_sql_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterSession_sql_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitSession_sql_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitSession_sql_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Session_sql_stmtContext session_sql_stmt() throws RecognitionException {
		Session_sql_stmtContext _localctx = new Session_sql_stmtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_session_sql_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(68);
				ident();
				setState(69);
				match(JOIN);
				}
				break;
			case 2:
				{
				setState(71);
				ident();
				setState(72);
				match(QUIT);
				}
				break;
			case 3:
				{
				setState(74);
				ident();
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==BACKGROUND) {
					{
					setState(75);
					match(BACKGROUND);
					}
				}

				setState(78);
				sql_stmt();
				}
				break;
			}
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

	public static class Sql_stmtContext extends ParserRuleContext {
		public BlanksContext blanks() {
			return getRuleContext(BlanksContext.class,0);
		}
		public TerminalNode SQL_STRING() { return getToken(StatementsParser.SQL_STRING, 0); }
		public Sql_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterSql_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitSql_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitSql_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Sql_stmtContext sql_stmt() throws RecognitionException {
		Sql_stmtContext _localctx = new Sql_stmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_sql_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			blanks();
			setState(83);
			match(SQL_STRING);
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

	public static class Sql_endContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(StatementsParser.SEMICOLON, 0); }
		public Sql_endContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql_end; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterSql_end(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitSql_end(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitSql_end(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Sql_endContext sql_end() throws RecognitionException {
		Sql_endContext _localctx = new Sql_endContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_sql_end);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(SEMICOLON);
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

	public static class Empty_stmtContext extends ParserRuleContext {
		public TerminalNode END_LINE() { return getToken(StatementsParser.END_LINE, 0); }
		public List<TerminalNode> BLANK() { return getTokens(StatementsParser.BLANK); }
		public TerminalNode BLANK(int i) {
			return getToken(StatementsParser.BLANK, i);
		}
		public Empty_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_empty_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterEmpty_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitEmpty_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitEmpty_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Empty_stmtContext empty_stmt() throws RecognitionException {
		Empty_stmtContext _localctx = new Empty_stmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_empty_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BLANK) {
				{
				{
				setState(87);
				match(BLANK);
				}
				}
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(93);
			match(END_LINE);
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

	public static class Shell_commandContext extends ParserRuleContext {
		public TerminalNode SHEEL_COMMAND() { return getToken(StatementsParser.SHEEL_COMMAND, 0); }
		public Shell_commandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shell_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterShell_command(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitShell_command(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitShell_command(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Shell_commandContext shell_command() throws RecognitionException {
		Shell_commandContext _localctx = new Shell_commandContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_shell_command);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(SHEEL_COMMAND);
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

	public static class BlanksContext extends ParserRuleContext {
		public List<TerminalNode> BLANK() { return getTokens(StatementsParser.BLANK); }
		public TerminalNode BLANK(int i) {
			return getToken(StatementsParser.BLANK, i);
		}
		public BlanksContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blanks; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterBlanks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitBlanks(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitBlanks(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlanksContext blanks() throws RecognitionException {
		BlanksContext _localctx = new BlanksContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_blanks);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BLANK) {
				{
				{
				setState(97);
				match(BLANK);
				}
				}
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class IdentContext extends ParserRuleContext {
		public TerminalNode SESSION_ID() { return getToken(StatementsParser.SESSION_ID, 0); }
		public IdentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ident; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterIdent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitIdent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitIdent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentContext ident() throws RecognitionException {
		IdentContext _localctx = new IdentContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_ident);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(SESSION_ID);
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

	public static class Single_commentContext extends ParserRuleContext {
		public TerminalNode S_COMMENT() { return getToken(StatementsParser.S_COMMENT, 0); }
		public Single_commentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_single_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).enterSingle_comment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof StatementsListener ) ((StatementsListener)listener).exitSingle_comment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StatementsVisitor ) return ((StatementsVisitor<? extends T>)visitor).visitSingle_comment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Single_commentContext single_comment() throws RecognitionException {
		Single_commentContext _localctx = new Single_commentContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_single_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(S_COMMENT);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\23n\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\5\2 \n\2\3\2\6\2#\n\2\r\2\16\2"+
		"$\3\2\5\2(\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\65\n\3"+
		"\3\4\3\4\3\5\3\5\5\5;\n\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bO\n\b\3\b\3\b\5\bS\n\b\3\t\3\t\3\t\3\n"+
		"\3\n\3\13\7\13[\n\13\f\13\16\13^\13\13\3\13\3\13\3\f\3\f\3\r\7\re\n\r"+
		"\f\r\16\rh\13\r\3\16\3\16\3\17\3\17\3\17\2\2\20\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\34\2\2\2n\2\37\3\2\2\2\4\64\3\2\2\2\6\66\3\2\2\2\b:\3\2\2\2"+
		"\n>\3\2\2\2\fB\3\2\2\2\16R\3\2\2\2\20T\3\2\2\2\22W\3\2\2\2\24\\\3\2\2"+
		"\2\26a\3\2\2\2\30f\3\2\2\2\32i\3\2\2\2\34k\3\2\2\2\36 \5\6\4\2\37\36\3"+
		"\2\2\2\37 \3\2\2\2 \"\3\2\2\2!#\5\4\3\2\"!\3\2\2\2#$\3\2\2\2$\"\3\2\2"+
		"\2$%\3\2\2\2%\'\3\2\2\2&(\5\6\4\2\'&\3\2\2\2\'(\3\2\2\2(\3\3\2\2\2)*\5"+
		"\20\t\2*+\5\22\n\2+\65\3\2\2\2,-\5\16\b\2-.\5\22\n\2.\65\3\2\2\2/\65\5"+
		"\34\17\2\60\65\5\24\13\2\61\65\5\26\f\2\62\65\5\b\5\2\63\65\5\6\4\2\64"+
		")\3\2\2\2\64,\3\2\2\2\64/\3\2\2\2\64\60\3\2\2\2\64\61\3\2\2\2\64\62\3"+
		"\2\2\2\64\63\3\2\2\2\65\5\3\2\2\2\66\67\7\21\2\2\67\7\3\2\2\28;\5\n\6"+
		"\29;\5\f\7\2:8\3\2\2\2:9\3\2\2\2;<\3\2\2\2<=\5\22\n\2=\t\3\2\2\2>?\7\17"+
		"\2\2?@\7\3\2\2@A\7\22\2\2A\13\3\2\2\2BC\7\17\2\2CD\7\3\2\2DE\7\20\2\2"+
		"E\r\3\2\2\2FG\5\32\16\2GH\7\b\2\2HS\3\2\2\2IJ\5\32\16\2JK\7\6\2\2KS\3"+
		"\2\2\2LN\5\32\16\2MO\7\7\2\2NM\3\2\2\2NO\3\2\2\2OP\3\2\2\2PQ\5\20\t\2"+
		"QS\3\2\2\2RF\3\2\2\2RI\3\2\2\2RL\3\2\2\2S\17\3\2\2\2TU\5\30\r\2UV\7\22"+
		"\2\2V\21\3\2\2\2WX\7\5\2\2X\23\3\2\2\2Y[\7\t\2\2ZY\3\2\2\2[^\3\2\2\2\\"+
		"Z\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^\\\3\2\2\2_`\7\13\2\2`\25\3\2\2\2ab\7\23"+
		"\2\2b\27\3\2\2\2ce\7\t\2\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2g\31"+
		"\3\2\2\2hf\3\2\2\2ij\7\16\2\2j\33\3\2\2\2kl\7\r\2\2l\35\3\2\2\2\13\37"+
		"$\'\64:NR\\f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}