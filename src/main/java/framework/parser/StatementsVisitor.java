// Generated from Statements.g4 by ANTLR 4.9.2
package framework.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link StatementsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface StatementsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link StatementsParser#script}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScript(StatementsParser.ScriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(StatementsParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#test_annotation_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest_annotation_stmt(StatementsParser.Test_annotation_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#env_var_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnv_var_stmt(StatementsParser.Env_var_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#env_var_value_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnv_var_value_stmt(StatementsParser.Env_var_value_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#env_var_shell_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnv_var_shell_stmt(StatementsParser.Env_var_shell_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#session_sql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSession_sql_stmt(StatementsParser.Session_sql_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#sql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_stmt(StatementsParser.Sql_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#sql_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_end(StatementsParser.Sql_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#empty_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_stmt(StatementsParser.Empty_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#shell_command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShell_command(StatementsParser.Shell_commandContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#blanks}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlanks(StatementsParser.BlanksContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(StatementsParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementsParser#single_comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_comment(StatementsParser.Single_commentContext ctx);
}