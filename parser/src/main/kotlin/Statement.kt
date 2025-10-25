package com.pedrodev

abstract class Statement {

    interface Visitor<R> {
        fun visitExprStatement(expr: Expr): R
        fun visitVarStatement(stmt: Var): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    class Expr(val expr: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExprStatement(this)
        }
    }

    class Var(val name: Token, val type: Token, val initializer: Expression?) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarStatement(this)
        }
    }
}