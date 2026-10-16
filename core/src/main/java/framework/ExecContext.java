package framework;

import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;
import java.util.Map;

public class ExecContext {
  private SessionManager sessionManager_;
  private Map<String, String> envs_ = null;

  private HookManager hookManager_;

  private TestCaseStatistic statistic_;

  public ExecContext(SessionManager sessionManager, Map<String, String> envs, HookManager hookManager) {
    sessionManager_ = sessionManager;
    if (envs != null)
      this.envs_ = new HashMap<>(envs);
    hookManager_ = hookManager;
    statistic_ = new TestCaseStatistic();
  }

  public ExecContext(HookManager hookManager) {
    this(null, null, hookManager);
  }

  public SessionManager getSessionManager() {
    return sessionManager_;
  }

  public Map<String, String> getEnvs() {
    return envs_;
  }

  public HookManager getHookManager() {
    return hookManager_;
  }

  public TestCaseStatistic getStatistic() {
    return statistic_;
  }
}
