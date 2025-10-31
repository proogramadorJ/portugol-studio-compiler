package com.pedrodev

abstract class Statement {

    interface Visitor<R> {
        fun visitExprStatement(exprStatement: ExprStatement): R
        fun visitVarStatement(stmt: Var): R
        fun visitFuncStatement(stmt: Function): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    class ExprStatement(val expr: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExprStatement(this)
        }
    }

    class Var(val name: Token, val type: Token, val initializer: Expression?) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarStatement(this)
        }
    }

    class Function(val name: Token, val returnType: Token, val params: List<Param>, val body: List<Statement>) :
        Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitFuncStatement(this)
        }
    }
}