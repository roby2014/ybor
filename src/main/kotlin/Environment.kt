class Environment {
    private val values = hashMapOf<String, Any?>()

    fun createVar(name: Token, value: Any?) =
        if (!values.containsKey(name.lexeme))
            values.set(name.lexeme, value)
        else
            throw RuntimeError(name, "Variable with name '${name.lexeme}' already exists")


    fun getVar(name: Token) =
        if (values.containsKey(name.lexeme))
            values[name.lexeme]
        else
            throw RuntimeError(name, "Undefined variable '${name.lexeme}'")

    fun assignVar(name: Token, value: Any?) {
        if (values.containsKey(name.lexeme))
            values[name.lexeme] = value
        else
            throw RuntimeError(name, "Undefined variable '${name.lexeme}'")
    }
}