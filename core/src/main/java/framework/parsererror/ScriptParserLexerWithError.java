package framework.parsererror;

import framework.parser.StatementsLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;

public class ScriptParserLexerWithError extends StatementsLexer {
    public ScriptParserLexerWithError(CharStream input) {
        super(input);
    }

    @Override
    public void recover(LexerNoViableAltException e) {
        throw new RuntimeException(e);
    }

    @Override
    public void recover(RecognitionException re) {
        throw new RuntimeException(re);
    }
}
