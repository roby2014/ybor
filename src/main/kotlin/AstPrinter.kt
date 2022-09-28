/** Used to debug our AST (Abstract Syntax Tree). */
object AstPrinter : Expression.Visitor<String> {

    /** Prints the AST as debug message. */
    fun debug(expr: Expression) = println("AST: " + expr.accept(this))

    /** "Parenthesizes" [expressions] into a string and returns it. */
    private fun parenthesize(name: String?, vararg expressions: Expression): String {
        val builder = StringBuilder().append("(").append(name)
        for (expr in expressions) {
            builder.append(" ${expr.accept(this)}");
        }
        return builder.append(")").toString();
    }

    override fun visitBinaryExpression(expression: Expression.Binary) =
        parenthesize(expression.operator.lexeme, expression.left, expression.right)

    override fun visitGroupingExpression(expression: Expression.Grouping) = parenthesize("group", expression.expr)

    override fun visitLiteralExpression(expression: Expression.Literal): String {
        val v = expression.value ?: return "nil"
        return v.toString()
    }

    override fun visitUnaryExpression(expression: Expression.Unary) =
        parenthesize(expression.operator.lexeme, expression.right)
}