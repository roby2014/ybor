/**
 * Parser class.
 * @param tokens Token list to parse.
 */
class Parser(private val tokens: List<Token>) {
    /** current token index */
    private var current = 0

    fun parse(): Expression? {
        return try {
            expression()
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    private fun expression() = equality()

    private fun equality(): Expression {
        var expr = comparison()
        while (match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun comparison(): Expression {
        var expr = term()
        while (match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            val operator = previous()
            val right = term()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun term(): Expression {
        var expr = factor()
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun factor(): Expression {
        var expr = unary()
        while (match(TokenType.STAR, TokenType.SLASH)) {
            val operator = previous()
            val right = unary()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expression {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expression.Unary(operator, right)
        }
        return primary()
    }

    private fun primary(): Expression {
        return when {
            match(TokenType.FALSE) -> Expression.Literal(false)
            match(TokenType.TRUE) -> Expression.Literal(true)
            match(TokenType.NIL) -> Expression.Literal(null)
            match(TokenType.LEFT_PAREN) -> {
                val expr = expression()
                consume(TokenType.RIGHT_PAREN, "Expected ')'")
                Expression.Grouping(expr)
            }
            match(TokenType.NUMBER, TokenType.STRING) -> Expression.Literal(previous().literal)
            else -> throw Exception("Expected expression")
        }
    }

    private fun match(vararg expected: TokenType): Boolean {
        for (type in expected) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(expected: TokenType): Boolean = when {
        end() || peek().type != expected -> false
        else -> true
    }

    private fun previous() = tokens[current - 1]

    private fun peek() = tokens[current]

    private fun end() = peek().type == TokenType.EOF

    private fun advance(): Token {
        if (!end()) current++
        return previous()
    }

    private fun consume(type: TokenType, errMsg: String): Token {
        if (check(type)) return advance()
        throw Exception(errMsg)
    }
}