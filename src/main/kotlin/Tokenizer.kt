/**
 * Tokenizer class (also known as a Lexer).
 * Implements function to transform our [source] into tokens.
 */
class Tokenizer(private val source: String) {
    /** token list */
    var tokens = mutableListOf<Token>()

    /** current line count */
    var line = 1

    /** index of token's lexeme start */
    private var start = 0

    /** index of current character */
    private var current = 0

    /** reserved keywords */
    private var keywords = hashMapOf<String, TokenType>(
        "if" to TokenType.IF,
        "else" to TokenType.ELSE,
        "elif" to TokenType.ELIF,
        "and" to TokenType.AND,
        "or" to TokenType.OR,
        "false" to TokenType.FALSE,
        "true" to TokenType.TRUE,
        "for" to TokenType.FOR,
        "while" to TokenType.WHILE,
        "fun" to TokenType.FUN,
        "nil" to TokenType.NIL,
        "return" to TokenType.RETURN,
        "var" to TokenType.VAR,
        "this" to TokenType.THIS,
    )

    /** Reads source input and scan for tokens. */
    fun scanTokens(): List<Token> {
        while (!endOfFile()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "EOF", null, line))
        return tokens
    }

    /**
     * Tries to scan a token depending on the current input.
     * In case it finds a valid input, it adds the token to the list.
     */
    private fun scanToken() {
        val c = advance()

        // "simple" cases
        val tokenToAdd = when (c) {
            '(' -> TokenType.LEFT_PAREN
            ')' -> TokenType.RIGHT_PAREN
            '{' -> TokenType.LEFT_BRACE
            '}' -> TokenType.RIGHT_BRACE
            ',' -> TokenType.COMMA
            '.' -> TokenType.DOT
            '-' -> TokenType.MINUS
            '+' -> TokenType.PLUS
            ';' -> TokenType.SEMICOLON
            '*' -> TokenType.STAR
            '!' -> if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG
            '=' -> if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL
            '<' -> if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS
            '>' -> if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER
            '/' -> {
                if (match('/')) {
                    while (!endOfFile() && peek() != '\n') advance()
                    TokenType.COMMENT
                } else {
                    TokenType.SLASH
                }
            }
            ' ', '\r', '\t' -> TokenType.WHITESPACE
            else -> null
        }

        if (tokenToAdd != null) {
            addToken(tokenToAdd)
            return
        }

        // "complex/different" cases
        when (c) {
            '\n' -> line++
            '"' -> getStringToken()
            else -> {
                when {
                    c.isDigit() -> getNumberToken()
                    c.isLetter() -> getIdentifierToken()
                    else -> {
                        Ybor.error(line, "Unexpected character ($c).")
                    }
                }
            }
        }
    }

    /** Adds a new token with type [type] to the token list. */
    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    /** Adds [TokenType.STRING] if a string was found. */
    private fun getStringToken() {
        while (peek() != '"' && !endOfFile()) {
            if (peek() == '\n') line++
            advance()
        }

        if (endOfFile()) {
            Ybor.error(line, "Expected end of string.")
            return
        }

        advance() // closing "
        val literal = source.substring(start + 1, current - 1) // remove '"'s
        addToken(TokenType.STRING, literal)
    }

    /** Adds [TokenType.NUMBER] if a number was found. */
    private fun getNumberToken() {
        while (peek().isDigit()) {
            advance()
        }

        if (peek() == '.' && peek(1).isDigit()) {
            advance()
            while (peek().isDigit()) advance()
        }

        val literal = source.substring(start, current).toDouble()
        addToken(TokenType.NUMBER, literal)
    }

    /** Adds [TokenType.IDENTIFIER] if an identifier was found. */
    private fun getIdentifierToken() {
        while (peek().isAlphaNumeric()) {
            advance()
        }

        val literal = source.substring(start, current)
        addToken(keywords[literal] ?: TokenType.IDENTIFIER)
    }

    /** Returns true if we are at end of file. */
    private fun endOfFile() = current >= source.length

    /** Returns true if next character is equal to [expected], advancing one. */
    private fun match(expected: Char): Boolean {
        if (endOfFile() || source[current] != expected) return false
        ++current
        return true
    }

    /** Advance 1 character. */
    private fun advance() = source[current++]

    /** Returns character at current+[offset] position. */
    private fun peek(offset: Int = 0): Char {
        val n = current + offset
        return if (n >= source.length) 0.toChar()
        else source[n]
    }

    /** Returns true if [this] is a letter or a number. */
    private fun Char.isAlphaNumeric() = this.isLetter() || this.isDigit() || this == '_'

}