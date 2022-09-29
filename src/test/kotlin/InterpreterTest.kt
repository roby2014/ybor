import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** Implements unit tests for [Interpreter]. */
class InterpreterTest {

    /// Statements tests

    @Test
    fun `test variable declaration and print statement`() {
        val src = """
            var a = 1+2;
            print a;
            """
        val tokens = Tokenizer(src).scanTokens()
        val statements = Parser(tokens).parseStatements()
        val output = tapSystemOut { Interpreter.interpret(statements) }
        assertEquals("3.0", output.trim())
    }

    @Test
    fun `test printing undefined variable`() {
        val src = """
            print b;
            """
        val tokens = Tokenizer(src).scanTokens()
        val statements = Parser(tokens).parseStatements()
        val output = tapSystemOut { Interpreter.interpret(statements) }
        assertTrue(output.contains("Undefined variable 'b'"))
    }

    @Test
    fun `test printing invalid expression`() {
        val src = """
            print "aaa"+1;
            """
        val tokens = Tokenizer(src).scanTokens()
        val statements = Parser(tokens).parseStatements()
        val output = tapSystemOut { Interpreter.interpret(statements) }
        assertTrue(output.contains("Can't sum 'aaa' with '1.0'"))
    }

    @Test
    fun `test printing variable from another block`() {
        val src =
            """
            {
                var a = 3;
                print a;
            }
            print a;
            """
        val tokens = Tokenizer(src).scanTokens()
        val statements = Parser(tokens).parseStatements()
        val output = tapSystemOut { Interpreter.interpret(statements) }
        assertEquals(output.split("\n")[0].toDouble(), 3.0)
        assertTrue(output.contains("Undefined variable 'a'")) // line 6
    }

    @Test
    fun `test division by 0`() {
        val src = """
            print 1/0;
            """
        val tokens = Tokenizer(src).scanTokens()
        val statements = Parser(tokens).parseStatements()
        val output = tapSystemOut { Interpreter.interpret(statements) }
        assertTrue(output.contains("Can't divide by 0"))
    }

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
        assertFailsWith<RuntimeError>(
            message = "Operand must be a number",
            block = { Interpreter.eval(ast) }
        )
    }

    @Test
    fun `test eval valid comparisons with numbers (greater, greater or equal, less, less or equal)`() {
        val tests =
            listOf(
                "1 > 2" to false,
                "1 >= 2" to false,
                "1 < 2" to true,
                "1 <= 2" to true,
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
        assertFailsWith<RuntimeError>(
            message = "Can't sum '1.0' with 'a'",
            block = { Interpreter.eval(ast) }
        )
    }

    @Test
    fun `test eval invalid expression #2 (sub number by string)`() {
        val tokens = Tokenizer("1-\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertFailsWith<RuntimeError>(
            message = "Can't sub '1.0' with 'a'",
            block = { Interpreter.eval(ast) }
        )
    }

    @Test
    fun `test eval invalid expression #3 (multiply number by string)`() {
        val tokens = Tokenizer("1*\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertFailsWith<RuntimeError>(
            message = "Can't multiply '1.0' with 'a'",
            block = { Interpreter.eval(ast) }
        )
    }

    @Test
    fun `test eval invalid expression #4 (divide number by string)`() {
        val tokens = Tokenizer("1/\"a\"").scanTokens()
        val ast = Parser(tokens).parseExpr()!!
        assertFailsWith<RuntimeError>(
            message = "Can't divide '1.0' by 'a'",
            block = { Interpreter.eval(ast) }
        )
    }
}
