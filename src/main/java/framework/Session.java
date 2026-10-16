package framework;

import framework.statement.IHookStatement;
import framework.statement.SessionStatement;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private static Logger LOGGER = LogManager.getLogger(Session.class);
    public static String mainThreadSessionId = "__MAIN-THREAD__";

    private String sessionId_;
    private int nodeId_;

    private ImpaladService impalad_;
    private Connection conn_;

    private ExecutorThread executorThread_ = null;

    // <final result, origin result>
    private Pair<String, String> bgStmtResult_ = null;
    private String connOptions = null;

    public Session(String sessionId, int nodeId, ImpaladService impalad, String connOptions) throws ExecException {
        this.sessionId_ = sessionId;
        this.impalad_ = impalad;
        this.connOptions = connOptions;
        this.nodeId_ = nodeId;
        conn_ = createConnection();
    }

    public int getNodeId() {
        return nodeId_;
    }

    private Connection createConnection() throws ExecException {
        String url = String.format("jdbc:mysql://%s:%d/%s?allowPublicKeyRetrieval=true&useLocalSessionState=TRUE%s", impalad_.getHost(),
            impalad_.getMysqlPort(), ConnectionOptionsManager.getGlobalOptions().getDbName(),
                connOptions != null && !connOptions.isEmpty() ? "&" + connOptions : "");
        LOGGER.trace("[" + sessionId_ + "] jdbc url: " + url);

        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOGGER.error("Get jdbc connection  " + url);
            throw new ExecException(e);
        }
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

    private String handleTimestampColumn(Timestamp ts) {
        String colValue = null;
        if (ts != null) {
            int nanos = ts.getNanos();
            if (nanos != 0) {
                colValue = ts.toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                // 提取纳秒，转为微秒（最大 6 位）
                int micros = ts.getNanos() / 1000;
                String fraction = String.format("%06d", micros); // 固定 6 位，不足补 0

                // 去掉末尾多余 0，保证最大 6 位
                fraction = fraction.replaceFirst("0+$", "");

                colValue += "." + fraction;
            } else {
                // 兼容原来resultSet.getString(i)的行为
                colValue = ts.toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        }
        return colValue;
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
                    int colType = resultSetMetaData.getColumnType(i);
                    String colValue = null;
                    if (colType == java.sql.Types.TIMESTAMP || colType == java.sql.Types.TIMESTAMP_WITH_TIMEZONE) {
                        // resultSet.getString(i) only print 4 digits of fractional seconds,
                        colValue = handleTimestampColumn(resultSet.getTimestamp(i));
                    } else {
                        colValue = resultSet.getString(i);
                    }
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
