package framework;

import framework.parser.StatementsBaseVisitor;
import framework.parser.StatementsParser;
import framework.statement.CombineHookStatement;
import framework.statement.DeleteHookStatement;
import framework.statement.EmptyStatement;
import framework.statement.EnvVarStatement;
import framework.statement.EvalEnvHookStatement;
import framework.statement.ExtractHookStatement;
import framework.statement.IHookStatement;
import framework.statement.IStatement;
import framework.statement.IgnoreHookStatement;
import framework.statement.ReplaceHookStatement;
import framework.statement.SessionStatement;
import framework.statement.ShellStatement;
import framework.statement.SortResultHookStatement;
import framework.statement.StatementType;
import framework.statement.TestAnnotationStmt;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ScriptVisitor extends StatementsBaseVisitor<Void> {
    private List<IStatement> statements_ = new ArrayList<>();
    private Stack<CombineHookStatement> combineHookStack_ = new Stack<>();

    private boolean hasHookStatement_ = false;
    private boolean isFileHook = true;

    private ExecContext execContext_;
    private boolean parserHookFile_;

    private boolean hasBeforeTestAnnotation_ = false;
    private boolean hasAfterTestAnnotation_ = false;
    private boolean hasTestCaseAnnotation_ = false;

    public ScriptVisitor(ExecContext execContext, boolean parserHookFile) {
        execContext_ = execContext;
        parserHookFile_= parserHookFile;
    }

    public List<IStatement> getStatements() {
        checkReplaceHook();
        return statements_;
    }

    public boolean hasHookStatement() {
        return hasHookStatement_;
    }

    public void addStatement(IStatement statement) {
        statements_.add(statement);
        if (!(statement instanceof IHookStatement ||
            statement instanceof EmptyStatement ||
            statement instanceof TestAnnotationStmt))
            execContext_.getStatistic().increTotalStatementCount();

        if (!hasHookStatement_)
            hasHookStatement_ = statement instanceof IHookStatement;
        if (parserHookFile_ && statement.isNotEmptyNormalStatement())
            throw new RuntimeException(String.format(
                    "[Line: %d] - Statement is not hook statement in global hook file",
                    statement.getLineNumber()));
        if (isFileHook && (statement.isNotEmptyNormalStatement() ||
                statement instanceof IgnoreHookStatement))
            isFileHook = false;
        if (!isFileHook && statement instanceof IHookStatement &&
                !((IHookStatement) statement).isAnonymous())
            throw new RuntimeException(String.format(
                    "[Line: %d] - Named hook is not allowed here",
                    statement.getLineNumber()));
    }

    private void addHookStatement(IHookStatement hookStatement) {
        if (parserHookFile_) {
            execContext_.getHookManager().addHook(hookStatement, true);
        } else if (isFileHook) {
            if (!hookStatement.isAnonymous())
                execContext_.getHookManager().addHook(hookStatement, false);
            else
                hookStatement.setHookScope(IHookStatement.HookScope.HS_FILE);
        }
    }

    private void checkReplaceHook() {
        for (IStatement statement : statements_) {
            if (!(statement instanceof ReplaceHookStatement)) continue;
            if (((ReplaceHookStatement) statement).getReplaceString() == null)
                throw new RuntimeException(String.format(
                    "[Line: %d] - Missing \"%s\" statment for \"%s\"",
                    statement.getLineNumber(), ReplaceHookStatement.REPLACT_IDENTIFIER,
                    ((ReplaceHookStatement) statement).getMatchRegexp()));
        }
    }

    public IStatement getTailStatement() {
        if (statements_.isEmpty()) return null;
        return statements_.get(statements_.size() - 1);
    }

    public void checkAnnotation() {
        if (parserHookFile_) return;
        if (hasBeforeTestAnnotation_) {
            if (!hasAfterTestAnnotation_)
                throw new RuntimeException("Missing after test annotation");
            if (!hasTestCaseAnnotation_)
                throw new RuntimeException("Missing test case annotation");
        }

        // Compatible with old test file which has not any test annotation
        if (!hasBeforeTestAnnotation_ && !hasTestCaseAnnotation_
            && !hasTestCaseAnnotation_)
            execContext_.getStatistic().increTotalCaseCount();
    }

    public boolean hasAnnotation() {
        return hasBeforeTestAnnotation_ || hasAfterTestAnnotation_ || hasTestCaseAnnotation_;
    }

    public boolean hasAfterTestAnnotation() {
        return hasAfterTestAnnotation_;
    }

    @Override
    public Void visitScript(StatementsParser.ScriptContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitTest_annotation_stmt(StatementsParser.Test_annotation_stmtContext ctx) {
        TestAnnotationStmt stmt = new TestAnnotationStmt(execContext_, ctx.start.getLine(),
                ctx.getText().substring(1));
        addStatement(stmt);
        if (stmt.getAnnotationType() == TestAnnotationStmt.AnnotationType.AT_CASE) {
            hasTestCaseAnnotation_ = true;
            execContext_.getStatistic().increTotalCaseCount();
        }
        else if (stmt.getAnnotationType() == TestAnnotationStmt.AnnotationType.AT_BEFORE) {
            if (hasBeforeTestAnnotation_) {
                throw new RuntimeException(String.format("[Line: %d] - Has defined before test annotation",
                        ctx.start.getLine()));
            }
            hasBeforeTestAnnotation_ = true;
        } else {
            if (hasAfterTestAnnotation_) {
                throw new RuntimeException(String.format("[Line: %d] - Has defined after test annotation",
                        ctx.start.getLine()));
            }
            hasAfterTestAnnotation_ = true;
        }
        return null;
    }

    @Override
    public Void visitStatement(StatementsParser.StatementContext ctx) {
        visitChildren(ctx);
        IStatement stmt = getTailStatement();
        if (stmt != null)
            stmt.setLineNumber(ctx.start.getLine());
        return null;
    }

    @Override
    public Void visitSingle_comment(StatementsParser.Single_commentContext ctx) {
        String comment = getRuleContextText(ctx);
        IStatement prevStmt = getTailStatement();
        if (ReplaceHookStatement.isMatchStatement(comment)) {
            if (prevStmt != null && prevStmt instanceof ReplaceHookStatement &&
                ((ReplaceHookStatement) prevStmt).getReplaceString() == null) {
                throw new RuntimeException(String.format(
                        "[Line: %d] - Missing \"%s\" statment for \"%s\"",
                        ctx.start.getLine(), ReplaceHookStatement.REPLACT_IDENTIFIER,
                        ((ReplaceHookStatement) prevStmt).getMatchRegexp()));
            }
            addStatement(new ReplaceHookStatement(ctx.start.getLine()).parserHook(comment));
        } else if (ReplaceHookStatement.isReplaceStatement(comment)) {
            if (prevStmt == null ||
                !(prevStmt instanceof ReplaceHookStatement) ||
                (prevStmt instanceof ReplaceHookStatement &&
                ((ReplaceHookStatement) prevStmt).getMatchRegexp() == null)) {
                throw new RuntimeException(String.format(
                    "[Line: %d] - Missing \"%s\" statment for \"%s\"",
                        prevStmt == null || !(prevStmt instanceof ReplaceHookStatement) ?
                        ctx.start.getLine() : prevStmt.getLineNumber(),
                    ReplaceHookStatement.MATCH_IDENTIFIER, comment));
            }
            ((ReplaceHookStatement) prevStmt).parserHook(comment);
            addHookStatement((ReplaceHookStatement) prevStmt);
        } else if (IgnoreHookStatement.isIgnoreStatement(comment)) {
            addStatement(new IgnoreHookStatement(ctx.start.getLine()));
        } else if (SortResultHookStatement.isSortResultStatement(comment)) {
            addStatement(new SortResultHookStatement(ctx.start.getLine()));
        } else if (CombineHookStatement.isCombineHook(comment)) {
            CombineHookStatement combineHook = new CombineHookStatement(execContext_, ctx.start.getLine());
            combineHook.parserHook(comment);
            if (combineHook.isEnd()) {
                CombineHookStatement startCombineHook = combineHookStack_.pop();
                if (startCombineHook == null) {
                    throw new RuntimeException(String.format(
                        "[Line: %d] - Missing combine hook start statement",
                        ctx.start.getLine()));
                }
                startCombineHook.setHookScope(IHookStatement.HookScope.HS_BLOCK);
            } else {
                combineHookStack_.push(combineHook);
            }
            addStatement(combineHook);
            if (!combineHook.isEnd())
                addHookStatement(combineHook);
        } else if (DeleteHookStatement.isDeleteHookStatement(comment)) {
            DeleteHookStatement deleteHook = new DeleteHookStatement(ctx.start.getLine());
            deleteHook.parserHook(comment);
            addStatement(deleteHook);
            addHookStatement(deleteHook);
        } else if (EvalEnvHookStatement.isEvalEnvHookStatement(comment)) {
            EvalEnvHookStatement evalHool = new EvalEnvHookStatement(execContext_, ctx.start.getLine());
            evalHool.parserHook(comment);
            addStatement(evalHool);
            addHookStatement(evalHool);
        } else if (ExtractHookStatement.isExtractHookStatement(comment)) {
            ExtractHookStatement extractHook = new ExtractHookStatement(ctx.start.getLine());
            extractHook.parserHook(comment);
            addStatement(extractHook);
            addHookStatement(extractHook);
        } else if (ConnectionOptionsManager.isConnectionOptions(comment)) {
            ConnectionOptionsManager.parserConnectionOptions(comment);
        }

        // ignore normal single comments
        return null;
    }

    @Override
    public Void visitSession_sql_stmt(StatementsParser.Session_sql_stmtContext ctx) {
        SessionStatement statement;
        if (ctx.sql_stmt() != null) {
            visitChildren(ctx);
            statement = (SessionStatement) getTailStatement();
        } else {
            statement = new SessionStatement(execContext_, ctx.start.getLine());
            addStatement(statement);
        }

        Pair<String, Integer> pair = getSessionIdAndNodeId(ctx.ident().SESSION_ID());
        statement.setSessionId(pair.a);
        statement.setNodeId(pair.b);
        statement.setLineNumber(ctx.start.getLine());
        if (ctx.JOIN() != null) {
            statement.setStatementFlag(StatementType.JOIN);
        } else if (ctx.BACKGROUND() != null) {
            statement.setStatementFlag(StatementType.BACKGROUND);
        } else if (ctx.QUIT() != null) {
            statement.setStatementFlag(StatementType.QUIT);
        }
        return null;
    }

    @Override
    public Void visitSql_stmt(StatementsParser.Sql_stmtContext ctx) {
        addStatement(new SessionStatement(getSqlString(ctx.SQL_STRING()),
            ctx.start.getLine(), execContext_,
            ConnectionOptionsManager.peekConnectionOptions()));
        return null;
    }

    @Override
    public Void visitEmpty_stmt(StatementsParser.Empty_stmtContext ctx) {
        addStatement(new EmptyStatement(ctx.start.getLine()));
        return null;
    }

    @Override
    public Void visitShell_command(StatementsParser.Shell_commandContext ctx) {
        String command = getRuleContextText(ctx).trim();
        command = command.substring(1, command.length() - 1).trim();
        addStatement(new ShellStatement(execContext_, ctx.start.getLine(), command));
        return null;
    }

    @Override
    public Void visitEnv_var_value_stmt(StatementsParser.Env_var_value_stmtContext ctx) {
        String stmt = getRuleContextText(ctx).trim();
        String varName = ctx.ENV_VARIABLE_NAME().getText().substring(1);
        addStatement(new EnvVarStatement(execContext_, ctx.start.getLine(),
                varName, getSqlString(ctx.SQL_STRING()), stmt, false));
        return null;
    }

    @Override
    public Void visitEnv_var_shell_stmt(StatementsParser.Env_var_shell_stmtContext ctx) {
        String stmt = getRuleContextText(ctx).trim();
        String varName = ctx.ENV_VARIABLE_NAME().getText().substring(1);
        String temp = ctx.ENV_SHELL_EXPR().getText();
        // '$' '(' shell_expr ')'
        String shellExpr =temp.substring(2, temp.length() - 1).trim();
        addStatement(new EnvVarStatement(execContext_, ctx.start.getLine(),
                varName, shellExpr, stmt, true));
        return null;
    }

    public String getRuleContextText(ParserRuleContext ctx) {
        return ctx.start.getInputStream().getText(
                new org.antlr.v4.runtime.misc.Interval(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    }

    private String getSqlString(TerminalNode sql_string) {
        return sql_string.getText();
    }

    private Pair<String, Integer> getSessionIdAndNodeId(TerminalNode node) {
        if (node.getText().length() == 2)
            return null;
        String tmp = node.getText().substring(1, node.getText().length() - 1);
        String[] split = tmp.split(":");
        if (split.length == 1)
            return new Pair<>(split[0], null);
        return new Pair<>(split[0], Integer.parseInt(split[1]));
    }
}
