/**
 * Interpreter singleton.
 * Contains functions to interpret/run the code.
 */
object Interpreter : Expression.Visitor<Any?> {

    /** "Interprets" [expression], returning its evaluated value or null if any runtime error occurred. */
    fun interpret(expression: Expression): Any? {
        return try {
            eval(expression)
        } catch (error: RuntimeError) {
            Ybor.runtimeError(error)
            null
        }
    }

    /** Evaluates [expr] and returns its value. */
    private fun eval(expr: Expression) = expr.accept(this)

    /** Binary expressions' evaluation. */
    override fun visitBinaryExpr(expr: Expression.Binary): Any? {
        val (left, right) = eval(expr.left) to eval(expr.right)
        return when (expr.operator.type) {
            TokenType.PLUS -> sum(left, right) ?: throw RuntimeError(expr.operator, "Can't sum '$left' with '$right'")
            TokenType.MINUS -> sub(left, right) ?: throw RuntimeError(expr.operator, "Can't sub '$left' with '$right'")
            TokenType.SLASH -> when {
                left is Double && right is Double -> left / right
                else -> throw RuntimeError(expr.operator, "Can't divide '$left' with '$right'")
            }
            TokenType.STAR -> when {
                left is Double && right is Double -> left * right
                else -> throw RuntimeError(expr.operator, "Can't multiply '$left' with '$right'")
            }
            else -> null
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
            TokenType.MINUS -> getOppositeNumber(right) ?: throw RuntimeError(expr.operator, "Operand must be a number")
            TokenType.BANG -> !isTruthy(right)
            else -> null // Unreachable
        }
    }

    /** "false and nil are false, and everything else is truthy." */
    private fun isTruthy(obj: Any?): Boolean = when (obj) {
        null -> false
        is Boolean -> obj
        else -> true
    }

    /** Returns opposite number of [right]. In case [right] is not a number, returns null. */
    private fun getOppositeNumber(right: Any?) = when (right) {
        is Double -> -right
        else -> null
    }

    /** Helper function to do sums between Double or Strings (concatenate). A bit redundant because Kotlin's smart casting :/ */
    private fun <T> sum(left: T, right: T): Any? = when {
        left is Double && right is Double -> left + right
        left is String && right is String -> left + right
        else -> null
    }

    /** Helper function to do subs between Double or Strings. A bit redundant because Kotlin's smart casting :/ */
    private fun <T> sub(left: T, right: T): Any? = when {
        left is Double && right is Double -> left - right
        left is String && right is String -> left.replace(right, "")
        else -> null
    }
}