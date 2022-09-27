/**
 * Parser class.
 * @param tokens Token list to parse.
 */
class Parser(private val tokens: List<Token>) {
    private class ParserError : Exception()

    /** current token index */
    private var current = 0

    /** Parses tokens, returning the AST if success, null otherwise. */
    fun parse() = try {
        expression()
    } catch (e: ParserError) {
        //synchronize()
        null
    }

    /**
     * Helper method to parse left associative binary expressions, such as:
     * Equality, comparison, term, factor, ...
     * This reduces redundant code.
     * @param tokenTypes Token types to match
     * @param method Higher precedence parse method
     */
    private fun parseLeftAssociative(tokenTypes: List<TokenType>, method: () -> Expression): Expression {
        var expr = method()
        while (match(tokenTypes)) {
            val operator = previous()
            val right = method()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    /** This is where the top-down parser starts.
     * "Recursive descent is considered a top-down parser because it starts from the
     * top or outermost grammar rule (here expression) and works its way down
     * into the nested subexpressions before finally reaching the leaves of the syntax tree."
     */
    private fun expression() = equality()

    private fun equality() = parseLeftAssociative(listOf(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL), ::comparison)

    private fun comparison() = parseLeftAssociative(
        listOf(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL), ::term
    )

    private fun term() = parseLeftAssociative(listOf(TokenType.MINUS, TokenType.PLUS), ::factor)

    private fun factor() = parseLeftAssociative(listOf(TokenType.STAR, TokenType.SLASH), ::unary)

    private fun unary(): Expression {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expression.Unary(operator, right)
        }
        return primary()
    }

    private fun primary() = when {
        match(TokenType.FALSE) -> Expression.Literal(false)
        match(TokenType.TRUE) -> Expression.Literal(true)
        match(TokenType.NIL) -> Expression.Literal(null)
        match(TokenType.LEFT_PAREN) -> {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expected ')'")
            Expression.Grouping(expr)
        }

        match(TokenType.NUMBER, TokenType.STRING) -> Expression.Literal(previous().literal)
        else -> throw parserError(peek(), "Expected valid expression")
    }

    /** Returns true if one of [expected] tokens is found, advancing one. */
    private fun match(vararg expected: TokenType): Boolean {
        for (type in expected) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    /** Returns true if one of [expected] tokens is found, advancing one. */
    private fun match(expected: List<TokenType>): Boolean {
        for (type in expected) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    /** Returns true if current token type is [expected]. */
    private fun check(expected: TokenType): Boolean = when {
        end() || peek().type != expected -> false
        else -> true
    }

    /** Returns previous token. */
    private fun previous() = tokens[current - 1]

    /** Returns current token. */
    private fun peek() = tokens[current]

    /** Returns true if at end of list. */
    private fun end() = peek().type == TokenType.EOF

    /** Advances token, returning it. */
    private fun advance(): Token {
        if (!end()) current++
        return previous()
    }

    /**
     * Consumes token by its type ([type]), advancing one.
     * In case token did not match, throws error with message [errMsg].
     */
    private fun consume(type: TokenType, errMsg: String): Token {
        if (check(type)) return advance()
        throw parserError(peek(), errMsg)
    }

    /** Report parser error. */
    private fun parserError(token: Token, message: String): ParserError {
        Ybor.error(token, message)
        return ParserError()
    }

    /**
     * Synchronize parser after an error occurred.
     * "Before it can get back to parsing, it needs to get its state and the sequence of
     * forthcoming tokens aligned such that the next token does match the rule being
     * parsed. This process is called synchronization."
     */
    private fun synchronize() {
        advance()
        while (!end()) {
            if (previous().type == TokenType.SEMICOLON) return
            advance()
        }
    }
}