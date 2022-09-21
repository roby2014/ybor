abstract class Expression {
    interface Visitor<T> {
        fun visitBinaryExpr(expr: Binary): T
        fun visitGroupingExpr(expr: Grouping): T
        fun visitLiteralExpr(expr: Literal): T
        fun visitUnaryExpr(expr: Unary): T
    }

    abstract fun <T> accept(visitor: Visitor<T>): T

    class Binary(val left: Expression, val operator: Token, val right: Expression) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitBinaryExpr(this)
        }
    }

    class Grouping(val expr: Expression) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitGroupingExpr(this)
        }
    }

    class Literal(val value: Any?) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitLiteralExpr(this)
        }
    }

    class Unary(val operator: Token, val right: Expression) : Expression() {
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitUnaryExpr(this)
        }
    }

}