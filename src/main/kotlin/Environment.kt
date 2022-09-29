/** Data structure to store variables. */
class Environment(private val enclosing: Environment? = null) {

    /** Stores our variable values, where key is variable name and value is its literal value. */
    private val values = hashMapOf<String, Any?>()

    /** Creates a new variable. Throws if variable with that name is already defined. */
    fun createVar(name: Token, value: Any?) =
        when {
            !values.containsKey(name.lexeme) -> values.set(name.lexeme, value)
            else -> throw RuntimeError(name, "Variable with name '${name.lexeme}' already exists")
        }

    /** Returns [name]'s variable value. Throws if variable is undefined. */
    fun getVar(name: Token): Any? =
        when {
            values.containsKey(name.lexeme) -> values[name.lexeme]
            enclosing != null -> enclosing.getVar(name)
            else -> throw RuntimeError(name, "Undefined variable '${name.lexeme}'")
        }

    /** Assigns [value] to [name]. Throws if variable is undefined. */
    fun assignVar(name: Token, value: Any?): Any? =
        when {
            values.containsKey(name.lexeme) -> values[name.lexeme] = value
            enclosing != null -> enclosing.assignVar(name, value)
            else -> throw RuntimeError(name, "Undefined variable '${name.lexeme}'")
        }
}
