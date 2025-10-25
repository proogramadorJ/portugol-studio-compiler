package com.pedrodev

class ByteCodeGenerator : ExprVisitor<List<String>> {
    override fun visitLiteral(expression: Expression.Literal): List<String> {
        return listOf("PUSH ${expression.value}")
    }

    override fun visitBinary(expression: Expression.Binary): List<String> {
        val left = expression.left.accept(this)
        val right = expression.right.accept(this)

        val opCode = when (expression.operator.type) {
            TokenType.TK_MULTPLICACAO -> "MUL"
            TokenType.TK_DIVISAO -> "DIV"
            TokenType.TK_SOMA -> "ADD"
            TokenType.TK_SUBTRACAO -> "SUB"
            TokenType.TK_MODULO -> "MOD"
            else -> throw IllegalArgumentException("Operador desconhecido: ${expression.operator.lexeme}")
        }

        return left + right + listOf(opCode)
    }

    override fun visitLogical(expression: Expression.Logical): List<String> {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary): List<String> {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable): List<String> {
        TODO("Not yet implemented")
    }

    fun generate(expression: Expression): List<String> {
        return expression.accept(this)
    }
}