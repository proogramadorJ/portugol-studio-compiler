package com.pedrodev

/**
 * Analisador sementico
 * Faz a resolução de escopos, verificação de tipos e enriquece a AST
 */
class TypeAndScopeResolver  : Statement.Visitor<Void?>, Expression.Visitor<Void?>{
    override fun visitExprStatement(exprStatement: Statement.ExprStatement): Void? {
        TODO("Not yet implemented")
    }

    override fun visitVarStatement(stmt: Statement.Var): Void? {
        TODO("Not yet implemented")
    }

    override fun visitFuncStatement(stmt: Statement.Function): Void? {
        TODO("Not yet implemented")
    }

    override fun visitBlockStatement(stmt: Statement.Block): Void? {
        TODO("Not yet implemented")
    }

    override fun visitIfStatement(stmt: Statement.If): Void? {
        TODO("Not yet implemented")
    }

    override fun visitWhileStatement(stmt: Statement.While): Void? {
        TODO("Not yet implemented")
    }

    override fun visitLiteral(expression: Expression.Literal): Void? {
        TODO("Not yet implemented")
    }

    override fun visitBinary(expression: Expression.Binary): Void? {
        TODO("Not yet implemented")
    }

    override fun visitLogical(expression: Expression.Logical): Void? {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary): Void? {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable): Void? {
        TODO("Not yet implemented")
    }

    override fun visitAssignExpr(expression: Expression.Assign): Void? {
        TODO("Not yet implemented")
    }
}