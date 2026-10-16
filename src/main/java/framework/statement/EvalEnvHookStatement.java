package framework.statement;

import framework.ExecContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvalEnvHookStatement extends IHookStatement {
  private static String EVALENV_IDENTIFIER = "-- evalenv";
  private static String ENV_NAME_REGEXP = "[a-zA-Z_][a-zA-Z0-9_]*";
  private String envVarRegexp_ = "\\$\\{" + ENV_NAME_REGEXP + "}";

  public EvalEnvHookStatement(ExecContext execContext, int lineNumber) {
    super(execContext, lineNumber, HookType.HT_STATEMENT);
  }

  public static boolean isEvalEnvHookStatement(String comment) {
    return comment.startsWith(EvalEnvHookStatement.EVALENV_IDENTIFIER);
  }

  private String getEnvName(String matchName) {
    Pattern pattern = Pattern.compile(ENV_NAME_REGEXP);
    Matcher matcher = pattern.matcher(matchName);
    if (matcher.find())
      return matcher.group();
    return null;
  }

  private String getEnvValue(String name) {
    String val = execContext_.getEnvs().get(name);
    if (val == null) {
      val = System.getenv(name);
    }
    if (val == null)
      val = "";
    return val;
  }

  @Override
  protected String getHookIndent() {
    return EVALENV_IDENTIFIER;
  }

  @Override
  protected String getHookRule() {
    return envVarRegexp_;
  }

  @Override
  public String apply(String line) {
    Pattern pattern = Pattern.compile(envVarRegexp_);
    Matcher matcher = pattern.matcher(line);
    StringBuffer result = new StringBuffer();

    while (matcher.find()) {
      // get env var name
      String envName = getEnvName(matcher.group());
      if (envName == null || envName.isEmpty())
        continue;

      // eval env
      String envValue = getEnvValue(envName);
      matcher.appendReplacement(result, envValue);
    }
    matcher.appendTail(result);
    return result.toString();
  }

  @Override
  public IHookStatement parserHook(String comment) {
    String tmp = comment.substring(EVALENV_IDENTIFIER.length()).trim();
    tmp = initName(tmp);
    if (tmp == null || tmp.isEmpty())
      return this;
    if (!tmp.substring(1).isEmpty())
      envVarRegexp_ = tmp.substring(1);
    return this;
  }
}
