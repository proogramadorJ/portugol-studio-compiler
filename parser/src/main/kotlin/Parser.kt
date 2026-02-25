package com.pedrodev

import types.TokenType

/**
 * Analise sintatica
 * Verifica se a sequencia de tokens está de acordo com a gramatica da linguagem.
 * Utiliza um parser descendente recursivo para gerar a AST
 */
class Parser(private val tokens: List<Token>) {

    private var current = 0

    fun parse(): List<Statement> {
        return programa().statements
    }

    private fun programa(): ProgramaSttm {
        val progr = ProgramaSttm(mutableListOf())

        consume(TokenType.TK_PROGRAMA, "Bloco 'programa' não encontrado.");
        consume(TokenType.TK_ABRE_CHAVE, "Esperado '{' apos 'Programa'.")

        while (!check(TokenType.TK_FECHA_CHAVE) && !isAtEnd()) {
            topLevelDeclaration()?.let { progr.statements.add(it) }
        }
        consume(TokenType.TK_FECHA_CHAVE, "Esperado '}' de encerramento do bloco Programa.")

        if (!isAtEnd()) {
            throw RuntimeException("Token inesperado após a expressão '${tokens[current].lexeme}'.")
        }
        println("Análise Sintatica- OK")
        return progr
    }

    private fun topLevelDeclaration(): Statement? {
        if (match(
                TokenType.TK_INTEIRO,
                TokenType.TK_CARACTER,
                TokenType.TK_REAL,
                TokenType.TK_CADEIA,
                TokenType.TK_LOGICO
            )
        ) {
            return varDeclaration()
        }

        if (match(TokenType.TK_FUNCAO)) {
            return funcDeclaration()
        }
        throw RuntimeException("É permitido somente declaração de variáveis funções no bloco Programa. Encontrado Token ${tokens[current].lexeme} ")
    }

    private fun funcDeclaration(): Statement {
        var returnType = Token(TokenType.TK_VAZIO, -1, -1, "", "")

        if (match(
                TokenType.TK_VAZIO,
                TokenType.TK_INTEIRO,
                TokenType.TK_REAL,
                TokenType.TK_CARACTER,
                TokenType.TK_REAL,
                TokenType.TK_CADEIA
            )
        ) {
            returnType = previous()
        }

        consume(TokenType.TK_IDENTIFICADOR, "Esperado nome da função.")
        val name = previous()
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após nome da função.")
        val parameters = mutableListOf<Param>()

        if (!check(TokenType.TK_FECHA_PARENTESE)) {
            do {
                if (parameters.size >= 255) {
                    throw RuntimeException("Função não pode ter mais de 255 parâmetros.")
                }
                if (!match(
                        TokenType.TK_INTEIRO,
                        TokenType.TK_REAL,
                        TokenType.TK_LOGICO,
                        TokenType.TK_CADEIA,
                        TokenType.TK_CARACTER
                    )
                ) {
                    throw RuntimeException("Tipo do parametro esperado.")
                }
                val type = previous()
                parameters.add(Param(type, consume(TokenType.TK_IDENTIFICADOR, "Nome do parâmetro esperado.")))
            } while (match(TokenType.TK_VIRGULA))
        }

        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' após os parâmetros da função.")
        consume(TokenType.TK_ABRE_CHAVE, "Esperado '{' antes do corpo da função.")

        val body = block()
        // A chave de fechamento é consumida em block()

        return Statement.Function(name, returnType, parameters, body, null)
    }

    private fun block(): List<Statement> {
        val statements = mutableListOf<Statement>()

        while (!check(TokenType.TK_FECHA_CHAVE) && !isAtEnd()) {
            statement()?.let { statements.add(it) }
        }
        consume(TokenType.TK_FECHA_CHAVE, "Esperado '}' depois do  bloco.")
        return statements
    }

    private fun statement(): Statement {
        return when {
            match(
                TokenType.TK_INTEIRO,
                TokenType.TK_CARACTER,
                TokenType.TK_REAL,
                TokenType.TK_CADEIA,
                TokenType.TK_LOGICO
            ) -> {
                varDeclaration()
            }

            match(TokenType.TK_SE) -> return ifStatement()
            match(TokenType.TK_ENQUANTO) -> return whileStatement()
            match(TokenType.TK_ABRE_CHAVE) -> return Statement.Block(block())

            else -> {
                return expressionStatement()
            }

        }
    }

    private fun whileStatement(): Statement.While {
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'enquanto'.")
        val condition = expression()
        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' depois da condição.")
        val body = statement()
        return Statement.While(condition, body)
    }

    private fun ifStatement(): Statement {
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'se'.")
        val condition = expression()
        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' após condição do comando 'se'.")
        val thenBranch = statement()
        val elseBranch = if (match(TokenType.TK_SENAO)) statement() else null
        return Statement.If(condition, thenBranch, elseBranch)
    }

    private fun varDeclaration(): Statement {
        val type = previous()
        consume(TokenType.TK_IDENTIFICADOR, "Esperado nome da variavel após o tipo. Na linha ${type.line}.")
        val name = previous()
        var expr: Expression? = null

        //TODO o initializer de uma variavel pode ser o retorno de uma chamada função
        // EX: inteiro diaAtual = getDiaAtual()
        if (match(TokenType.TK_IGUAL)) {
            expr = expression()
        }
        return Statement.VarDeclaration(name, type, expr, null)
    }

    private fun expressionStatement(): Statement {
        val expr = expression()
        return Statement.ExprStatement(expr)
    }

    private fun expression(): Expression {
        return assignment()
    }

    private fun assignment(): Expression {
        val expr = or()
        if (match(TokenType.TK_IGUAL)) {
            val equals = previous()
            val value = assignment()
            when (expr) {
                is Expression.Variable -> {
                    val name = expr.name
                    return Expression.Assign(name, value, null)
                }

                else -> {
                    throw RuntimeException("Alvo de atribuição inválido. Linha ${equals.line}")
                }
            }
        }
        return expr
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

        if (match(TokenType.TK_VERDADEIRO_LITERAL)) return Expression.Literal(true, previous().type)
        if (match(TokenType.TK_FALSO_LITERAL)) return Expression.Literal(false, previous().type)

        if (match(
                TokenType.TK_NUMERO_REAL_LITERAL,
                TokenType.TK_NUMERO_INTEIRO_LITERAL,
                TokenType.TK_STRING_LITERAL,
                TokenType.TK_CHAR_LITERAL
            )
        ) {
            return Expression.Literal(previous().lexeme, previous().type)
        }
        //Ainda não suporta chamada de funções
        if (match(TokenType.TK_IDENTIFICADOR)) {
            return Expression.Variable(previous(), null)
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
        return tokens[current].type == TokenType.EOF
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
        throw RuntimeException(msgError)
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return tokens[current].type == type
    }
}