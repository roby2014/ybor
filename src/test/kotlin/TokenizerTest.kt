import kotlin.test.*

/**
 * Implements tests for [Tokenizer] aKa Lexer class.
 * TODO: Implement more tests
 */
class TokenizerTest {

    @Test
    fun `test tokenizer with simple input #1`() {
        val tokens = Tokenizer("(()) {}").scanTokens()
        val expectedTokens = listOf(
            TokenType.LEFT_PAREN,
            TokenType.LEFT_PAREN,
            TokenType.RIGHT_PAREN,
            TokenType.RIGHT_PAREN,
            TokenType.LEFT_BRACE,
            TokenType.RIGHT_BRACE,
            TokenType.EOF
        )
        repeat(tokens.size) {
            assertEquals(tokens[it].type, expectedTokens[it])
        }
    }

    @Test
    fun `test tokenizer with simple input #2`() {
        val tokens = Tokenizer("!*+-/=<> <= == //").scanTokens()
        val expectedTokens = listOf(
            TokenType.BANG,
            TokenType.STAR,
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.SLASH,
            TokenType.EQUAL,
            TokenType.LESS,
            TokenType.GREATER,
            TokenType.LESS_EQUAL,
            TokenType.EQUAL_EQUAL,
            TokenType.COMMENT,
            TokenType.EOF
        )
        repeat(tokens.size) {
            assertEquals(tokens[it].type, expectedTokens[it])
        }
    }

    @Test
    fun `test tokenizer with big input using a smarter way (i think)`() {
        val tokens = Tokenizer("(()) {} !*+-/=<> <= == //").scanTokens()
        for (tkType in TokenType.values()) {
            for (tk in tokens) {
                if (tk.lexeme == tkType.lexeme) {
                    assertEquals(tk.type, tkType)
                }
            }
        }
    }

    @Test
    fun `test comment parsing`() {
        val tokens = Tokenizer("// this is a comment").scanTokens()
        assertEquals(tokens[0].type, TokenType.COMMENT)
        assertEquals(tokens[0].lexeme, "// this is a comment")
    }

    @Test
    fun `test string parsing`() {
        val tokens = Tokenizer(" \"this is a string\" ").scanTokens()
        assertEquals(tokens[0].type, TokenType.STRING)
        assertEquals(tokens[0].lexeme, "\"this is a string\"")
        assertEquals(tokens[0].literal, "this is a string")
    }

    @Test
    fun `test number parsing`() {
        val tokens = Tokenizer("123\n12.3\n12.").scanTokens()
        assertEquals(tokens[0].type, TokenType.NUMBER)
        assertEquals(tokens[0].literal, 123.0)

        assertEquals(tokens[1].type, TokenType.NUMBER)
        assertEquals(tokens[1].literal, 12.3)

        assertEquals(tokens[2].type, TokenType.NUMBER)
        assertEquals(tokens[2].literal, 12.0)

        assertEquals(tokens[3].type, TokenType.DOT)
    }

    @Test
    fun `test identifier + keywords parsing`() {
        val tokens = Tokenizer("abc or xd\nfor\nwhile").scanTokens()
        assertEquals(tokens[0].type, TokenType.IDENTIFIER)
        assertEquals(tokens[0].lexeme, "abc")

        assertEquals(tokens[1].type, TokenType.OR)
        assertEquals(tokens[1].lexeme, "or")

        assertEquals(tokens[2].type, TokenType.IDENTIFIER)
        assertEquals(tokens[2].lexeme, "xd")

        assertEquals(tokens[3].type, TokenType.FOR)
        assertEquals(tokens[3].lexeme, "for")

        assertEquals(tokens[4].type, TokenType.WHILE)
        assertEquals(tokens[4].lexeme, "while")
    }
}