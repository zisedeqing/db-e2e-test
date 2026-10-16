package framework.statement;

import framework.ExecContext;
import framework.ExecException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;

public class ShellStatement extends INormalStatement {

  public ShellStatement(ExecContext execContext, int lineNumber, String command) {
    super(execContext, lineNumber, command);
  }

  // copy from Runtime.exec(String command, String[] envp, File dir)
  public static Process startProcess(String stmt, Map<String, String> envs) throws IOException {
    StringTokenizer st = new StringTokenizer(stmt);
    String[] cmdarray = new String[st.countTokens()];
    for (int i = 0; st.hasMoreTokens(); i++)
      cmdarray[i] = st.nextToken();

    ProcessBuilder processBuilder = new ProcessBuilder(cmdarray);
    if (envs != null) {
      processBuilder.environment().putAll(envs);
    }

    return processBuilder.start();
  }


  private Process startProcess() throws IOException {
    return startProcess(getStatement(), getEnvs());
  }

  public void execute() throws ExecException {
    try {
      super.execute();
      Process process = startProcess();
      BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(process.getInputStream()));

      handleOutput(bufferedReader, true);
      int status = process.waitFor();
      if (status != 0)
        handleOutput(new BufferedReader(new InputStreamReader(process.getErrorStream())), false);
    } catch (IOException e) {
      throw new ExecException(e);
    } catch (InterruptedException e) {
      writeLine(e.toString());
    }
  }

  private void handleOutput(BufferedReader bufferedReader, boolean applyHooks) throws ExecException {
    try {
      while (true) {
        String line = bufferedReader.readLine();

        if (line == null) break;

        if (applyHooks)
          writeLine(applyResultHooks(line), line);
        else
          writeLine(line);
      }
    } catch (IOException e) {
      throw new ExecException(e);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("!").append(getStatement()).append(";");
    return builder.toString();
  }

  @Override
  String toOriginString() {
    StringBuilder builder = new StringBuilder();
    builder.append("!").append(getOriginStatement()).append(";");
    return builder.toString();
  }
}
