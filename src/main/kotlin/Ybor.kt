import java.io.File

/**
 * Main functions to make the interpreter run code or debug/display errors.
 */
object Ybor {
    /** True if there was an error reported (by the lexer/parser) */
    private var err = false

    /** Executes [source] */
    fun exec(source: String) {
        val tokens = Tokenizer(source).scanTokens()
        val ast = Parser(tokens).parse()

        println(tokens)

        if (!err && ast != null) {
            AstPrinter.debug(ast)
        } else {
            // error
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
}