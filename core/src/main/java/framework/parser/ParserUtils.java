package framework.parser;

import framework.ExecContext;
import framework.ExecException;
import framework.ScriptVisitor;
import framework.parsererror.ScriptErrorListener;
import framework.parsererror.ScriptErrorStrategy;
import framework.statement.IStatement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ParserUtils {
  public static class ParserResult {
    public List<IStatement> statements_;
    public boolean hasHookStatement_;

    public ParserResult(List<IStatement> statements, boolean hasHookStatement) {
      statements_ = statements;
      hasHookStatement_ = hasHookStatement;
    }
  }

  public static ParserResult parserScript(ExecContext execCtx, String inputFile, boolean isHookFile) throws ExecException {
    StatementsLexer lexer = null;
    try {
      lexer = new StatementsLexer(CharStreams.fromFileName(
              inputFile, StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ExecException(e);
    }

    lexer.removeErrorListeners();
    lexer.addErrorListener(new ScriptErrorListener());

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    StatementsParser parser = new StatementsParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new ScriptErrorListener());
    parser.setErrorHandler(new ScriptErrorStrategy());

    ParseTree statement = parser.script();
    ScriptVisitor visitor = new ScriptVisitor(execCtx, isHookFile);
    visitor.visit(statement);
    visitor.checkAnnotation();
    return new ParserResult(visitor.getStatements(), visitor.hasHookStatement());
  }
}
