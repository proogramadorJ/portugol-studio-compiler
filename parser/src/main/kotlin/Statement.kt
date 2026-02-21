package com.pedrodev

abstract class Statement {

    interface Visitor<R> {
        fun visitExprStatement(exprStatement: ExprStatement): R
        fun visitVarStatement(stmt: VarDeclaration): R
        fun visitFuncStatement(stmt: Function): R
        fun visitBlockStatement(stmt: Block): R
        fun visitIfStatement(stmt: If): R
        fun visitWhileStatement(stmt: While): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    class ExprStatement(val expr: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExprStatement(this)
        }
    }

    class VarDeclaration(val name: Token, val declaredType: Token, val initializer: Expression?) : Statement() {
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

    class Block(val body: List<Statement>) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBlockStatement(this)
        }
    }

    class If(val condition: Expression, val thenBranch: Statement, val elseBranch: Statement?) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIfStatement(this)
        }
    }

    class While(val condition: Expression, val body: Statement) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitWhileStatement(this)
        }
    }
}