package com.pedrodev

class ByteCodeGenerator : ExprVisitor<List<String>> {
    override fun visitLiteral(expr: Expr.Literal): List<String> {
        return listOf("PUSH ${expr.value}")
    }

    override fun visitBinary(expr: Expr.Binary): List<String> {
        val left = expr.left.accept(this)
        val right = expr.right.accept(this)

        val opCode = when (expr.operator.type) {
            TokenType.TK_MULTPLICACAO -> "MUL"
            TokenType.TK_DIVISAO -> "DIV"
            TokenType.TK_SOMA -> "ADD"
            TokenType.TK_SUBTRACAO -> "SUB"
            else -> throw IllegalArgumentException("Operador desconhecido: ${expr.operator.lexeme}")
        }

        return left + right + listOf(opCode)
    }

    fun generate(expr: Expr): List<String> {
        return expr.accept(this)
    }
}