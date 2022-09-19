/**
 * Represents the token's type.
 * [dbg] is text describing how the token lexeme could look like,
 * but it's not really useful for now.
 */
enum class TokenType(val dbg: String = "") {
    // literals
    IDENTIFIER, STRING, NUMBER,

    // single char
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    COMMA(","),
    DOT("."),
    MINUS("-"),
    PLUS("+"),
    SEMICOLON(";"),
    SLASH("/"),
    STAR("*"),

    // one or two char
    BANG("!"),
    BANG_EQUAL("!="),
    EQUAL("="),
    EQUAL_EQUAL("=="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),

    // keywords
    IF, ELSE, ELIF,
    AND, OR,
    FALSE, TRUE,
    WHILE, FOR,
    FUN, NIL, RETURN, THIS, VAR,

    WHITESPACE, NEW_LINE, COMMENT,

    EOF("EOF")
}