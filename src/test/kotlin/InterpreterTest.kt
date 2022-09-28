import kotlin.test.*

/** Implements unit tests for [Interpreter]. */
class InterpreterTest {

    @Test
    fun `test math #1`() {
        val tokens = Tokenizer("1+2").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == 3.0)
    }

    @Test
    fun `test math #2`() {
        val tokens = Tokenizer("1+2*3").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == 7.0)
    }

    @Test
    fun `test math #3`() {
        val tokens = Tokenizer("(1+2)*3").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == 9.0)
    }

    @Test
    fun `test string sum (concatenation)`() {
        val tokens = Tokenizer("\"hello\" + \"world\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == "helloworld")
    }

    @Test
    fun `test string sub (replacing)`() {
        val tokens = Tokenizer("\"cool interpreter\" - \"cool \"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == "interpreter")
    }

    @Test
    fun `test valid unary #1 (negative number)`() {
        val tokens = Tokenizer("-2").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == -2.0)
    }

    @Test
    fun `test valid unary #2 (negating number)`() {
        val tokens = Tokenizer("!1").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == false)
    }

    @Test
    fun `test valid unary #3 (negating true)`() {
        val tokens = Tokenizer("!true").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == false)
    }

    @Test
    fun `test valid unary #4 (negating null)`() {
        val tokens = Tokenizer("!nil").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == true)
    }

    @Test
    fun `test invalid unary (negative string)`() {
        val tokens = Tokenizer("-\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #1`() {
        val tokens = Tokenizer("1+\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #2`() {
        val tokens = Tokenizer("1-\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #3`() {
        val tokens = Tokenizer("1*\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #4`() {
        val tokens = Tokenizer("1/\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

}