package com.pedrodev

class ASTPrinter : ExprVisitor<String> {
    override fun visitLiteral(expr: Expr.Literal): String {
        return expr.value.toString()
    }

    override fun visitBinary(expr: Expr.Binary): String {
        val left = expr.left.accept(this)
        val right = expr.right.accept(this)
        val operation = expr.operator.lexeme

        return "(${left} $operation ${right})"
    }

    override fun visitLogical(expr: Expr.Logical): String {
        val left = expr.left.accept(this)
        val right = expr.right.accept(this)
        val operation = expr.operator.lexeme
        return "(${left} $operation ${right})"
    }

    fun print(expr: Expr) {
        println(expr.accept(this))
    }
}