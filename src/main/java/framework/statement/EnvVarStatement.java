package framework.statement;

import framework.ExecContext;
import framework.ExecException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EnvVarStatement extends INormalStatement {
  private String envVarName;
  private String envVarValue;
  private boolean isShellExpr;

  public EnvVarStatement(ExecContext execContext, int lineNumber,
                         String envVarName, String envVarValue, String statement, boolean isShellExpr) {
    super(execContext, lineNumber, statement);
    this.envVarName = envVarName;
    this.envVarValue = envVarValue;
    this.isShellExpr = isShellExpr;
  }

  @Override
  public void execute() throws ExecException {
    super.execute();
    String value = envVarValue;
    if (isShellExpr) {
      try {
        value = evalShellExpr();
      } catch (Exception e) {
        throw new ExecException(e);
      }
    }
    execContext_.getEnvs().put(envVarName, value);
  }

  private String evalShellExpr() throws IOException, ExecException, InterruptedException {
    //Process process = ShellStatement.startProcess(envVarValue, getEnvs());
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (getEnvs() != null) {
      processBuilder.environment().putAll(getEnvs());
    }
    processBuilder.command("bash", "-c", envVarValue);
    Process process = processBuilder.start();
    BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
    String result = handleOutput(bufferedReader);
    int status = process.waitFor();
    if (status != 0)
      throw new ExecException(handleOutput(new BufferedReader(new InputStreamReader(process.getErrorStream()))));
    return result;
  }

  private String handleOutput(BufferedReader bufferedReader) throws ExecException {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      boolean isFirstLine = true;
      while (true) {
        String line = bufferedReader.readLine();

        if (line == null) break;
        if (isFirstLine)
          isFirstLine = false;
        else
          stringBuilder.append(System.lineSeparator());
        stringBuilder.append(line);
      }
    } catch (IOException e) {
      throw new ExecException(e);
    }
    return stringBuilder.toString();
  }

  @Override
  public String toString() {
    return "$" + envVarValue + "=" + envVarValue + ";";
  }

  @Override
  public String toOriginString() {
    return getOriginStatement();
  }
}
