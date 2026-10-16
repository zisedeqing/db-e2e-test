package framework;

import framework.statement.IHookStatement;
import framework.statement.SessionStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ExecutorThread extends Thread {
    Logger LOGGER = LogManager.getLogger(ExecutorThread.class);

    private Session session_;
    private SessionStatement statement_;
    private List<IHookStatement> hooks_;

    public ExecutorThread(Session session, SessionStatement statement,
        List<IHookStatement> hooks) {
        session_ = session;
        statement_ = statement;
        hooks_ = hooks;
    }

    @Override
    public void run() {
        try {
            session_.execBgStatement(statement_, hooks_);
        } catch (ExecException e) {
            LOGGER.error("", e);
        }
        hooks_.clear();
    }

    public SessionStatement getStatement() {
        return statement_;
    }
}
