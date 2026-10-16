package framework.parsererror;

import org.antlr.v4.runtime.*;

public class ScriptErrorStrategy extends DefaultErrorStrategy {
    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        if (ignoreEofException(e)) return;
        throw new RuntimeException(e);
    }

    @Override
    public Token recoverInline(Parser recognizer)
            throws RecognitionException
    {
        throw new RuntimeException(new InputMismatchException(recognizer));
    }

    @Override
    public void sync(Parser recognizer) { }

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
        if (ignoreEofException(e)) return;
        super.reportError(recognizer, e);
    }

    private boolean ignoreEofException(RecognitionException e) {
        if ( e instanceof NoViableAltException) {
            NoViableAltException noViableAltException = (NoViableAltException) e;
            // does not report "null valid input" exception, such as "all comments" or null input.
            if (noViableAltException.getStartToken().getType() == Token.EOF) return true;
        }
        return false;
    }
}
