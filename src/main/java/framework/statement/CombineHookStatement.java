package framework.statement;

import com.google.common.base.Joiner;
import framework.ExecContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// now only replace/combine hook
public class CombineHookStatement extends IHookStatement {
  private static final String COMBINE_IDENTIFIER = "-- hooks";
  private List<IHookStatement> hookStatements_ = null;


  private String endString_ = null;

  public CombineHookStatement(ExecContext execContext, int lineNumber) {
    super(lineNumber, HookType.HT_COMBINE);
    execContext_ = execContext;
  }

  public boolean isEnd() {
    return endString_ != null;
  }

  @Override
  protected String getHookIndent() {
    return COMBINE_IDENTIFIER;
  }

  @Override
  protected String getHookRule() {
    if (hookStatements_ != null || !hookStatements_.isEmpty())
      return Joiner.on(",").join(
            hookStatements_.stream().map((h)->h.getName()).collect(Collectors.toList()));
    return null;
  }

  @Override
  public String apply(String line) {
    throw new RuntimeException("CombineHookStatement not support apply(String line)");
  }

  public String apply(String line, IHookStatement.HookType type) {
    return applyHooks(line, hookStatements_, type);
  }

  @Override
  public IHookStatement parserHook(String comment) {
    String tmp = comment.substring(COMBINE_IDENTIFIER.length()).trim();
    tmp = initName(tmp);
    if (tmp == null || tmp.isEmpty() ||
      !tmp.startsWith(":")) {
      endString_ = comment;
      return this;
    }

    // skip ':'
    String hookNames = tmp.substring(1);
    String[] hookNameArr = hookNames.split(",");
    if (hookNameArr == null || hookNameArr.length == 0)
      throw new RuntimeException("Invalid combine hook format, " +
              "expect format is: -- hooks[(hook name)]: name, name,...");

    hookStatements_ = new ArrayList<>();
    for (String hook : hookNameArr) {
      String hookName = hook.trim();
      IHookStatement hookStatement = execContext_.getHookManager().getHook(hookName);
      if (hookStatement == null)
        throw new RuntimeException("Can not found hook, hook name: " + hookName);
      hookStatements_.add(hookStatement);
    }
    return this;
  }

  public static boolean isCombineHook(String comment) {
    return comment != null ? comment.startsWith(COMBINE_IDENTIFIER) : false;
  }

  @Override
  public String toString() {
    if (endString_ != null) return endString_;
    return super.toString();
  }
}
