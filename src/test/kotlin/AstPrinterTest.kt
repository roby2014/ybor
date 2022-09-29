import kotlin.test.Test
import kotlin.test.assertEquals

class AstPrinterTest {
    @Test
    fun `test assign expression`() {
        val tokens = Tokenizer("a = 3").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("(variable assign 'a' 3.0)", AstPrinter.debug(ast))
    }

    @Test
    fun `test simple binary expression`() {
        val tokens = Tokenizer("1+3").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("(+ 1.0 3.0)", AstPrinter.debug(ast))
    }

    @Test
    fun `test operator precedence in binary expressions`() {
        val tokens = Tokenizer("1+2*3").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("(+ 1.0 (* 2.0 3.0))", AstPrinter.debug(ast))
    }

    @Test
    fun `test math grouping parentheses`() {
        val tokens = Tokenizer("(1+2)*3").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("(* (group (+ 1.0 2.0)) 3.0)", AstPrinter.debug(ast))
    }

    @Test
    fun `test literal expression`() {
        val tokens = Tokenizer("1337").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("1337.0", AstPrinter.debug(ast))
    }

    @Test
    fun `test unary expression`() {
        val tokens = Tokenizer("-1").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("(- 1.0)", AstPrinter.debug(ast))
    }

    @Test
    fun `test variable expression`() {
        val tokens = Tokenizer("a").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertEquals("(variable access 'a')", AstPrinter.debug(ast))
    }
}
