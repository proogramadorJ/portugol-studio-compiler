package com.pedrodev

import symbols.Symbol

abstract class Statement {

    interface Visitor<R> {
        fun visitExprStatement(exprStatement: ExprStatement): R
        fun visitVarDeclarationStatement(stmt: VarDeclaration): R
        fun visitFuncStatement(stmt: Function): R
        fun visitBlockStatement(stmt: Block): R
        fun visitIfStatement(stmt: If): R
        fun visitWhileStatement(stmt: While): R
        fun visitDoWhileStatement(stmt: DoWhile): R
        fun visitForStatement(stmt: For): R
        fun visitReturnStatement(stmt: Return): R
        fun visitSwitchStatement(stmt: Switch): R
        fun visitArrayDeclarationStatement(stmt: ArrayDeclaration) : R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R

    class ExprStatement(val expr: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExprStatement(this)
        }
    }

    class VarDeclaration(val name: Token, val declaredType: Token, var initializer: Expression?, var symbol: Symbol? , val isConst: Boolean) :
        Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarDeclarationStatement(this)
        }
    }

    class Function(
        val name: Token,
        val returnType: Token,
        val params: List<Param>,
        val body: List<Statement>,
        var symbol: Symbol?
    ) :
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

    class DoWhile(val condition: Expression, val body: Statement) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitDoWhileStatement(this)
        }
    }

    class For(val expressionInitializer: Expression?, val varDeclarationInitializer: Statement?, val condition: Expression, val increment: Expression, val body: Statement) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitForStatement(this)
        }
    }

    class Return(val keyword: Token, val expression: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReturnStatement(this)
        }
    }

    class Switch(val expr: Expression, val cases : List<Pair<Expression, Statement>>, val defaultCase : Statement?) : Statement(){
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSwitchStatement(this)
        }
    }

    class ArrayDeclaration(val type : Token, val name : Token, var declaredSize : Int ?, var size: Expression, var values: List<Expression>, var symbol: Symbol?) : Statement(){
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitArrayDeclarationStatement(this)
        }
    }
}