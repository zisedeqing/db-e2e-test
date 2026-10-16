// Generated from Statements.g4 by ANTLR 4.9.2
package framework.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StatementsLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, COLON=2, SEMICOLON=3, QUIT=4, BACKGROUND=5, JOIN=6, BLANK=7, WS=8, 
		END_LINE=9, M_COMMENT=10, S_COMMENT=11, SESSION_ID=12, ENV_VARIABLE_NAME=13, 
		ENV_SHELL_EXPR=14, TEST_ANNOTATION=15, SQL_STRING=16, SHEEL_COMMAND=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
			"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "HEX_DIGIT", 
			"DIGIT", "LETTER", "COLON", "SEMICOLON", "QUIT", "BACKGROUND", "JOIN", 
			"BLANK", "WS", "END_LINE", "M_COMMENT", "S_COMMENT", "SESSION_ID", "ENV_VARIABLE_NAME", 
			"ENV_SHELL_EXPR", "TEST_ANNOTATION", "SQL_STRING", "SHEEL_COMMAND"
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


	public StatementsLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Statements.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23\u0114\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17"+
		"\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26"+
		"\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35"+
		"\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\6&"+
		"\u00a9\n&\r&\16&\u00aa\3&\3&\3\'\5\'\u00b0\n\'\3\'\3\'\3(\3(\3(\3(\7("+
		"\u00b8\n(\f(\16(\u00bb\13(\3(\3(\3(\3(\3(\3)\3)\3)\3)\5)\u00c6\n)\3)\7"+
		")\u00c9\n)\f)\16)\u00cc\13)\3)\3)\5)\u00d0\n)\3)\5)\u00d3\n)\3*\3*\3*"+
		"\3*\6*\u00d9\n*\r*\16*\u00da\3*\3*\6*\u00df\n*\r*\16*\u00e0\5*\u00e3\n"+
		"*\3*\3*\3+\3+\3+\3+\6+\u00eb\n+\r+\16+\u00ec\3,\3,\3,\3,\3,\6,\u00f4\n"+
		",\r,\16,\u00f5\3,\3,\3-\3-\3-\3-\6-\u00fe\n-\r-\16-\u00ff\3.\3.\3.\3."+
		"\7.\u0106\n.\f.\16.\u0109\13.\3/\3/\3/\3/\6/\u010f\n/\r/\16/\u0110\3/"+
		"\3/\4\u00b9\u00ca\2\60\3\3\5\2\7\2\t\2\13\2\r\2\17\2\21\2\23\2\25\2\27"+
		"\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2\65\2\67\29\2"+
		";\2=\2?\4A\5C\6E\7G\bI\tK\nM\13O\fQ\rS\16U\17W\20Y\21[\22]\23\3\2\"\4"+
		"\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2KKk"+
		"k\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2"+
		"TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\|"+
		"|\4\2\62;CH\3\2\62;\4\2C\\c|\4\2\13\13\"\"\3\2==\f\2\f\f\17\17\"#&&(("+
		"**//\61\61=?BB\2\u010e\2\3\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2"+
		"E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3"+
		"\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2"+
		"\2\3_\3\2\2\2\5a\3\2\2\2\7c\3\2\2\2\te\3\2\2\2\13g\3\2\2\2\ri\3\2\2\2"+
		"\17k\3\2\2\2\21m\3\2\2\2\23o\3\2\2\2\25q\3\2\2\2\27s\3\2\2\2\31u\3\2\2"+
		"\2\33w\3\2\2\2\35y\3\2\2\2\37{\3\2\2\2!}\3\2\2\2#\177\3\2\2\2%\u0081\3"+
		"\2\2\2\'\u0083\3\2\2\2)\u0085\3\2\2\2+\u0087\3\2\2\2-\u0089\3\2\2\2/\u008b"+
		"\3\2\2\2\61\u008d\3\2\2\2\63\u008f\3\2\2\2\65\u0091\3\2\2\2\67\u0093\3"+
		"\2\2\29\u0095\3\2\2\2;\u0097\3\2\2\2=\u0099\3\2\2\2?\u009b\3\2\2\2A\u009d"+
		"\3\2\2\2C\u009f\3\2\2\2E\u00a1\3\2\2\2G\u00a3\3\2\2\2I\u00a5\3\2\2\2K"+
		"\u00a8\3\2\2\2M\u00af\3\2\2\2O\u00b3\3\2\2\2Q\u00c5\3\2\2\2S\u00d4\3\2"+
		"\2\2U\u00e6\3\2\2\2W\u00ee\3\2\2\2Y\u00f9\3\2\2\2[\u0101\3\2\2\2]\u010a"+
		"\3\2\2\2_`\7?\2\2`\4\3\2\2\2ab\t\2\2\2b\6\3\2\2\2cd\t\3\2\2d\b\3\2\2\2"+
		"ef\t\4\2\2f\n\3\2\2\2gh\t\5\2\2h\f\3\2\2\2ij\t\6\2\2j\16\3\2\2\2kl\t\7"+
		"\2\2l\20\3\2\2\2mn\t\b\2\2n\22\3\2\2\2op\t\t\2\2p\24\3\2\2\2qr\t\n\2\2"+
		"r\26\3\2\2\2st\t\13\2\2t\30\3\2\2\2uv\t\f\2\2v\32\3\2\2\2wx\t\r\2\2x\34"+
		"\3\2\2\2yz\t\16\2\2z\36\3\2\2\2{|\t\17\2\2| \3\2\2\2}~\t\20\2\2~\"\3\2"+
		"\2\2\177\u0080\t\21\2\2\u0080$\3\2\2\2\u0081\u0082\t\22\2\2\u0082&\3\2"+
		"\2\2\u0083\u0084\t\23\2\2\u0084(\3\2\2\2\u0085\u0086\t\24\2\2\u0086*\3"+
		"\2\2\2\u0087\u0088\t\25\2\2\u0088,\3\2\2\2\u0089\u008a\t\26\2\2\u008a"+
		".\3\2\2\2\u008b\u008c\t\27\2\2\u008c\60\3\2\2\2\u008d\u008e\t\30\2\2\u008e"+
		"\62\3\2\2\2\u008f\u0090\t\31\2\2\u0090\64\3\2\2\2\u0091\u0092\t\32\2\2"+
		"\u0092\66\3\2\2\2\u0093\u0094\t\33\2\2\u00948\3\2\2\2\u0095\u0096\t\34"+
		"\2\2\u0096:\3\2\2\2\u0097\u0098\t\35\2\2\u0098<\3\2\2\2\u0099\u009a\t"+
		"\36\2\2\u009a>\3\2\2\2\u009b\u009c\7<\2\2\u009c@\3\2\2\2\u009d\u009e\7"+
		"=\2\2\u009eB\3\2\2\2\u009f\u00a0\7#\2\2\u00a0D\3\2\2\2\u00a1\u00a2\7("+
		"\2\2\u00a2F\3\2\2\2\u00a3\u00a4\7>\2\2\u00a4H\3\2\2\2\u00a5\u00a6\7\""+
		"\2\2\u00a6J\3\2\2\2\u00a7\u00a9\t\37\2\2\u00a8\u00a7\3\2\2\2\u00a9\u00aa"+
		"\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac"+
		"\u00ad\b&\2\2\u00adL\3\2\2\2\u00ae\u00b0\7\17\2\2\u00af\u00ae\3\2\2\2"+
		"\u00af\u00b0\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\7\f\2\2\u00b2N\3"+
		"\2\2\2\u00b3\u00b4\7\61\2\2\u00b4\u00b5\7,\2\2\u00b5\u00b9\3\2\2\2\u00b6"+
		"\u00b8\13\2\2\2\u00b7\u00b6\3\2\2\2\u00b8\u00bb\3\2\2\2\u00b9\u00ba\3"+
		"\2\2\2\u00b9\u00b7\3\2\2\2\u00ba\u00bc\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bc"+
		"\u00bd\7,\2\2\u00bd\u00be\7\61\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c0\b("+
		"\3\2\u00c0P\3\2\2\2\u00c1\u00c2\7/\2\2\u00c2\u00c6\7/\2\2\u00c3\u00c4"+
		"\7\61\2\2\u00c4\u00c6\7\61\2\2\u00c5\u00c1\3\2\2\2\u00c5\u00c3\3\2\2\2"+
		"\u00c6\u00ca\3\2\2\2\u00c7\u00c9\13\2\2\2\u00c8\u00c7\3\2\2\2\u00c9\u00cc"+
		"\3\2\2\2\u00ca\u00cb\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00d2\3\2\2\2\u00cc"+
		"\u00ca\3\2\2\2\u00cd\u00d3\7\2\2\3\u00ce\u00d0\7\17\2\2\u00cf\u00ce\3"+
		"\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d3\7\f\2\2\u00d2"+
		"\u00cd\3\2\2\2\u00d2\u00cf\3\2\2\2\u00d3R\3\2\2\2\u00d4\u00d8\7*\2\2\u00d5"+
		"\u00d9\5=\37\2\u00d6\u00d9\7a\2\2\u00d7\u00d9\5;\36\2\u00d8\u00d5\3\2"+
		"\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d7\3\2\2\2\u00d9\u00da\3\2\2\2\u00da"+
		"\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00e2\3\2\2\2\u00dc\u00de\7<"+
		"\2\2\u00dd\u00df\5;\36\2\u00de\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0"+
		"\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00dc\3\2"+
		"\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e5\7+\2\2\u00e5"+
		"T\3\2\2\2\u00e6\u00ea\7&\2\2\u00e7\u00eb\5=\37\2\u00e8\u00eb\7a\2\2\u00e9"+
		"\u00eb\5;\36\2\u00ea\u00e7\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00e9\3\2"+
		"\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed"+
		"V\3\2\2\2\u00ee\u00ef\7&\2\2\u00ef\u00f3\7*\2\2\u00f0\u00f1\7^\2\2\u00f1"+
		"\u00f4\7=\2\2\u00f2\u00f4\n \2\2\u00f3\u00f0\3\2\2\2\u00f3\u00f2\3\2\2"+
		"\2\u00f4\u00f5\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7"+
		"\3\2\2\2\u00f7\u00f8\7+\2\2\u00f8X\3\2\2\2\u00f9\u00fd\7B\2\2\u00fa\u00fe"+
		"\5=\37\2\u00fb\u00fe\7a\2\2\u00fc\u00fe\5;\36\2\u00fd\u00fa\3\2\2\2\u00fd"+
		"\u00fb\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u00fd\3\2"+
		"\2\2\u00ff\u0100\3\2\2\2\u0100Z\3\2\2\2\u0101\u0107\n!\2\2\u0102\u0103"+
		"\7^\2\2\u0103\u0106\7=\2\2\u0104\u0106\n \2\2\u0105\u0102\3\2\2\2\u0105"+
		"\u0104\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107\u0108\3\2"+
		"\2\2\u0108\\\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010e\7#\2\2\u010b\u010c"+
		"\7^\2\2\u010c\u010f\7=\2\2\u010d\u010f\n \2\2\u010e\u010b\3\2\2\2\u010e"+
		"\u010d\3\2\2\2\u010f\u0110\3\2\2\2\u0110\u010e\3\2\2\2\u0110\u0111\3\2"+
		"\2\2\u0111\u0112\3\2\2\2\u0112\u0113\7=\2\2\u0113^\3\2\2\2\30\2\u00aa"+
		"\u00af\u00b9\u00c5\u00ca\u00cf\u00d2\u00d8\u00da\u00e0\u00e2\u00ea\u00ec"+
		"\u00f3\u00f5\u00fd\u00ff\u0105\u0107\u010e\u0110\4\b\2\2\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}