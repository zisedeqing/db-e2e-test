package framework.parsererror;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ScriptErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        throw new RuntimeException("Line " + line + ":" + charPositionInLine + " " + msg);
    }

    String getThrowPosition(ParserRuleContext ctx) {
        return ctx == null ? null :
                "Line: " + ctx.getStart().getLine() + ", postion: " +
                        ctx.getStart().getCharPositionInLine();
    }
}
