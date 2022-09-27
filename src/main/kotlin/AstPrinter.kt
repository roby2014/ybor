/**
 * AST class. Used to debug our AST.
 */
object AstPrinter : Expression.Visitor<String> {
    /** Prints the AST as debug message. */
    fun debug(expr: Expression) = println(expr.accept(this))

    override fun visitBinaryExpr(expr: Expression.Binary): String {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visitGroupingExpr(expr: Expression.Grouping): String {
        return parenthesize("group", expr.expr)
    }

    override fun visitLiteralExpr(expr: Expression.Literal): String {
        val v = expr.value ?: return "nil"
        return v.toString()
    }

    override fun visitUnaryExpr(expr: Expression.Unary): String {
        return parenthesize(expr.operator.lexeme, expr.right)
    }

    private fun parenthesize(name: String?, vararg expressions: Expression): String {
        val builder = StringBuilder().append("(").append(name)
        for (expr in expressions) {
            builder.append(" ${expr.accept(this)}");
        }
        return builder.append(")").toString();
    }
}