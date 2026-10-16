package framework.statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteHookStatement extends IHookStatement {
  public static String DELETE_IDENTIFIER = "-- delete";
  private String deleteRegexp_;

  public DeleteHookStatement(int line) {
    super(line, HookType.HT_RESULT);
  }

  public static boolean isDeleteHookStatement(String comment) {
    return comment.startsWith(DELETE_IDENTIFIER);
  }

  @Override
  protected String getHookIndent() {
    return DELETE_IDENTIFIER;
  }

  @Override
  protected String getHookRule() {
    return deleteRegexp_;
  }

  @Override
  public String apply(String line) {
    if (line == null) return null;
    Pattern pattern = Pattern.compile(deleteRegexp_);
    Matcher matcher = pattern.matcher(line);
    if (matcher.find())
      return null;
    return line;
  }

  @Override
  public IHookStatement parserHook(String comment) {
    String tmp = comment.substring(DELETE_IDENTIFIER.length()).trim();
    tmp = initName(tmp);
    deleteRegexp_ = tmp.substring(1);
    return this;
  }
}
