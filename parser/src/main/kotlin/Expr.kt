package com.pedrodev

sealed interface Expr {

    fun <R> accept(visitor: ExprVisitor<R>): R

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr {
        override fun <R> accept(visitor: ExprVisitor<R>): R {
            return visitor.visitBinary(this)
        }
    }

    data class Literal(val value: Any) : Expr {
        override fun <R> accept(visitor: ExprVisitor<R>): R {
            return visitor.visitLiteral(this)
        }
    }

    data class Logical(val left : Expr, val operator: Token, val right: Expr) : Expr{
        override fun <R> accept(visitor: ExprVisitor<R>): R {
            return visitor.visitLogical(this)
        }
    }

}