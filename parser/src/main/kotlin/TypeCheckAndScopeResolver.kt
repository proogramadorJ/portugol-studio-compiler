package com.pedrodev

/**
 * Analisador semantico
 * Faz a resolução de escopos, verificação de tipos e enriquece a AST
 */
class TypeCheckAndScopeResolver : Statement.Visitor<Void?>, Expression.Visitor<TokenType?> {

    private val scopes: ArrayDeque<MutableMap<String, String>> = ArrayDeque()
    private var offset: Int = 0

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

    override fun visitLiteral(expression: Expression.Literal): TokenType? {
        TODO("Not yet implemented")
    }

    override fun visitBinary(expression: Expression.Binary): TokenType? {
        TODO("Not yet implemented")
    }

    override fun visitLogical(expression: Expression.Logical): TokenType? {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary): TokenType? {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable): TokenType? {
        TODO("Not yet implemented")
    }

    override fun visitAssignExpr(expression: Expression.Assign): TokenType? {
        TODO("Not yet implemented")
    }

}