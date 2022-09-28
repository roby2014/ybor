/** Represents a RuntimeError exception. */
class RuntimeError(val token: Token, message: String) : RuntimeException(message)