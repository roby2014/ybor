/**
 * Represents a single token.
 * @param lexeme word that the token contains
 * @param literal literal value of it (text if a string, number if a number, ...)
 * The other parameters should be pretty self-descriptive.
 */
data class Token(val type: TokenType, val lexeme: String, val literal: Any?, val line: Int) {}