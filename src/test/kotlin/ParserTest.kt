import kotlin.test.Test
import kotlin.test.assertEquals

/** Implements tests for [Parser] class. TODO: Implement more tests */
class ParserTest {

    /// Statement tests

    @Test
    fun `test variable declaration statement inside block`() {
        val tokens = Tokenizer("{ var a = 3; }").scanTokens()
        val statements = Parser(tokens).parseStatements()
        val expectedStatements =
            listOf(
                Statement.Variable(
                    Token(TokenType.IDENTIFIER, "a", null, 1),
                    Expression.Literal(3.0)
                )
            )
        assert(statements[0] == Statement.Block(expectedStatements))
    }

    @Test
    fun `test expression statement`() {
        val tokens = Tokenizer("1+2;").scanTokens()
        val statements = Parser(tokens).parseStatements()
        val expectedExpression =
            Expression.Binary(
                Expression.Literal(1.0),
                Token(TokenType.PLUS, "+", null, 1),
                Expression.Literal(2.0)
            )
        assert(statements[0] == Statement.Expr(expectedExpression))
    }

    @Test
    fun `test variable declaration with math and string`() {
        val tokens = Tokenizer("var a = 1+2; var b = \"str\";").scanTokens()
        val statements = Parser(tokens).parseStatements()

        val expectedName1 = Token(TokenType.IDENTIFIER, "a", null, 1)
        val expectedValue1 =
            Expression.Binary(
                Expression.Literal(1.0),
                Token(TokenType.PLUS, "+", null, 1),
                Expression.Literal(2.0)
            )
        assert(statements[0] == Statement.Variable(expectedName1, expectedValue1))

        val expectedName2 = Token(TokenType.IDENTIFIER, "b", null, 1)
        val expectedValue2 = Expression.Literal("str")
        assert(statements[1] == Statement.Variable(expectedName2, expectedValue2))
    }

    @Test
    fun `test invalid variable declaration (no semicolon at end of statement)`() {
        val tokens = Tokenizer("var a = 3").scanTokens()
        val statements = Parser(tokens).parseStatements()
        assert(statements.isEmpty())
    }

    /// Expression tests

    @Test
    fun `test simple math expression by inserting tokens manually`() {
        // val tokens = Tokenizer("1+2").scanTokens()
        // im not trusting Tokenizer class here :]
        val tokens =
            listOf(
                Token(TokenType.NUMBER, "1", 1.0, 1),
                Token(TokenType.PLUS, "+", null, 1),
                Token(TokenType.NUMBER, "2", 2.0, 1),
                Token(TokenType.EOF, "EOF", null, 1)
            )
        val ast = Parser(tokens).parseExpr()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedRight = Expression.Literal(2.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }

    @Test
    fun `test simple addition`() {
        val tokens = Tokenizer("1+2").scanTokens()
        val ast = Parser(tokens).parseExpr()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedRight = Expression.Literal(2.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }

    @Test
    fun `test multiplication precedence`() {
        val tokens = Tokenizer("1+2*3").scanTokens()
        val ast = Parser(tokens).parseExpr()

        val expectedLeft = Expression.Literal(1.0)
        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedOperator2 = Token(TokenType.STAR, "*", null, 1)
        val expectedRight =
            Expression.Binary(Expression.Literal(2.0), expectedOperator2, Expression.Literal(3.0))

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator, expectedRight))
    }

    @Test
    fun `test simple math with parentheses`() {
        val tokens = Tokenizer("(1+2)*3").scanTokens()
        val ast = Parser(tokens).parseExpr()

        val expectedOperator = Token(TokenType.PLUS, "+", null, 1)
        val expectedOperator2 = Token(TokenType.STAR, "*", null, 1)
        val expectedLeft =
            Expression.Grouping(
                Expression.Binary(
                    Expression.Literal(1.0),
                    expectedOperator,
                    Expression.Literal(2.0)
                )
            )
        val expectedRight = Expression.Literal(3.0)

        assertEquals(ast, Expression.Binary(expectedLeft, expectedOperator2, expectedRight))
    }

    @Test
    fun `test parser with string input`() {
        val tokens = Tokenizer("\"abc\"").scanTokens()
        val ast = Parser(tokens).parseExpr()
        assertEquals(ast, Expression.Literal("abc"))
    }
}
