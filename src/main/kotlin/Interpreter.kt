/** Interpreter singleton. Contains functions to interpret/run the code. */
object Interpreter : StatementVisitor<Unit>, ExpressionVisitor<Any?> {
    /** Environment to store variables. */
    private var env = Environment()

    /** "Interprets" various [statements]. */
    fun interpret(statements: List<Statement>) =
        try {
            statements.forEach(::execute)
        } catch (error: RuntimeError) {
            Ybor.runtimeError(error)
        }

    /** Executes [statement]. */
    fun execute(statement: Statement) = statement.accept(this)

    /** Evaluates [expr] and returns its value. */
    fun eval(expr: Expression): Any? = expr.accept(this)

    /**
     * Executes scope block, containing [statements], storing variables in a temporary environment.
     */
    private fun executeBlock(statements: List<Statement>, newEnv: Environment) {
        val prev = env
        try {
            env = newEnv // new env for this block
            statements.forEach(::execute)
        } catch (error: RuntimeError) {
            Ybor.runtimeError(error)
        } finally {
            env = prev
        }
    }

    /** "false and nil are false, and everything else is truthy." */
    private fun isTruthy(obj: Any?): Boolean =
        when (obj) {
            null -> false
            is Boolean -> obj
            else -> true
        }

    /**
     * Returns opposite number of [right]. In case [right] is not a number, throws [RuntimeError].
     */
    private fun getOppositeNumber(right: Any?, operator: Token) =
        when (right) {
            is Double -> -right
            else -> throw RuntimeError(operator, "Operand must be a number")
        }

    /** Helper function to do sums between Double or Strings (concatenate). */
    private fun sum(left: Any?, right: Any?, operator: Token): Any =
        when {
            left is Double && right is Double -> left + right
            left is String && right is String -> left + right
            else -> throw RuntimeError(operator, "Can't sum '$left' with '$right'")
        }

    /** Helper function to do subs between numbers or Strings. */
    private fun sub(left: Any?, right: Any?, operator: Token): Any =
        when {
            left is Double && right is Double -> left - right
            left is String && right is String -> left.replace(right, "")
            else -> throw RuntimeError(operator, "Can't sub '$left' with '$right'")
        }

    /** Helper function to multiply numbers. */
    private fun mult(left: Any?, right: Any?, operator: Token): Any =
        when {
            left is Double && right is Double -> left * right
            else -> throw RuntimeError(operator, "Can't multiply '$left' with '$right'")
        }

    /** Helper function to do numbers division. */
    private fun div(left: Any?, right: Any?, operator: Token): Any =
        when {
            left is Double && right is Double -> {
                if (right == 0.0) throw RuntimeError(operator, "Can't divide by 0")
                else left / right
            }
            else -> throw RuntimeError(operator, "Can't divide '$left' with '$right'")
        }

    /** Helper function to do compare numbers. */
    private fun cmp(left: Any?, right: Any?, cmpType: TokenType, operator: Token): Any =
        when {
            left is Double && right is Double ->
                when (cmpType) {
                    TokenType.GREATER -> left > right
                    TokenType.GREATER_EQUAL -> left >= right
                    TokenType.LESS -> left < right
                    TokenType.LESS_EQUAL -> left <= right
                    else -> {} // Unreachable!
                }
            else -> throw RuntimeError(operator, "Can't compare '$left' with '$right'")
        }

    /** Visit Expression. Returns the evaluated value. */
    override fun visit(expression: Expression): Any? =
        when (expression) {
            is Expression.Assign -> {
                val value = eval(expression.value)
                env.assignVar(expression.name, value)
                value
            }
            is Expression.Binary -> {
                val (left, right) = eval(expression.left) to eval(expression.right)
                when (val type = expression.operator.type) {
                    TokenType.PLUS -> sum(left, right, expression.operator)
                    TokenType.MINUS -> sub(left, right, expression.operator)
                    TokenType.SLASH -> div(left, right, expression.operator)
                    TokenType.STAR -> mult(left, right, expression.operator)
                    TokenType.EQUAL_EQUAL -> left == right
                    TokenType.BANG_EQUAL -> left != right
                    else -> cmp(left, right, type, expression.operator) // >, >=, <, <=
                }
            }
            is Expression.Grouping -> eval(expression.expr)
            is Expression.Literal -> expression.value
            is Expression.Unary -> {
                val right = eval(expression.right)
                when (expression.operator.type) {
                    TokenType.MINUS -> getOppositeNumber(right, expression.operator)
                    TokenType.BANG -> !isTruthy(right)
                    else -> null // Unreachable(!?)
                }
            }
            is Expression.Variable -> env.getVar(expression.name)
        }

    /** Visit Statement. Does not return anything because statements don't produce a value. */
    override fun visit(statement: Statement) {
        when (statement) {
            is Statement.Block -> executeBlock(statement.statements, Environment(env))
            is Statement.Expr -> println(eval(statement.expr)) // TODO: print only if we are on REPL
            is Statement.Print -> println(eval(statement.expr))
            is Statement.Variable -> {
                val value = statement.value?.let { eval(it) }
                env.createVar(statement.name, value)
            }
        }
    }
}
