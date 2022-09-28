import java.io.File

/**
 * Main functions to make the interpreter run code or debug/display errors.
 */
object Ybor {
    /** True if there was an error reported (by the lexer/parser) */
    private var err = false

    /** True if there was a runtime error reported (by the interpreter) */
    private var runtimeErr = false

    /** Executes [source] */
    fun exec(source: String) {
        val tokens = Tokenizer(source).scanTokens()
        val ast = Parser(tokens).parse()

        ast?.let { it ->
            AstPrinter.debug(it)
            println("Result: ${Interpreter.interpret(it)}")
        }
    }

    /** Executes [filename] source content. */
    fun execFile(filename: String) = exec(File(filename).readText())

    /** Runs a prompt where custom commands can be executed. */
    fun execPrompt() {
        while (true) {
            print("> ")
            val input = readln()
            if (input.isEmpty() || input == "exit") break
            exec(input)
        }
    }

    /** Wrapper for [reportError]. */
    fun error(token: Token, msg: String) = reportError(token.line, token.lexeme, msg)
    fun error(line: Int, msg: String) = reportError(line, "", msg)

    /** Reports error with its [line] and error [msg]. */
    private fun reportError(line: Int, where: String, msg: String) {
        println("[line $line] ERROR at '$where': $msg")
        err = true
    }

    /** Reports a runtime error */
    fun runtimeError(error: RuntimeError) {
        println("[line ${error.token.line}] ERROR at '${error.token.lexeme}': ${error.message}")
        runtimeErr = true
    }
}