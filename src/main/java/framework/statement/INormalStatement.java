package framework.statement;

import framework.ExecContext;
import framework.ExecException;

import java.util.List;

abstract public class INormalStatement extends IStatement {
  // statement that originStatement_ applied statement hooks
  protected String statement_ = null;
  // the origin statement that user input
  protected String originStatement_;

  public String getStatement() {
    return statement_ == null ? originStatement_ : statement_;
  }

  public String getOriginStatement() {
    return originStatement_;
  }

  public String getPreferOriginStatement() {
    return originStatement_ != null ? originStatement_ : statement_;
  }

  public INormalStatement(ExecContext execContext, int lineNumber, String statement) {
    super(execContext, lineNumber);
    originStatement_ = statement;
  }

  public void startExecute() {
    if (originStatement_ != null) {
      String res = applyStatementHook(originStatement_);
      if (res != null && !originStatement_.equals(res))
        statement_ = res;
    }
  }

  public void endExecute() {
    execContext_.getHookManager().popSingleHook();
  }

  public void writeStatement() throws ExecException {
    writer_.a.write(toOriginString());
    if (writer_.b != null)
      writer_.b.write(toString());
  }

  public String applyStatementHook(String line) {
    List<IHookStatement> hooks = execContext_.getHookManager().peekHooks();
    return applyHooks(line, hooks, IHookStatement.HookType.HT_STATEMENT);
  }

  public String applyResultHooks(String line) {
    List<IHookStatement> hooks = execContext_.getHookManager().peekHooks();
    return applyResultHooks(line, hooks);
  }

  public String applyResultHooks(String line, List<IHookStatement> hooks) {
    return applyHooks(line, hooks, IHookStatement.HookType.HT_RESULT);
  }

  abstract String toOriginString();

  public boolean needSortedResult() {
    List<IHookStatement> hooks = execContext_.getHookManager().peekHooks();
    if (hooks == null || hooks.isEmpty()) return false;
    return hooks.stream().anyMatch(hook -> hook instanceof SortResultHookStatement);
  }
}
