/**
 * Interpreter singleton.
 * Contains functions to interpret/run the code.
 */
object Interpreter : Expression.Visitor<Any?> {
    /** "Interprets" [expression], returning its evaluated value or null if any runtime error occurred. */
    fun interpret(expression: Expression) = try {
        eval(expression)
    } catch (error: RuntimeError) {
        Ybor.runtimeError(error)
        null
    }

    /** Evaluates [expr] and returns its value. */
    private fun eval(expr: Expression): Any? = expr.accept(this)

    /** Binary expressions' evaluation. */
    override fun visitBinaryExpr(expr: Expression.Binary): Any {
        val (left, right) = eval(expr.left) to eval(expr.right)
        return when (val type = expr.operator.type) {
            TokenType.PLUS -> sum(left, right, expr.operator)
            TokenType.MINUS -> sub(left, right, expr.operator)
            TokenType.SLASH -> div(left, right, expr.operator)
            TokenType.STAR -> mult(left, right, expr.operator)
            TokenType.EQUAL_EQUAL -> left == right
            TokenType.BANG_EQUAL -> left != right
            else -> cmp(left, right, type, expr.operator) // GREATER, GREATER_EQUAL, LESS, LESS_EQUAL
        }
    }

    /** Grouping expressions' evaluation. */
    override fun visitGroupingExpr(expr: Expression.Grouping): Any? = eval(expr.expr)

    /** Literal expressions' evaluation. */
    override fun visitLiteralExpr(expr: Expression.Literal): Any? = expr.value

    /** Unary expressions' evaluation. */
    override fun visitUnaryExpr(expr: Expression.Unary): Any? {
        val right = eval(expr.right)
        return when (expr.operator.type) {
            TokenType.MINUS -> getOppositeNumber(right, expr.operator)
            TokenType.BANG -> !isTruthy(right)
            else -> null // Unreachable(!?)
        }
    }

    /** "false and nil are false, and everything else is truthy." */
    private fun isTruthy(obj: Any?): Boolean = when (obj) {
        null -> false
        is Boolean -> obj
        else -> true
    }

    /** Returns opposite number of [right]. In case [right] is not a number, throws [RuntimeError]. */
    private fun getOppositeNumber(right: Any?, operator: Token) = when (right) {
        is Double -> -right
        else -> throw RuntimeError(operator, "Operand must be a number")
    }

    /** Helper function to do sums between Double or Strings (concatenate). */
    private fun <T> sum(left: T, right: T, operator: Token): Any = when {
        left is Double && right is Double -> left + right
        left is String && right is String -> left + right
        else -> throw RuntimeError(operator, "Can't sum '$left' with '$right'")
    }

    /** Helper function to do subs between numbers or Strings. */
    private fun <T> sub(left: T, right: T, operator: Token): Any = when {
        left is Double && right is Double -> left - right
        left is String && right is String -> left.replace(right, "")
        else -> throw RuntimeError(operator, "Can't sub '$left' with '$right'")
    }

    /** Helper function to multiply numbers. */
    private fun <T> mult(left: T, right: T, operator: Token): Any = when {
        left is Double && right is Double -> left * right
        else -> throw RuntimeError(operator, "Can't multiply '$left' with '$right'")
    }

    /** Helper function to do numbers division. */
    private fun <T> div(left: T, right: T, operator: Token): Any = when {
        left is Double && right is Double -> left / right
        else -> throw RuntimeError(operator, "Can't divide '$left' with '$right'")
    }

    /** Helper function to do compare numbers. */
    private fun <T> cmp(left: T, right: T, cmpType: TokenType, operator: Token): Any = when {
        left is Double && right is Double -> when (cmpType) {
            TokenType.GREATER -> left > right
            TokenType.GREATER_EQUAL -> left >= right
            TokenType.LESS -> left < right
            TokenType.LESS_EQUAL -> left <= right
            else -> {} // Unreachable!
        }
        else -> throw RuntimeError(operator, "Can't compare '$left' with '$right'")
    }
}