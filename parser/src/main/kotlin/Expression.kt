package com.pedrodev

import symbols.Symbol
import types.TokenType

abstract class Expression {

    abstract fun <R> accept(visitor: Visitor<R>): R

    interface Visitor<R> {
        fun visitLiteral(expression: Literal): R
        fun visitBinary(expression: Binary): R
        fun visitLogical(expression: Logical): R
        fun visitUnary(expression: Unary): R
        fun visitVariable(expression: Variable): R
        fun visitAssignExpr(expression: Assign): R
    }

    class Binary(val left: Expression, val operator: Token, val right: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinary(this)
        }
    }

    class Literal(val value: Any, val type: TokenType) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteral(this)
        }
    }

    class Logical(val left: Expression, val operator: Token, val right: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLogical(this)
        }
    }

    class Unary(val operator: Token, val right: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnary(this)
        }
    }

    class Variable(val name: Token, var symbol: Symbol?) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariable(this)
        }
    }

    class Assign(val name: Token, val value: Expression, var symbol: Symbol?) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitAssignExpr(this)
        }
    }
}