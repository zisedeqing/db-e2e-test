package framework;

import framework.connection.DbConnector;
import framework.connection.Endpoint;
import framework.statement.IHookStatement;
import framework.statement.SessionStatement;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private static Logger LOGGER = LogManager.getLogger(Session.class);
    public static String mainThreadSessionId = "__MAIN-THREAD__";

    private String sessionId_;
    private int nodeId_;

    private Endpoint endpoint_;
    private DbConnector connector_;
    private Connection conn_;

    private ExecutorThread executorThread_ = null;

    // <final result, origin result>
    private Pair<String, String> bgStmtResult_ = null;
    private String connOptions = null;

    public Session(String sessionId, int nodeId, Endpoint endpoint, DbConnector connector,
                   String connOptions) throws ExecException {
        this.sessionId_ = sessionId;
        this.endpoint_ = endpoint;
        this.connector_ = connector;
        this.connOptions = connOptions;
        this.nodeId_ = nodeId;
        conn_ = createConnection();
    }

    public int getNodeId() {
        return nodeId_;
    }

    private Connection createConnection() throws ExecException {
        String db = ConnectionOptionsManager.getGlobalOptions().getDbName();
        // Pass a raw options fragment. Each connector owns its JDBC URL syntax
        // and decides whether the fragment needs '?' or '&'.
        String url = connector_.buildJdbcUrl(endpoint_, db, connOptions);
        String safeUrl = sanitizeJdbcUrl(url);
        LOGGER.trace("[" + sessionId_ + "] jdbc url: " + safeUrl);

        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOGGER.error("Get jdbc connection " + safeUrl);
            throw new ExecException(e);
        }
    }

    static String sanitizeJdbcUrl(String url) {
        if (url == null) return null;
        return url.replaceAll("(?i)([?&](?:password|pwd)=)[^&]*", "$1***");
    }

    public ExecutorThread getExecutorThread() {
        return executorThread_;
    }

    public void setExecutorThread(ExecutorThread executorThread) {
        this.executorThread_ = executorThread;
    }

    public void resetExecutorThread() {
        executorThread_ = null;
    }

    public void close() throws ExecException {
        try {
            conn_.close();
        } catch (SQLException e) {
            throw new ExecException(e);
        }
    }

    public void execStatement(@NotNull SessionStatement statement) throws ExecException {
        SqlResultBuilder resultBuilder = new SqlResultBuilder();
        SqlResultBuilder originResultBuilder = new SqlResultBuilder();
        RowHandler handler = (String line) -> {
            originResultBuilder.append(line);
            String res = statement.applyResultHooks(line);
            if (res != null) {
                resultBuilder.append(res);
            }
        };
        execStatementInternal(statement, handler);

        if (statement.needSortedResult()) {
            resultBuilder.sort();
        }
        statement.writeLine(resultBuilder.getLineCount() > 0 ? resultBuilder.toString() : null,
            originResultBuilder.toString());
    }

    public void execBgStatement(@NotNull SessionStatement statement,
                                List<IHookStatement> hooks) throws ExecException {
        bgStmtResult_ = null;
        SqlResultBuilder resultBuilder = new SqlResultBuilder();
        SqlResultBuilder originResultBuilder = new SqlResultBuilder();
        RowHandler handler = (String line) -> {
            originResultBuilder.append(line);
            String res = statement.applyResultHooks(line, hooks);
            if (res != null) {
                resultBuilder.append(res);
            }
        };
        execStatementInternal(statement, handler);
        bgStmtResult_ = new Pair<>(resultBuilder.toString(), originResultBuilder.toString());
    }

    private void execStatementInternal(@NotNull SessionStatement statement,
                                       RowHandler rowHandler) throws ExecException {
        try {
            Statement stmt = conn_.createStatement();
            boolean hasResult = stmt.execute(statement.getStatement());
            handleExecResult(hasResult, stmt, rowHandler);
            stmt.close();
        } catch (SQLException e) {
            rowHandler.handle(e.toString());
        }
    }

    private void handleExecResult(boolean hasResult, Statement stmt, RowHandler rowHandler) throws ExecException, SQLException {
        SQLWarning sqlWarning = conn_.getWarnings();
        boolean hasWarning = false;
        while (sqlWarning != null) {
            hasWarning = true;
            rowHandler.handle(sqlWarning.toString());
            sqlWarning = sqlWarning.getNextWarning();
        }
        if (hasWarning)
            rowHandler.handle("\n");

        if (hasResult) {
            ResultSet resultSet = stmt.getResultSet();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                StringBuilder rowBuilder = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1)
                        rowBuilder.append(",");
                    // Delegate column rendering to the connector so product-specific
                    // dialect quirks (e.g. timestamp precision) stay out of the core.
                    String colValue = connector_.renderColumn(resultSet, resultSetMetaData, i);
                    rowBuilder.append(colValue);
                }
                rowHandler.handle(rowBuilder.toString());
            }
            resultSet.close();
        } else {
            rowHandler.handle("UPDATE COUNT " + stmt.getUpdateCount());
        }
    }

    public Pair<String, String> getBgStmtResult() {
        return bgStmtResult_;
    }

    public interface RowHandler {
        void handle(String row) throws ExecException;

        static String newLine(String content) {
            return "\n" + content;
        }
    }

    static class SqlResultBuilder {
        private final List<String> lines = new ArrayList<>();

        public SqlResultBuilder append(String str) {
            lines.add(str);
            return this;
        }

        public int getLineCount() {
            return lines.size();
        }

        @Override
        public String toString() {
            return String.join("\n", lines);
        }

        public void sort() {
            lines.sort(String::compareTo);
        }
    }
}
