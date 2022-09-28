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

    override fun visitBinaryExpr(expr: Expression.Binary) = parenthesize(expr.operator.lexeme, expr.left, expr.right)

    override fun visitGroupingExpr(expr: Expression.Grouping) = parenthesize("group", expr.expr)

    override fun visitLiteralExpr(expr: Expression.Literal): String {
        val v = expr.value ?: return "nil"
        return v.toString()
    }

    override fun visitUnaryExpr(expr: Expression.Unary) = parenthesize(expr.operator.lexeme, expr.right)
}