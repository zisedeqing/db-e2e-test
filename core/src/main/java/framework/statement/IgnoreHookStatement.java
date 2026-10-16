package framework.statement;

public class IgnoreHookStatement extends IHookStatement {
  public static String IGNORE_IDENTIFIER = "-- ignore result";

  public IgnoreHookStatement(int lineNumber) {
    super(lineNumber, HookType.HT_RESULT);
  }

  @Override
  protected String getHookIndent() {
    return IGNORE_IDENTIFIER;
  }

  @Override
  protected String getHookRule() {
    return null;
  }

  @Override
  public String apply(String line) {
    return null;
  }

  @Override
  public IHookStatement parserHook(String comment) {
    return this;
  }

  public static boolean isIgnoreStatement(String comment) {
    return comment.trim().equalsIgnoreCase(IGNORE_IDENTIFIER);
  }
}
