package framework;

import com.github.difflib.algorithm.DiffException;
import framework.parser.ParserUtils;
import framework.statement.CombineHookStatement;
import framework.statement.EmptyStatement;
import framework.statement.IHookStatement;
import framework.statement.INormalStatement;
import framework.statement.IStatement;
import framework.statement.TestAnnotationStmt;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

// Not thread-safe
public class TestScript {
    private static Logger LOGGER = LogManager.getLogger(TestScript.class);
    private SessionManager sessionManager_ = new SessionManager();

    private HookManager hookManager_ = new HookManager();

    private Map<String, String> envs_;

    private ResultWriter finalWriter_;
    private ResultWriter originWriter_;

    private AtomicLong finishedStatement = new AtomicLong(0);
    private ExecContext execContext_;

    public TestScript() {
    }

    public void registerImpaladService(List<ImpaladService> impalads) {
        sessionManager_.setImpalads(impalads);
    }

    public void setEnvs(Map<String, String> envs) {
        envs_ = envs;
    }

    public void setEnv(String envName, String envValue) {
        if (envs_ == null) {
            envs_ = new HashMap<>();
        }
        envs_.put(envName, envValue);
    }

    public void addGlobalHooks(String hookFile) throws ExecException {
        hookManager_.addGlobalHooks(hookFile);
    }

    public TestCaseStatistic executeScript(String inputFile, String finalOutputFile, String originOutputFile) throws ExecException {
        if (sessionManager_.getImpalads().isEmpty()) {
            throw new ExecException("No impalads register, call registerImpaladService() to register impalad");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExecException(e);
        }

        execContext_ = new ExecContext(sessionManager_, envs_, hookManager_);
        ParserUtils.ParserResult result = ParserUtils.parserScript(
                execContext_, inputFile, false);
        List<IStatement> statements = result.statements_;

        finalWriter_ = new ResultWriter(finalOutputFile);
        if (result.hasHookStatement_)
            originWriter_ = new ResultWriter(originOutputFile);
        Pair pairWriter = new Pair<>(finalWriter_, originWriter_);

        try {
            int startIdx = handleFileHooks(statements, pairWriter);
            checkTestAnnotationStatement(statements, startIdx);
            for (int i = startIdx; i < statements.size(); i++) {
                IStatement stmt = statements.get(i);
                stmt.setWriter(pairWriter);
                if (stmt instanceof IHookStatement) {
                    handleRuntimeHook((IHookStatement) stmt);
                    continue;
                } else if (stmt instanceof TestAnnotationStmt) {
                    stmt.execute();
                    continue;
                }
                handleNormalStatement((INormalStatement) stmt);
            }

            return execContext_.getStatistic();
        } finally {
            clear();
        }
    }

    private int handleFileHooks(List<IStatement> statements, Pair pairWriter) throws ExecException {
        int i = 0;
        for (; i < statements.size(); i++) {
            IStatement stmt = statements.get(i);
            stmt.setWriter(pairWriter);
            if (stmt.isNotEmptyNormalStatement() || stmt instanceof TestAnnotationStmt) break;

            LOGGER.debug(stmt.toString());
            stmt.execute();
            if (stmt instanceof IHookStatement && ((IHookStatement) stmt).isAnonymous())
                hookManager_.pushHook((IHookStatement) stmt);
        }
        return i;
    }

    private void checkTestAnnotationStatement(List<IStatement> statements, int startIdx) throws ExecException {
        boolean foundAfterAnnotation = false;
        for (int i = startIdx + 1; i < statements.size(); i++) {
            IStatement stmt = statements.get(i);
            if (stmt instanceof TestAnnotationStmt) {
                if (((TestAnnotationStmt) stmt).getAnnotationType() == TestAnnotationStmt.AnnotationType.AT_BEFORE)
                    throw new ExecException("Before annotation must be the first statement");
                if (((TestAnnotationStmt) stmt).getAnnotationType() == TestAnnotationStmt.AnnotationType.AT_AFTER)
                    foundAfterAnnotation = true;
                if (foundAfterAnnotation && ((TestAnnotationStmt) stmt).getAnnotationType() == TestAnnotationStmt.AnnotationType.AT_CASE)
                    throw new ExecException("After annotation must be the last annotation statement");
            }
        }
    }

    private void handleRuntimeHook(IHookStatement hookStatement) throws ExecException {
        LOGGER.debug(hookStatement.toString());
        hookStatement.execute();
        if (hookStatement instanceof CombineHookStatement) {
            if (((CombineHookStatement) hookStatement).isEnd()) {
                IHookStatement blockHook = hookManager_.popBlockHook();
                if (blockHook == null) {
                    LOGGER.error("Missing block combine hook");
                }
                return;
            }
        }
        hookManager_.pushHook(hookStatement);
    }

    private void handleNormalStatement(INormalStatement statement) throws ExecException {
        LOGGER.debug(statement.toString());

        statement.startExecute();
        statement.execute();
        statement.endExecute();
        if (!(statement instanceof EmptyStatement)) {
            execContext_.getStatistic().increFinishedStatementCount();
        }
    }

    public boolean diffResult(String expectFile, String targetFile,
            String diffFile) throws DiffException, ExecException {
        FileDiffer differ = new FileDiffer(expectFile, targetFile);
        return differ.diff(diffFile);
    }

    private void clear() throws ExecException {
        hookManager_.clearFileHooks();
        sessionManager_.close();
        finalWriter_.close();
        if (originWriter_ != null) {
            originWriter_.close();
            originWriter_ = null;
        }
        execContext_ = null;
    }

    public int getProgress() {
        if (execContext_.getStatistic().getTotalStatementCount() == 0) return 0;
        return (int) (execContext_.getStatistic().getTotalFinishedStatementCount() * 100 /
                execContext_.getStatistic().getTotalStatementCount());
    }
}
