package framework.statement;

import framework.ExecContext;

public abstract class IHookStatement extends IStatement {
  public enum HookScope {
    HS_SINGLE,
    HS_BLOCK,
    HS_FILE
  }

  public enum HookType {
    HT_RESULT,
    HT_STATEMENT,
    HT_COMBINE
  }

  protected String name_;
  private HookScope hookScope_ = HookScope.HS_SINGLE;
  private HookType hookType_;

  public IHookStatement(int lineNumber, HookType hookType) {
    this(null, lineNumber, hookType);
  }

  public IHookStatement(ExecContext execContext, int lineNumber, HookType hookType) {
    super(execContext, lineNumber);
    name_ = null;
    hookType_ = hookType;
  }

  abstract protected String getHookIndent();
  abstract protected String getHookRule();

  public String getName() {
    return name_;
  }

  public boolean isAnonymous() {
    return name_ == null;
  }

  public void setHookScope(HookScope scope) {
    hookScope_ = scope;
  }

  public HookScope getHookScope() {
    return hookScope_;
  }

  public HookType getHookType() {
    return hookType_;
  }

  public abstract String apply(String line);

  public abstract IHookStatement parserHook(String comment);

  protected String initName(String string) {
    if (string.startsWith("(")) {
      int pos = string.indexOf(')');
      if (pos != -1)
        name_ = string.substring(1, pos);
      // skip ')'
      string = string.substring(pos + 1).trim();
    }
    return string;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getHookIndent());
    if (name_ != null)
      builder.append("(").append(name_).append(")");

    String rule = getHookRule();
    if (rule != null)
      builder.append(":").append(getHookRule());
    builder.append("\n");
    return builder.toString();
  }
}
