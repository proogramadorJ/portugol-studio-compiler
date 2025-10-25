package com.pedrodev

class ASTPrinter : Expression.Visitor<String>, Statement.Visitor<Void?> {
    override fun visitLiteral(expression: Expression.Literal): String {
        return expression.value.toString()
    }

    override fun visitBinary(expression: Expression.Binary): String {
        val left = expression.left.accept(this)
        val right = expression.right.accept(this)
        val operation = expression.operator.lexeme

        return "(${left} $operation ${right})"
    }

    override fun visitLogical(expression: Expression.Logical): String {
        val left = expression.left.accept(this)
        val right = expression.right.accept(this)
        val operation = expression.operator.lexeme
        return "(${left} $operation ${right})"
    }

    override fun visitUnary(expression: Expression.Unary): String {
        val right = expression.right.accept(this)
        return "(${expression.operator.lexeme}${right})"
    }

    override fun visitVariable(expression: Expression.Variable): String {
        TODO("Not yet implemented")
    }

    fun print(expression: Expression) {
        println(expression.accept(this))
    }

    override fun visitExprStatement(expr: Statement.Expr): Void? {
        TODO("Not yet implemented")
    }

    override fun visitVarStatement(stmt: Statement.Var): Void? {
        TODO("Not yet implemented")
    }
}