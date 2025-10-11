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
        return or()
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
        var expr = unary()
        while (match(TokenType.TK_MULTPLICACAO, TokenType.TK_DIVISAO, TokenType.TK_MODULO)) {
            val operation = tokens[current - 1]
            val right = unary()
            expr = Expr.Binary(expr, operation, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.TK_BIT_NOT)) { // TODO confirmar, mas acho que esse dialeto portugol não tem operadores unarios tipo -4, !verdadeiro
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return primary()
    }

    private fun or(): Expr {
        var expr = and()

        while (match(TokenType.TK_OU)) {
            val operator = previous()
            val right = and()
            expr = Expr.Logical(expr, operator, right)
        }
        return expr
    }

    private fun and(): Expr {
        var expr = equality()

        while (match(TokenType.TK_E)) {
            val operator = previous()
            val right = equality()
            expr = Expr.Logical(expr, operator, right)
        }
        return expr
    }

    private fun equality(): Expr {
        var expr = comparison()

        while (match(TokenType.TK_DIFERENTE, TokenType.TK_IGUAL_IGUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun comparison(): Expr {
        var expr = term()

        while (match(
                TokenType.TK_MAIOR,
                TokenType.TK_MAIOR_OU_IGUAL,
                TokenType.TK_MENOR,
                TokenType.TK_MENOR_OU_IGUAL
            )
        ) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
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

        if (match(TokenType.TK_VERDADEIRO)) return Expr.Literal(true)
        if (match(TokenType.TK_FALSO)) return Expr.Literal(false)
        if (match(TokenType.TK_NUMERO_LITERAL, TokenType.TK_STRING_LITERAL)) return Expr.Literal(previous().lexeme)
        throw RuntimeException("Esperado expressão, mas encontrou: ${tokens.getOrNull(current)?.lexeme ?: "EOF"} ")
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

    private fun previous(): Token {
        return tokens[current - 1]
    }
}