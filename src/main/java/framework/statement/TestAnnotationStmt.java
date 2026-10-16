package framework.statement;

import framework.ExecContext;
import framework.ExecException;

import java.io.IOException;
import java.util.List;

public class TestAnnotationStmt extends IStatement {
  public enum AnnotationType {
    AT_BEFORE,
    AT_AFTER,
    AT_CASE;

    public static AnnotationType fromString(String s) {
      if (s.equalsIgnoreCase("beforetest"))
        return AT_BEFORE;
      if (s.equalsIgnoreCase("aftertest"))
        return AT_AFTER;
      if (s.toLowerCase().startsWith("test"))
        return AT_CASE;
      throw new RuntimeException("Unknown annotation type: " + s);
    }
  }


  private AnnotationType annotationType_;
  private String caseName_;
  public TestAnnotationStmt(ExecContext execContext, int lineNumber, String text) {
    super(execContext, lineNumber);
    annotationType_ = AnnotationType.fromString(text);
    if (annotationType_ == AnnotationType.AT_CASE)
      caseName_ = text.substring("test".length());
    else
      caseName_ = text;
  }

  @Override
  public void execute() throws ExecException {
    writeStatement();
    switch (annotationType_) {
      case AT_BEFORE:
        execContext_.getStatistic().setInBefore();
        break;
      case AT_AFTER: {
        execContext_.getStatistic().setInAfter();
        break;
      }
      case AT_CASE:
        break;
    }
  }

  // ignore all of hooks
  @Override
  public String applyHooks(String line, List<IHookStatement> hooks, IHookStatement.HookType type) {
    return line;
  }

  public AnnotationType getAnnotationType() {
    return annotationType_;
  }

  @Override
  public String toString() {
    if (getAnnotationType() == AnnotationType.AT_CASE)
      return "@test" + caseName_;
    return "@" + caseName_;
  }
}
