package framework.statement;

public class SortResultHookStatement extends IHookStatement{
  public static String IDENTIFIER = "-- sort result";

  public SortResultHookStatement(int lineNumber) {
    super(lineNumber, HookType.HT_RESULT);
  }

  @Override
  protected String getHookIndent() {
    return IDENTIFIER;
  }

  @Override
  protected String getHookRule() {
    return null;
  }

  @Override
  public String apply(String line) {
    return line;
  }

  @Override
  public IHookStatement parserHook(String comment) {
    return this;
  }

  public static boolean isSortResultStatement(String comment) {
    return comment.trim().equalsIgnoreCase(IDENTIFIER);
  }
}
