// Generated from PigLatin.g4 by ANTLR 4.9.2

    package igriega.piglatin.antlr4;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PigLatinLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, NUMERUS=14, DECIMALIS=15, SI=16, 
		LPAREN=17, RPAREN=18, LBRACE=19, RBRACE=20, FINIS=21, COLON=22, ESTO=23, 
		SEMICOLON=24, ID=25, INT=26, PRINT=27, WS=28;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "NUMERUS", "DECIMALIS", "SI", "LPAREN", 
			"RPAREN", "LBRACE", "RBRACE", "FINIS", "COLON", "ESTO", "SEMICOLON", 
			"ID", "INT", "PRINT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "'*'", "'/'", "'+'", "'-'", "'>'", "'<'", "'>='", "'<='", 
			"'=='", "'!='", "'||'", "'&&'", "'numerus'", "'decimalis'", "'si'", "'('", 
			"')'", "'{'", "'}'", "'finis'", "':'", "'esto'", "';'", null, null, "'>>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "NUMERUS", "DECIMALIS", "SI", "LPAREN", "RPAREN", "LBRACE", 
			"RBRACE", "FINIS", "COLON", "ESTO", "SEMICOLON", "ID", "INT", "PRINT", 
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


	public PigLatinLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PigLatin.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\36\u009d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\7\32\u008a"+
		"\n\32\f\32\16\32\u008d\13\32\3\33\6\33\u0090\n\33\r\33\16\33\u0091\3\34"+
		"\3\34\3\34\3\35\6\35\u0098\n\35\r\35\16\35\u0099\3\35\3\35\2\2\36\3\3"+
		"\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36\3\2\6\5"+
		"\2C\\aac|\6\2\62;C\\aac|\3\2\62;\5\2\13\f\17\17\"\"\2\u009f\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\3;\3\2\2\2\5=\3"+
		"\2\2\2\7?\3\2\2\2\tA\3\2\2\2\13C\3\2\2\2\rE\3\2\2\2\17G\3\2\2\2\21I\3"+
		"\2\2\2\23L\3\2\2\2\25O\3\2\2\2\27R\3\2\2\2\31U\3\2\2\2\33X\3\2\2\2\35"+
		"[\3\2\2\2\37c\3\2\2\2!m\3\2\2\2#p\3\2\2\2%r\3\2\2\2\'t\3\2\2\2)v\3\2\2"+
		"\2+x\3\2\2\2-~\3\2\2\2/\u0080\3\2\2\2\61\u0085\3\2\2\2\63\u0087\3\2\2"+
		"\2\65\u008f\3\2\2\2\67\u0093\3\2\2\29\u0097\3\2\2\2;<\7?\2\2<\4\3\2\2"+
		"\2=>\7,\2\2>\6\3\2\2\2?@\7\61\2\2@\b\3\2\2\2AB\7-\2\2B\n\3\2\2\2CD\7/"+
		"\2\2D\f\3\2\2\2EF\7@\2\2F\16\3\2\2\2GH\7>\2\2H\20\3\2\2\2IJ\7@\2\2JK\7"+
		"?\2\2K\22\3\2\2\2LM\7>\2\2MN\7?\2\2N\24\3\2\2\2OP\7?\2\2PQ\7?\2\2Q\26"+
		"\3\2\2\2RS\7#\2\2ST\7?\2\2T\30\3\2\2\2UV\7~\2\2VW\7~\2\2W\32\3\2\2\2X"+
		"Y\7(\2\2YZ\7(\2\2Z\34\3\2\2\2[\\\7p\2\2\\]\7w\2\2]^\7o\2\2^_\7g\2\2_`"+
		"\7t\2\2`a\7w\2\2ab\7u\2\2b\36\3\2\2\2cd\7f\2\2de\7g\2\2ef\7e\2\2fg\7k"+
		"\2\2gh\7o\2\2hi\7c\2\2ij\7n\2\2jk\7k\2\2kl\7u\2\2l \3\2\2\2mn\7u\2\2n"+
		"o\7k\2\2o\"\3\2\2\2pq\7*\2\2q$\3\2\2\2rs\7+\2\2s&\3\2\2\2tu\7}\2\2u(\3"+
		"\2\2\2vw\7\177\2\2w*\3\2\2\2xy\7h\2\2yz\7k\2\2z{\7p\2\2{|\7k\2\2|}\7u"+
		"\2\2},\3\2\2\2~\177\7<\2\2\177.\3\2\2\2\u0080\u0081\7g\2\2\u0081\u0082"+
		"\7u\2\2\u0082\u0083\7v\2\2\u0083\u0084\7q\2\2\u0084\60\3\2\2\2\u0085\u0086"+
		"\7=\2\2\u0086\62\3\2\2\2\u0087\u008b\t\2\2\2\u0088\u008a\t\3\2\2\u0089"+
		"\u0088\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2"+
		"\2\2\u008c\64\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u0090\t\4\2\2\u008f\u008e"+
		"\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"\66\3\2\2\2\u0093\u0094\7@\2\2\u0094\u0095\7@\2\2\u00958\3\2\2\2\u0096"+
		"\u0098\t\5\2\2\u0097\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u0097\3\2"+
		"\2\2\u0099\u009a\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\b\35\2\2\u009c"+
		":\3\2\2\2\6\2\u008b\u0091\u0099\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}