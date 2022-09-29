import kotlin.test.*

/** Implements unit tests for [Interpreter]. */
class InterpreterTest {

    // TODO: test statements and their output...

    /// Expressions evaluation tests

    @Test
    fun `test eval math #1`() {
        val tokens = Tokenizer("1+2").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == 3.0)
    }

    @Test
    fun `test eval math #2`() {
        val tokens = Tokenizer("1+2*3").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == 7.0)
    }

    @Test
    fun `test eval math #3`() {
        val tokens = Tokenizer("(1+2)*3").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == 9.0)
    }

    @Test
    fun `test eval string sum (concatenation)`() {
        val tokens = Tokenizer("\"hello\" + \"world\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == "helloworld")
    }

    @Test
    fun `test eval string sub (replacing)`() {
        val tokens = Tokenizer("\"cool interpreter\" - \"cool \"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == "interpreter")
    }

    @Test
    fun `test eval valid unary #1 (negative number)`() {
        val tokens = Tokenizer("-2").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == -2.0)
    }

    @Test
    fun `test eval valid unary #2 (negating number)`() {
        val tokens = Tokenizer("!1").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == false)
    }

    @Test
    fun `test eval valid unary #3 (negating true)`() {
        val tokens = Tokenizer("!true").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == false)
    }

    @Test
    fun `test eval valid unary #4 (negating null)`() {
        val tokens = Tokenizer("!nil").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == true)
    }

    @Test
    fun `test eval invalid unary (negative string)`() {
        val tokens = Tokenizer("-\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == null)
    }

    @Test
    fun `test eval valid comparisons with numbers (greater, greater or equal, less, less or equal)`() {
        val tests =
            listOf(
                "1 > 2" to false,
                "1 >= 2" to false,
                "1 < 2" to true,
                "1 <= 2" to true,
                "1 <= \"a\"" to null,
            )
        tests.forEach { (test, expected) ->
            val tokens = Tokenizer(test).scanTokens()
            val ast = Parser(tokens).parseExpr()!!
            assert(Interpreter.eval(ast) == expected)
        }
    }

    @Test
    fun `test eval boolean expressions with numbers (equal, not equal)`() {
        val tests =
            listOf(
                "1 == 2" to false,
                "1 != 2" to true,
                "1 == 1" to true,
                "1 != 1" to false,
                "1 == \"a\"" to false,
                "1 != \"a\"" to true
            )
        tests.forEach { (test, expected) ->
            val tokens = Tokenizer(test).scanTokens()
            val ast = Parser(tokens).parseExpr()!!
            assert(Interpreter.eval(ast) == expected)
        }
    }

    @Test
    fun `test eval boolean expressions with strings (equal, not equal)`() {
        val tests =
            listOf(
                "\"abc\" != \"abc\"" to false,
                "\"abc\" == \"abc\"" to true,
                "\"abc\" != \"a\"" to true,
                "\"abc\" == \"a\"" to false,
                "\"abc\" == 1" to false
            )
        tests.forEach { (test, expected) ->
            val tokens = Tokenizer(test).scanTokens()
            val ast = Parser(tokens).parseExpr()!!
            assert(Interpreter.eval(ast) == expected)
        }
    }

    @Test
    fun `test eval invalid expression #1 (add number with string)`() {
        val tokens = Tokenizer("1+\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == null)
    }

    @Test
    fun `test eval invalid expression #2 (sub number by string)`() {
        val tokens = Tokenizer("1-\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == null)
    }

    @Test
    fun `test eval invalid expression #3 (multiply number by string)`() {
        val tokens = Tokenizer("1*\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == null)
    }

    @Test
    fun `test eval invalid expression #4 (divide number by string)`() {
        val tokens = Tokenizer("1/\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assert(Interpreter.eval(ast) == null)
    }
}
