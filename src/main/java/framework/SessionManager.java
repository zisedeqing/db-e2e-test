package framework;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {
  private static Logger LOGGER = LogManager.getLogger(SessionManager.class);
  private Map<String, Session> sessions_ = new HashMap<>();
  private List<ImpaladService> impalads_ = new ArrayList<>();

  public SessionManager() {
  }

  public void setImpalads(List<ImpaladService> impalads) {
    impalads_ = impalads;
  }

  public List<ImpaladService> getImpalads() {
    return impalads_;
  }

  public Session removeSession(String sessionId) {
    return sessions_.remove(sessionId);
  }

  public Session getOrCreateSession(String sessionId, Integer nodeId, String connOptions) throws ExecException {
    int realNodeId = nodeId == null ? 0 : nodeId;
    Session session = getSession(sessionId);
    if (session == null) {
      session = new Session(sessionId, realNodeId,
              impalads_.get(realNodeId), connOptions);
      sessions_.put(sessionId, session);
    }
    if (nodeId != null && session.getNodeId() != nodeId)
      throw new ExecException("Expect as id to be " + nodeId + " but was " + session.getNodeId() + " for session: " + sessionId);
    return session;
  }

  public Session getSession(String sessionId) {
    return sessions_.get(sessionId);
  }

  public void close() {
    for (Session session : sessions_.values()) {
      if (session.getExecutorThread() != null) {
        try {
          session.getExecutorThread().join();
        } catch (InterruptedException e) {
          LOGGER.error("", e);
        }
      }
      try {
        session.close();
      } catch (ExecException e) {
        LOGGER.error("", e);
      }
    }
    sessions_.clear();
  }
}
