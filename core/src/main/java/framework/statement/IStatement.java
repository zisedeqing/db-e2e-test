package framework.statement;

import framework.ExecContext;
import framework.ExecException;
import framework.ResultWriter;
import framework.SessionManager;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public abstract class IStatement {
  protected static Logger LOGGER = LogManager.getLogger(IStatement.class);

  private int lineNumber_ = 0;

  protected Pair<ResultWriter, ResultWriter> writer_;

  protected ExecContext execContext_;


  public IStatement(ExecContext execContext, int lineNumber) {
    lineNumber_ = lineNumber;
    execContext_ = execContext;
  }

  public int getLineNumber() {
    return lineNumber_;
  }

  public void setLineNumber(int lineNumber_) {
    this.lineNumber_ = lineNumber_;
  }

  public void execute() throws ExecException {
    if (execContext_ != null) {
      execContext_.getStatistic().increSqlCount();
    }
    writeStatement();
  }

  public void setWriter(Pair<ResultWriter, ResultWriter> writer) {
    writer_ = writer;
  }

  public void writeStatement() throws ExecException {
    String str = toString();
    writer_.a.write(str);
    if (writer_.b != null)
      writer_.b.write(str);
  }

  public void writeLine(String line) throws ExecException {
    writer_.a.writeNewLine(line);
    if (writer_.b != null)
      writer_.b.writeNewLine(line);
  }

  public void writeLine(String finalLine, String originLine) throws ExecException {
    if (finalLine != null)
      writer_.a.writeNewLine(finalLine);
    if (writer_.b != null && originLine != null)
      writer_.b.writeNewLine(originLine);
  }

  public SessionManager getSessionManager() {
    return execContext_ != null ? execContext_.getSessionManager() : null;
  }

  public Map<String, String> getEnvs() {
    return execContext_ != null ? execContext_.getEnvs() : null;
  }

  public boolean isNotEmptyNormalStatement() {
    return (this instanceof INormalStatement) && !(this instanceof EmptyStatement);
  }

  public String applyHooks(String line, List<IHookStatement> hooks, IHookStatement.HookType type) {
    if (hooks == null || hooks.isEmpty()) return line;

    String result = line;
    for (IHookStatement hookStatement : hooks) {
      if (result == null) break;

      if (hookStatement.getHookType() != type &&
          hookStatement.getHookType() != IHookStatement.HookType.HT_COMBINE) continue;

      LOGGER.trace(String.format("applyHooks(%s): %s", type, hookStatement.toString()));
      if (hookStatement.getHookType() == IHookStatement.HookType.HT_COMBINE)
        result = ((CombineHookStatement)hookStatement).apply(result, type);
      else
        result = hookStatement.apply(result);
    }
    return result;
  }
}
