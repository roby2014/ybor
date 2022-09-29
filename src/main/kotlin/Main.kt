fun main(args: Array<String>) =
    when (args.size) {
        0 -> Ybor.execPrompt() // aKa REPL: read–eval–print-loop
        1 -> Ybor.execFile(args[0])
        else -> println("Usage: ybor [file (optional)]")
    }
