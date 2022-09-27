import kotlin.test.*

/**
 * Implements tests for [Parser] class.
 * TODO: Implement more tests
 */
class ParserTest {

    @Test
    fun `test parser with simple math input by inserting tokens manually`() {
        //val tokens = Tokenizer("1+2").scanTokens()
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

    fun `test parser with simple math input by Tokenizer tokens`() {
        val tokens = Tokenizer("1+2").scanTokens()
        val ast = Parser(tokens).parse()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedRight = Expression.Literal(2.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }
}