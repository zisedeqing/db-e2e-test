// Generated from Statements.g4 by ANTLR 4.9.2
package framework.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StatementsParser}.
 */
public interface StatementsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StatementsParser#script}.
	 * @param ctx the parse tree
	 */
	void enterScript(StatementsParser.ScriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#script}.
	 * @param ctx the parse tree
	 */
	void exitScript(StatementsParser.ScriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(StatementsParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(StatementsParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#test_annotation_stmt}.
	 * @param ctx the parse tree
	 */
	void enterTest_annotation_stmt(StatementsParser.Test_annotation_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#test_annotation_stmt}.
	 * @param ctx the parse tree
	 */
	void exitTest_annotation_stmt(StatementsParser.Test_annotation_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#env_var_stmt}.
	 * @param ctx the parse tree
	 */
	void enterEnv_var_stmt(StatementsParser.Env_var_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#env_var_stmt}.
	 * @param ctx the parse tree
	 */
	void exitEnv_var_stmt(StatementsParser.Env_var_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#env_var_value_stmt}.
	 * @param ctx the parse tree
	 */
	void enterEnv_var_value_stmt(StatementsParser.Env_var_value_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#env_var_value_stmt}.
	 * @param ctx the parse tree
	 */
	void exitEnv_var_value_stmt(StatementsParser.Env_var_value_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#env_var_shell_stmt}.
	 * @param ctx the parse tree
	 */
	void enterEnv_var_shell_stmt(StatementsParser.Env_var_shell_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#env_var_shell_stmt}.
	 * @param ctx the parse tree
	 */
	void exitEnv_var_shell_stmt(StatementsParser.Env_var_shell_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#session_sql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSession_sql_stmt(StatementsParser.Session_sql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#session_sql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSession_sql_stmt(StatementsParser.Session_sql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt(StatementsParser.Sql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt(StatementsParser.Sql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#sql_end}.
	 * @param ctx the parse tree
	 */
	void enterSql_end(StatementsParser.Sql_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#sql_end}.
	 * @param ctx the parse tree
	 */
	void exitSql_end(StatementsParser.Sql_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#empty_stmt}.
	 * @param ctx the parse tree
	 */
	void enterEmpty_stmt(StatementsParser.Empty_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#empty_stmt}.
	 * @param ctx the parse tree
	 */
	void exitEmpty_stmt(StatementsParser.Empty_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#shell_command}.
	 * @param ctx the parse tree
	 */
	void enterShell_command(StatementsParser.Shell_commandContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#shell_command}.
	 * @param ctx the parse tree
	 */
	void exitShell_command(StatementsParser.Shell_commandContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#blanks}.
	 * @param ctx the parse tree
	 */
	void enterBlanks(StatementsParser.BlanksContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#blanks}.
	 * @param ctx the parse tree
	 */
	void exitBlanks(StatementsParser.BlanksContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(StatementsParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(StatementsParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementsParser#single_comment}.
	 * @param ctx the parse tree
	 */
	void enterSingle_comment(StatementsParser.Single_commentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementsParser#single_comment}.
	 * @param ctx the parse tree
	 */
	void exitSingle_comment(StatementsParser.Single_commentContext ctx);
}