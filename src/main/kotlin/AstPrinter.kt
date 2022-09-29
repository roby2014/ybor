/**
 * Used to debug our AST (Abstract Syntax Tree). At the moment it can only debug single line
 * expressions.
 */
object AstPrinter : ExpressionVisitor<String> {
    /** String builder to build the AST debug message. */
    private val sb = StringBuilder()

    init {
        // Since [AstPrinter] is singleton, in order to reset the previous usage
        // we clear the string builder
        sb.clear()
    }

    /** Returns the AST as debug message. */
    fun debug(expr: Expression) = expr.accept(this)

    /** Returns the expression string as debug format. */
    override fun visit(expression: Expression) =
        when (expression) {
            is Expression.Assign ->
                parenthesize("variable assign '${expression.name.lexeme}'", expression.value)
            is Expression.Binary ->
                parenthesize(expression.operator.lexeme, expression.left, expression.right)
            is Expression.Grouping -> parenthesize("group", expression.expr)
            is Expression.Literal -> expression.value?.toString() ?: "nil"
            is Expression.Unary -> parenthesize(expression.operator.lexeme, expression.right)
            is Expression.Variable -> parenthesize("variable access '${expression.name.lexeme}'")
        }

    /** "Parenthesizes" aKa builds a debug message with all the [expressions]. */
    private fun parenthesize(prefix: String?, vararg expressions: Expression): String {
        val builder = StringBuilder().append("(").append(prefix)
        for (expr in expressions) {
            builder.append(" ${expr.accept(this)}")
        }
        return builder.append(")").toString()
    }
}
