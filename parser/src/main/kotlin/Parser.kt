package com.pedrodev

class Parser(private val tokens: List<Token>) {

    private var current = 0

    fun parse(): Expr {
        val expr = expression();
        if (!isAtEnd()) {
            throw RuntimeException("Token inesperado após a expressão '${tokens[current].lexeme}'")
        }

        return expr
    }

    private fun expression(): Expr {
        return term()
    }

    private fun term(): Expr {
        var expr = factor()
        while (match(TokenType.TK_SOMA, TokenType.TK_SUBTRACAO)) {
            val operation = tokens[current - 1]
            val right = factor()
            expr = Expr.Binary(expr, operation, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = primary()
        while (match(TokenType.TK_MULTPLICACAO, TokenType.TK_DIVISAO)) {
            val operation = tokens[current - 1]
            val right = primary()
            expr = Expr.Binary(expr, operation, right)
        }

        return expr
    }

    private fun primary(): Expr {
        if (match(TokenType.TK_ABRE_PARENTESE)) {
            val expr = expression()
            if (!match(TokenType.TK_FECHA_PARENTESE)) {
                throw RuntimeException("Esperado ')' depois da expressão")
            }
            return expr
        }

        if (isAtEnd() || tokens[current].type != TokenType.TK_NUMERO_LITERAL) {
            throw RuntimeException("Esperado um número, mas encontrou: ${tokens.getOrNull(current)?.lexeme ?: "EOF"} ")
        }

        val token = tokens[current]
        advance()
        return Expr.Literal(token.lexeme.toDouble())
    }

    private fun match(vararg types: TokenType): Boolean {
        for (tokenType in types) {
            if (!isAtEnd() && tokens[current].type == tokenType) {
                advance()
                return true
            }
        }
        return false
    }

    private fun isAtEnd(): Boolean {
        return current >= tokens.size || tokens[current].type == TokenType.EOF
    }

    private fun advance(): Token {
        current++
        return tokens[current - 1]
    }
}