/**
 * Represents an "Expression".
 */
abstract class Expression {
    interface Visitor<T> {
        fun visitBinaryExpr(expr: Binary): T
        fun visitGroupingExpr(expr: Grouping): T
        fun visitLiteralExpr(expr: Literal): T
        fun visitUnaryExpr(expr: Unary): T
    }

    abstract fun <T> accept(visitor: Visitor<T>): T

    /** Represents a binary expression. */
    class Binary(val left: Expression, val operator: Token, val right: Expression) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitBinaryExpr(this)
        }

        override fun equals(other: Any?) = when {
            this === other -> true
            other == null || other !is Binary -> false
            left != other.left || right != other.right || operator != other.operator -> false
            else -> true
        }

        override fun hashCode(): Int = 31 * left.hashCode() + operator.hashCode() + right.hashCode()
    }

    /** Represents a grouping expression (expressions inside parentheses). */
    class Grouping(val expr: Expression) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitGroupingExpr(this)
        }

        override fun equals(other: Any?) = when {
            this === other -> true
            other == null || other !is Grouping -> false
            expr != other.expr -> false
            else -> true
        }

        override fun hashCode(): Int = expr.hashCode()
    }

    /** Represents a literal value. */
    class Literal(val value: Any?) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitLiteralExpr(this)
        }

        override fun equals(other: Any?) = when {
            this === other -> true
            other == null || other !is Literal -> false
            value != other.value -> false
            else -> true
        }

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    /** Represents a unary expression ([operator][right]), e.g -3. */
    class Unary(val operator: Token, val right: Expression) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitUnaryExpr(this)
        }

        override fun equals(other: Any?) = when {
            this === other -> true
            other == null || other !is Unary -> false
            right != other.right || operator != other.operator -> false
            else -> true
        }

        override fun hashCode(): Int = 31 * operator.hashCode() + right.hashCode()
    }

}