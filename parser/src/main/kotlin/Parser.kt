package com.pedrodev

class Parser(private val tokens: List<Token>) {

    private var current = 0

    fun parse(): List<Statement> {
        val statements = mutableListOf<Statement>()

        consume(TokenType.TK_PROGRAMA, "Bloco 'Programa' não encontrado.");
        consume(TokenType.TK_ABRE_CHAVE, "Esperado '{' apos 'Programa'.")

        while (!isAtEnd()) {
            declaration()?.let { statements.add(it) }
        }
        consume(TokenType.TK_FECHA_CHAVE, "Esperado '}' de encerramento do bloco Programa.")

        if (!isAtEnd()) {
            throw RuntimeException("Token inesperado após a expressão '${tokens[current].lexeme}'.")
        }
        return statements
    }

    private fun declaration(): Statement? {
        if (match(TokenType.TK_INTEIRO, TokenType.TK_CARACTER, TokenType.TK_REAL, TokenType.TK_CADEIA)) {
            return varDeclaration()
        }
        return null
    }

    private fun varDeclaration(): Statement {
        val type = previous()
        consume(TokenType.TK_IDENTIFICADOR, "Esperado nome da variavel após o tipo. Na linha ${type.line}")
        val name = previous()
        var expr: Expression? = null

        if (match(TokenType.TK_IGUAL)) {
            expr = expression()
        }
        return Statement.Var(name, type, expr)
    }


    private fun expression(): Expression {
        return or()
    }


    private fun or(): Expression {
        var expr = and()
        while (match(TokenType.TK_OU)) {
            val operator = previous()
            val right = and()
            expr = Expression.Logical(expr, operator, right)
        }
        return expr
    }

    private fun and(): Expression {
        var expr = bitWiseOr()
        while (match(TokenType.TK_E)) {
            val operator = previous()
            val right = bitWiseOr()
            expr = Expression.Logical(expr, operator, right)
        }
        return expr
    }

    private fun bitWiseOr(): Expression {
        var expr = bitWiseXor()
        while (match(TokenType.TK_BIT_OR)) {
            val operator = previous()
            val right = bitWiseXor()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }


    private fun bitWiseXor(): Expression {
        var expr = bitWiseAnd()
        while (match(TokenType.TK_BIT_XOR)) {
            val operator = previous()
            val right = bitWiseAnd()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun bitWiseAnd(): Expression {
        var expr = equality()
        while (match(TokenType.TK_BIT_AND)) {
            val operator = previous()
            val right = equality()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun equality(): Expression {
        var expr = comparison()
        while (match(TokenType.TK_DIFERENTE, TokenType.TK_IGUAL_IGUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun comparison(): Expression {
        var expr = shift()
        while (match(
                TokenType.TK_MAIOR,
                TokenType.TK_MAIOR_OU_IGUAL,
                TokenType.TK_MENOR,
                TokenType.TK_MENOR_OU_IGUAL
            )
        ) {
            val operator = previous()
            val right = shift()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }

    private fun shift(): Expression {
        var expr = term()
        while (match(TokenType.TK_BIT_SHIFT)) {
            val operator = previous()
            val right = term()
            expr = Expression.Binary(expr, operator, right)
        }
        return expr
    }


    private fun term(): Expression {
        var expr = factor()
        while (match(TokenType.TK_SOMA, TokenType.TK_SUBTRACAO)) {
            val operation = tokens[current - 1]
            val right = factor()
            expr = Expression.Binary(expr, operation, right)
        }
        return expr
    }

    private fun factor(): Expression {
        var expr = unary()
        while (match(TokenType.TK_MULTPLICACAO, TokenType.TK_DIVISAO, TokenType.TK_MODULO)) {
            val operation = tokens[current - 1]
            val right = unary()
            expr = Expression.Binary(expr, operation, right)
        }
        return expr
    }

    private fun unary(): Expression {
        if (match(TokenType.TK_BIT_NOT, TokenType.TK_NAO)) {
            val operator = previous()
            val right = unary()
            return Expression.Unary(operator, right)
        }
        return primary()
    }

    private fun primary(): Expression {
        if (match(TokenType.TK_ABRE_PARENTESE)) {
            val expr = expression()
            if (!match(TokenType.TK_FECHA_PARENTESE)) {
                throw RuntimeException("Esperado ')' depois da expressão")
            }
            return expr
        }

        if (match(TokenType.TK_VERDADEIRO_LITERAL)) return Expression.Literal(true)
        if (match(TokenType.TK_FALSO_LITERAL)) return Expression.Literal(false)

        if (match(TokenType.TK_NUMERO_REAL_LITERAL, TokenType.TK_NUMERO_INTEIRO_LITERAL, TokenType.TK_STRING_LITERAL)) {
            return Expression.Literal(previous().lexeme)
        }
        if (match(TokenType.TK_IDENTIFICADOR)) {
            return Expression.Variable(previous())
        }
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

    private fun consume(type: TokenType, msgError: String): Token {
        if (check(type)) return advance()
        throw RuntimeException(msgError) // TODO Substituir. Exibir linha e informações do token, e não lançar exceção, apenas exibir erro e encerrar o parser.

    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return tokens[current].type == type
    }
}