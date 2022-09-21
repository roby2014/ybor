import java.io.File

/**
 * Main functions to make the interpreter run code or debug/display errors.
 */
object Ybor {
    private var err: Boolean = false

    /** Executes [source] */
    fun exec(source: String) {
        val tokens: List<Token> = Tokenizer(source).scanTokens()

        println("DEBUG:")
        for (token in tokens) {
            println(token)
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

fun testAstPrinter() {
    val expression = Expression.Binary(
        Expression.Unary(
            Token(TokenType.MINUS, "-", null, 1), Expression.Literal(123)
        ), Token(TokenType.STAR, "*", null, 1), Expression.Grouping(Expression.Literal(45.56))
    )
    println(AstPrinter().print(expression))
}

fun main(args: Array<String>) {
    //testAstPrinter()

// ybor [file (optional)]
    Ybor.exec("a")
    when {
        args.size > 1 || args.isEmpty() -> println("Usage: ybor [file]")
        args.size == 1 -> Ybor.execFile(args[0])
        else -> Ybor.execPrompt()
    }
}