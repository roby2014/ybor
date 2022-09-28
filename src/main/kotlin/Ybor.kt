import java.io.File
import kotlin.system.exitProcess

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
        val ast = Parser(tokens).parseStatements()

        if (err || runtimeErr)
            return

        Interpreter.interpret(ast)
    }

    /** Executes [filename] source content. */
    fun execFile(filename: String) {
        val src = File(filename).readText()
        exec(src)
        if (err || runtimeErr)
            exitProcess(1337) // TODO: different status code for syntax/runtime error
    }

    /** Runs a prompt where custom commands can be executed (aKa REPL: read–eval–print-loop). */
    fun execPrompt() {
        while (true) {
            print("> ")
            val input = readln()
            if (input.isEmpty() || input == "exit") break
            exec(input)
            err = false
            // runtimeErr = false // I suppose the REPL does not care about runtime errors since its single expressions(?)
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