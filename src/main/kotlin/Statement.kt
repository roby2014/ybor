/** This file is autogenerated using [AstGenerator.defineAst]. */
abstract class Statement {

	abstract fun <T> accept(visitor: Visitor<T>): T

	interface Visitor<T> {
		fun visitExprStatement(statement: Expr): T
		fun visitPrintStatement(statement: Print): T
	}

	data class Expr(val expr: Expression): Statement() {
		override fun <T> accept(visitor: Visitor<T>) = visitor.visitExprStatement(this)
	}

	data class Print(val expr: Expression): Statement() {
		override fun <T> accept(visitor: Visitor<T>) = visitor.visitPrintStatement(this)
	}

}
