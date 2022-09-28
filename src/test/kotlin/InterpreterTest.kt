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
    fun `test valid comparisons with numbers (greater, greater or equal, less, less or equal)`() {
        val tests = listOf(
            "1 > 2" to false,
            "1 >= 2" to false,
            "1 < 2" to true,
            "1 <= 2" to true,
            "1 <= \"a\"" to null,
        )
        tests.forEach { (test, expected) ->
            val tokens = Tokenizer(test).scanTokens()
            val ast = Parser(tokens).parse()!!
            assert(Interpreter.interpret(ast) == expected)
        }
    }

    @Test
    fun `test boolean expressions with numbers (equal, not equal)`() {
        val tests = listOf(
            "1 == 2" to false,
            "1 != 2" to true,
            "1 == 1" to true,
            "1 != 1" to false,
            "1 == \"a\"" to false,
            "1 != \"a\"" to true
        )
        tests.forEach { (test, expected) ->
            val tokens = Tokenizer(test).scanTokens()
            val ast = Parser(tokens).parse()!!
            assert(Interpreter.interpret(ast) == expected)
        }
    }

    @Test
    fun `test boolean expressions with strings (equal, not equal)`() {
        val tests = listOf(
            "\"abc\" != \"abc\"" to false,
            "\"abc\" == \"abc\"" to true,
            "\"abc\" != \"a\"" to true,
            "\"abc\" == \"a\"" to false,
            "\"abc\" == 1" to false
        )
        tests.forEach { (test, expected) ->
            val tokens = Tokenizer(test).scanTokens()
            val ast = Parser(tokens).parse()!!
            assert(Interpreter.interpret(ast) == expected)
        }
    }

    @Test
    fun `test invalid expression #1 (add number with string)`() {
        val tokens = Tokenizer("1+\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #2 (sub number by string)`() {
        val tokens = Tokenizer("1-\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #3 (multiply number by string)`() {
        val tokens = Tokenizer("1*\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

    @Test
    fun `test invalid expression #4 (divide number by string)`() {
        val tokens = Tokenizer("1/\"a\"").scanTokens()
        val ast = Parser(tokens).parse()!!
        assert(Interpreter.interpret(ast) == null)
    }

}