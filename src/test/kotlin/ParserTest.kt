import kotlin.test.*

/**
 * Implements tests for [Parser] class.
 * TODO: Implement more tests
 */
class ParserTest {

    @Test
    fun `test parser with simple math input by inserting tokens manually`() {
        // val tokens = Tokenizer("1+2").scanTokens()
        // im not trusting Tokenizer class here :]
        val tokens = listOf(
            Token(TokenType.NUMBER, "1", 1.0, 1),
            Token(TokenType.PLUS, "+", null, 1),
            Token(TokenType.NUMBER, "2", 2.0, 1),
            Token(TokenType.EOF, "EOF", null, 1)
        )
        val ast = Parser(tokens).parse()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedRight = Expression.Literal(2.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }

    @Test
    fun `test parser with simple math expression`() {
        val tokens = Tokenizer("1+2").scanTokens()
        val ast = Parser(tokens).parse()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedRight = Expression.Literal(2.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }

    @Test
    fun `test parser with simple math expression to test multiplication precedence`() {
        val tokens = Tokenizer("1+2*3").scanTokens()
        val ast = Parser(tokens).parse()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedOperator2 = Token(TokenType.STAR, "*", null, 1)
        val expectedRight = Expression.Binary(Expression.Literal(2.0), expectedOperator2, Expression.Literal(3.0))

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }

    @Test
    fun `test parser with simple math expression to test parentheses precedence`() {
        val tokens = Tokenizer("(1+2)*3").scanTokens()
        val ast = Parser(tokens).parse()

        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedOperator2 = Token(TokenType.STAR, "*", null, 1)
        val expectedLeft = Expression.Grouping(Expression.Binary(Expression.Literal(1.0), expectedOperator, Expression.Literal(2.0)))
        val expectedRight = Expression.Literal(3.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator2, expectedRight))
    }

    @Test
    fun `test parser with invalid expression input`() {
        val tokens = Tokenizer("abc").scanTokens()
        val ast = Parser(tokens).parse()
        assertEquals(ast, null)
    }

    @Test
    fun `test parser with string input`() {
        val tokens = Tokenizer("\"abc\"").scanTokens()
        val ast = Parser(tokens).parse()
        assertEquals(ast, Expression.Literal("abc"))
    }
}