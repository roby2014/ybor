import java.io.File

/**
 * Main functions to make the interpreter run code or debug/display errors.
 */
object Ybor {
    private var err: Boolean = false

    /** Executes [source] */
    fun exec(source: String) {
        val tokens = Tokenizer(source).scanTokens()
        val expr = Parser(tokens).parse()

        if (expr != null && !err) {
            AstPrinter.debug(expr)
        } else {
            println("Could not parse source input.")
        }
    }

    /** Executes [filename] content. */
    fun execFile(filename: String) {
        exec(File(filename).readText())
    }

    /** Runs a prompt where custom commands can be executed. */
    fun execPrompt() {
        while (true) {
            println("> ")
            val input = readln()
            if (input.isEmpty()) break
            exec(input)
        }
    }

    /** Wrapper for [reportError]. */
    fun error(line: Int, msg: String) {
        reportError(line, "", msg)
    }

    /** Reports error with its [line] and error [msg]. */
    private fun reportError(line: Int, where: String, msg: String) {
        println("[line $line] ERROR $where : $msg")
        err = true
    }
}

fun main(args: Array<String>) {
    //testAstPrinter()

// ybor [file (optional)]
    Ybor.exec("(1 * 2) + 5")
    return
    when {
        args.size > 1 || args.isEmpty() -> println("Usage: ybor [file]")
        args.size == 1 -> Ybor.execFile(args[0])
        else -> Ybor.execPrompt()
    }
}