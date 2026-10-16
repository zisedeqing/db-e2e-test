package framework;

import framework.parser.ParserUtils;
import framework.statement.IHookStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HookManager {
  private static Logger LOGGER = LogManager.getLogger(HookManager.class);

  private Map<String, IHookStatement> globalHooks_ = new HashMap<>();
  private Map<String, IHookStatement> fileHooks_ = new HashMap<>();

  private List<IHookStatement> runtimeHooks_ = new ArrayList<>();

  public HookManager() {
  }

  public void addGlobalHooks(String hookFile) throws ExecException {
    ParserUtils.parserScript(new ExecContext(this), hookFile, true);
  }

  protected void addHook(IHookStatement hookStatement, boolean isGlobal) {
    Map<String, IHookStatement> hooks = isGlobal ? globalHooks_ : fileHooks_;
    LOGGER.trace(String.format("Add %s hook: %s", isGlobal ? "global" : "file", hookStatement));

    validStatement(hookStatement);
    IHookStatement old = hooks.put(hookStatement.getName(), hookStatement);
    if (old != null) {
      throw new RuntimeException(String.format("[Line: %d] - %s has exists, current is: %s, old is: %s",
              hookStatement.getLineNumber(), hookStatement.getName(), hookStatement, old));
    }
  }

  protected void clearFileHooks() {
    fileHooks_.clear();
    runtimeHooks_.clear();
  }

  public IHookStatement getHook(String hookName) {
    IHookStatement hook = fileHooks_.get(hookName);
    if (hook != null) return hook;
    return globalHooks_.get(hookName);
  }

  private void validStatement(IHookStatement statement) {
    if (!(statement instanceof IHookStatement)) {
      throw new RuntimeException(String.format("[Line: %d] - Statement is not hook statement",
              statement.getLineNumber()));
    }
    IHookStatement hookStatement = statement;
    if (hookStatement.getName() == null) {
      throw new RuntimeException(String.format("[Line: %d] - Anonymous hook statement, ignore it",
              statement.getLineNumber()));
    }
  }

  // runtime hooks
  public void pushHook(IHookStatement statement) {
    LOGGER.trace("pushHook: " + statement.toString());
    runtimeHooks_.add(statement);
  }

  public List<IHookStatement> peekHooks() {
    return runtimeHooks_;
  }

  public void popSingleHook() {
    if (runtimeHooks_.isEmpty()) return;
    int currIdx = runtimeHooks_.size() - 1;
    while (currIdx >= 0) {
      IHookStatement hook = runtimeHooks_.get(currIdx);
      if (hook.getHookScope() == IHookStatement.HookScope.HS_FILE) break;
      if (hook.getHookScope() == IHookStatement.HookScope.HS_SINGLE) {
        runtimeHooks_.remove(currIdx);
        LOGGER.trace("popSingleHook: " + hook);
      }
      currIdx--;
    }
  }

  public IHookStatement popBlockHook() {
    if (runtimeHooks_.isEmpty()) return null;
    int currIdx = runtimeHooks_.size() - 1;
    while (currIdx >= 0) {
      IHookStatement hook = runtimeHooks_.get(currIdx);
      LOGGER.trace("popBlockHook: " + hook);
      if (hook.getHookScope() == IHookStatement.HookScope.HS_BLOCK) {
        return runtimeHooks_.remove(currIdx);
      }

      assert hook.getHookScope() != IHookStatement.HookScope.HS_FILE;
      runtimeHooks_.remove(currIdx);
      currIdx--;
    }
    return null;
  }
}
