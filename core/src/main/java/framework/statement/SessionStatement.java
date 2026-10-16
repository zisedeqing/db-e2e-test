package framework.statement;

import framework.ExecContext;
import framework.ExecException;
import framework.ExecutorThread;
import framework.Session;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.List;

public class SessionStatement extends INormalStatement {
    private String sessionId_ = Session.mainThreadSessionId;
    private Integer nodeId_ = null;
    private StatementType statementType_ = null;

    private String connectionOptions = null;

    public SessionStatement(String sql, int lineNumber,
        ExecContext execContext, String connOptions) {
        super(execContext, lineNumber, sql);
        connectionOptions = connOptions;
    }

    public SessionStatement(ExecContext execContext, int lineNumber) {
        super(execContext, lineNumber, null);
    }

    public void setSessionId(String sessionId) {
        this.sessionId_ = sessionId;
    }

    public void setNodeId(Integer asId) {
        nodeId_ = asId;
    }

    private Integer getNodeId() {
        return nodeId_;
    }

    public void setStatementFlag(StatementType statementType) {
        this.statementType_ = statementType;
    }

    public void setConnectionOptions(String options) {
        connectionOptions = options;
    }

    @Override
    public String toString() {
        return toStringInternal(false);
    }

    @Override
    public String toOriginString() {
        return toStringInternal(true);
    }

    private String toStringInternal(boolean isOrigin) {
        StringBuilder builder = new StringBuilder();
        String statement = isOrigin ? getOriginStatement() : getStatement();
        if (sessionId_ != null && !sessionId_.equals(Session.mainThreadSessionId)) {
            builder.append("(").append(sessionId_);
            if (nodeId_ != null) {
                builder.append(":").append(nodeId_);
            }
            builder.append(")");
            if (statementType_ != null)
                builder.append(statementType_);
            if (statement != null)
                builder.append(" ");
        }

        if (statementType_ == StatementType.JOIN)
            builder.append(getJoinStmtString());
        else {
            if (statement != null)
                builder.append(statement);
            builder.append(";");
        }
        return builder.toString();
    }

    public String getJoinStmtString() {
        Session session = getSessionManager().getSession(sessionId_);
        if (session == null || session.getExecutorThread() == null)
            return "";
        SessionStatement bgStmt = session.getExecutorThread().getStatement();
        StringBuilder builder = new StringBuilder();
        builder.append("-- ").append(bgStmt.getPreferOriginStatement()).append(";");
        return builder.toString();
    }

    @Override
    public void execute() throws ExecException {
        long startTime = System.currentTimeMillis();
        execInner();
        long endTime = System.currentTimeMillis();
        writeLine(null, "Time: " + (endTime - startTime) + "ms");
    }

    private void execInner() throws ExecException {
        super.execute();
        if (statementType_ == null) {
            executeSqlStmt();
            return;
        }
        switch (statementType_) {
            case JOIN:
                executeJoinStmt();
                break;
            case QUIT:
                executeQuitStmt();
                break;
            case BACKGROUND:
                executeSqlStmt();
                break;
            default:
                break;
        }
    }

    public void executeJoinStmt() throws ExecException {
        Session session = getSessionManager().getSession(sessionId_);
        if (session == null)
            throw new ExecException(String.format("[Line: %d] - Session \"%s\" not exists",
                    getLineNumber(), sessionId_));
        if (session.getExecutorThread() == null) {
            throw new ExecException(String.format("[Line: %d] - Session \"%s\" has not bind any executor",
                    getLineNumber(), sessionId_));
        }
        try {
            session.getExecutorThread().join();
        } catch (InterruptedException e) {
            throw new ExecException(e);
        }

        session.resetExecutorThread();
        Pair<String, String> bgResult = session.getBgStmtResult();
        writeLine(bgResult.a, bgResult.b);
    }

    public void executeQuitStmt() throws ExecException {
        Session session = getSessionManager().removeSession(sessionId_);
        if (session == null)
            throw new ExecException(String.format("[Line: %d] - Not found session: %s",
                    getLineNumber(), sessionId_));
        session.close();
    }

    public void executeSqlStmt() throws ExecException {
        Session session = getSessionManager().getOrCreateSession(sessionId_, getNodeId(), connectionOptions);
        if (session.getExecutorThread() != null) {
            throw new ExecException(String.format("[Line: %d] - Session \"%s\" had bind executor",
                    getLineNumber(), sessionId_));
        }
        if (statementType_ == StatementType.BACKGROUND) {
            if (sessionId_ == Session.mainThreadSessionId)
                throw new ExecException(String.format("[Line: %d] - Main session sql can not executed at background",
                        getLineNumber()));

            List<IHookStatement> hooks = new ArrayList<>(execContext_.getHookManager().peekHooks());
            ExecutorThread executorThread = new ExecutorThread(session, this, hooks);
            session.setExecutorThread(executorThread);
            executorThread.start();
        } else {
            // execute in main thread using given session
            session.execStatement(this);
        }
    }
}
