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

    fun parse2(): List<Statement> {
        val statements: MutableList<Statement> = mutableListOf()

        while (!isAtEnd()) {
            when {
                match(
                    TokenType.TK_INTEIRO,
                    TokenType.TK_CARACTER,
                    TokenType.TK_REAL,
                    TokenType.TK_CADEIA,
                    TokenType.TK_LOGICO
                ) -> {
                    statements.add(declaration())
                }

                match(TokenType.TK_CONST) -> statements.add(varDeclaration())
                match(TokenType.TK_FUNCAO) -> statements.add(funcDeclaration())
                match(TokenType.TK_SE) -> statements.add(ifStatement())
                match(TokenType.TK_ENQUANTO) -> statements.add(whileStatement())
                match(TokenType.TK_FACA) -> statements.add(doWhileStatement())
                match(TokenType.TK_PARA) -> statements.add(forStatement())
                match(TokenType.TK_ESCOLHA) -> statements.add(switchStatement())
                match(TokenType.TK_ABRE_CHAVE) -> statements.add(Statement.Block(block()))
                match(TokenType.TK_RETURNE) -> statements.add(
                    Statement.Return(
                        previous(),
                        expression()
                    )
                )

                else -> statements.add(expressionStatement())
            }

        }
        return statements
    }

    private fun programa(): ProgramaSttm {
        val progr = ProgramaSttm(mutableListOf())

        consume(TokenType.TK_PROGRAMA, "Bloco 'programa' não encontrado.")
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
                TokenType.TK_LOGICO,
            )
        ) {
            return declaration()
        }

        if(match(TokenType.TK_CONST)) {
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
                parameters.add(
                    Param(
                        type,
                        consume(TokenType.TK_IDENTIFICADOR, "Nome do parâmetro esperado."),
                        null
                    )
                )
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
            statement().let { statements.add(it) }
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
                TokenType.TK_LOGICO,
            ) -> {
                declaration()
            }

            match(TokenType.TK_CONST) -> varDeclaration()
            match(TokenType.TK_SE) -> ifStatement()
            match(TokenType.TK_ENQUANTO) -> whileStatement()
            match(TokenType.TK_FACA) -> doWhileStatement()
            match(TokenType.TK_PARA) -> forStatement()
            match(TokenType.TK_ESCOLHA) -> switchStatement()
            match(TokenType.TK_ABRE_CHAVE) -> Statement.Block(block())
            match(TokenType.TK_RETURNE) -> returnStatement()

            else -> {
                expressionStatement()
            }

        }
    }

    private fun returnStatement(): Statement.Return {
        val keyword = previous()
        val value: Expression = expression()
        return Statement.Return(keyword, value)
    }

    private fun whileStatement(): Statement.While {
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'enquanto'.")
        val condition = expression()
        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' depois da condição.")
        val body = statement()
        return Statement.While(condition, body)
    }

    private fun doWhileStatement(): Statement.DoWhile {
        val body = statement()
        consume(TokenType.TK_ENQUANTO, "Esperado 'enquanto' após o corpo do laço.")
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'enquanto'.")
        val condition = expression()
        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' depois da condição.")
        return Statement.DoWhile(condition, body)
    }

    private fun forStatement(): Statement.For {
        var initializer: Expression? = null
        var varDeclarationInitializer: Statement? = null

        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'para'.")

        if (match(
                TokenType.TK_INTEIRO,
                TokenType.TK_REAL,
                TokenType.TK_CARACTER,
                TokenType.TK_CADEIA,
                TokenType.TK_LOGICO,
                TokenType.TK_CONST
            )
        ) {
            varDeclarationInitializer = varDeclaration()
        } else {
            initializer = expression()
        }
        consume(TokenType.TK_PONTO_E_VIRGULA, "Esperado ';' após a inicialização do laço.")

        val condition = expression()
        consume(TokenType.TK_PONTO_E_VIRGULA, "Esperado ';' após a condição do laço.")

        val increment = expression()
        consume(
            TokenType.TK_FECHA_PARENTESE,
            "Esperado ')' depois da expressão de incremento/decremento do laço. 'para'"
        )

        val body = statement()
        return Statement.For(initializer, varDeclarationInitializer, condition, increment, body)
    }

    private fun switchStatement(): Statement {
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'escolha'.")
        val expr = expression()
        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' após a expressão do comando 'escolha'.")

        consume(TokenType.TK_ABRE_CHAVE, "Esperado '{' após a expressão do comando 'escolha'.")

        val cases = mutableListOf<Pair<Expression, Statement>>()
        var defaultCase: Statement? = null


        while (!check(TokenType.TK_FECHA_CHAVE) && !isAtEnd()) {

            consume(TokenType.TK_CASO, "Esperado comando 'caso' ou 'caso contrario'")

            if (match(TokenType.TK_CASO_CONTRARIO)) {
                if (defaultCase != null) {
                    throw ParseException("Somente um comando 'caso contrario' é permitido.")
                }

                consume(TokenType.TK_DOIS_PONTOS, "Esperado ':' depois de 'caso contrario'.")
                val statements = mutableListOf<Statement>()
                while (!check(TokenType.TK_CASO) && !check(TokenType.TK_FECHA_CHAVE) && !isAtEnd()) {
                    statements.add(statement())
                }

                defaultCase = if (statements.size == 1) {
                    statements[0]
                } else {
                    Statement.Block(statements)
                }
            } else {
                val caseExpression = expression()
                consume(TokenType.TK_DOIS_PONTOS, "Esperado ':' depois do valor do caso.")
                val statements = mutableListOf<Statement>()

                while (!check(TokenType.TK_CASO) && !check(TokenType.TK_CASO_CONTRARIO) && !check(
                        TokenType.TK_FECHA_CHAVE
                    ) && !isAtEnd()
                ) {
                    statements.add(statement())

                    val caseBody = if (statements.size == 1) {
                        statements[0]
                    } else {
                        Statement.Block(statements)
                    }
                    cases.add(Pair(caseExpression, caseBody))
                }
            }
        }

        consume(TokenType.TK_FECHA_CHAVE, "Esperado '}' depois dos casos.")

        return Statement.Switch(expr, cases, defaultCase)
    }

    private fun ifStatement(): Statement {
        consume(TokenType.TK_ABRE_PARENTESE, "Esperado '(' após comando 'se'.")
        val condition = expression()
        consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' após condição do comando 'se'.")
        val thenBranch = statement()
        val elseBranch = if (match(TokenType.TK_SENAO)) statement() else null
        return Statement.If(condition, thenBranch, elseBranch)
    }

    private fun declaration(): Statement {
        val type = previous()

        consume("Esperado identificador após ${type.lexeme}", TokenType.TK_IDENTIFICADOR)

        if (match(TokenType.TK_ABRE_COLCHETE)) {
            return arrayDeclaration()
        }
        current--
        return varDeclaration()
    }

    private fun varDeclaration(): Statement {
        var type = previous()
        var isConst = false

        if (type.type == TokenType.TK_CONST) {
            type = consume(
                "Esperado o tipo da variavel após 'const' na linha ${type.line}.",
                TokenType.TK_INTEIRO,
                TokenType.TK_REAL,
                TokenType.TK_CARACTER,
                TokenType.TK_CADEIA,
                TokenType.TK_LOGICO
            )
            isConst = true
        }

        consume(
            TokenType.TK_IDENTIFICADOR,
            "Esperado nome da variavel após o tipo. Na linha ${type.line}."
        )
        val name = previous()
        var expr: Expression? = null

        if (match(TokenType.TK_IGUAL)) {
            expr = expression()
        }
        return Statement.VarDeclaration(
            name,
            type,
            expr,
            null,
            isConst
        )
    }

    /**
     * inteiro vetor[5]
     * caracter vetor2[200]
     *
     * //vetores inicializados
     * real vetor3[2] = {1.4,2.5}
     * logico vetor4[4] = {verdadeiro,falso,verdadeiro,verdadeiro}
     * cadeia vetor5[] = {"Questão","Fundamental"}
     *
     * //Mudando o valor do vetor5 na posição 0 de "Questão" para "Pergunta"
     * vetor5[0] = "Pergunta"
     */
    private fun arrayDeclaration(): Statement {
        //current = O que vem depois de '['
        //current - 2 = identificador
        //current -3 = tipo

        val type = tokens[current - 3]
        val name = tokens[current - 2]
        var declaredSize : Int ? = null
        val initializationValues = mutableListOf<Expression>()
        var size = 0


        if (match(TokenType.TK_NUMERO_INTEIRO_LITERAL)) {
            size = previous().lexeme.toInt()
            declaredSize = size
        }

        consume("Esperado ']' mas encontrou '${peek().lexeme}'", TokenType.TK_FECHA_COLCHETE)

        if (size == 0) { //Obrigatoria initializacao
            consume(
                "Obrigatoria a initialização do Vetor quando o tamanho não é especificado na declaração.",
                TokenType.TK_IGUAL
            )
            consume("Esperado '{' mas encontrou '${peek().lexeme}' ", TokenType.TK_ABRE_CHAVE)
            do {
                initializationValues.add(expression())
            } while (match(TokenType.TK_VIRGULA))

            consume("Esperado '}' mas encontrou '${peek().lexeme}' ", TokenType.TK_FECHA_CHAVE)

            return Statement.ArrayDeclaration(
                type,
                name,
                declaredSize,
                Expression.Literal(initializationValues.size, TokenType.TK_NUMERO_INTEIRO_LITERAL),
                initializationValues,
                null
            )
        }

        if (match(TokenType.TK_IGUAL)) {
            consume("Esperado '{' mas encontrou '${peek().lexeme}' ", TokenType.TK_ABRE_CHAVE)
            do {
                initializationValues.add(expression())
            } while (match(TokenType.TK_VIRGULA))

            consume("Esperado '}' mas encontrou '${peek().lexeme}' ", TokenType.TK_FECHA_CHAVE)
        }

        return Statement.ArrayDeclaration(
            type,
            name,
            declaredSize,
            Expression.Literal(initializationValues.size, TokenType.TK_NUMERO_INTEIRO_LITERAL),
            initializationValues,
            null
        )
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
        if (match(TokenType.TK_BIT_NOT, TokenType.TK_NAO, TokenType.TK_SUBTRACAO)) {
            val operator = previous()
            val right = unary()
            return Expression.Unary(operator, right)
        }
        return call()
    }

    private fun call(): Expression {
        var expr = primary()
        if (match(TokenType.TK_ABRE_PARENTESE)) {
            expr = finishCall(expr)
        }
        return expr
    }

    private fun finishCall(callee: Expression): Expression {
        val arguments = mutableListOf<Expression>()
        if (!check(TokenType.TK_FECHA_PARENTESE)) {
            do {
                if (arguments.size >= 255) {
                    throw RuntimeException("Função não pode ter mais de 255 argumentos.")
                }
                arguments.add(expression())
            } while (match(TokenType.TK_VIRGULA))
        }
        val paren = consume(TokenType.TK_FECHA_PARENTESE, "Esperado ')' depois de argumentos.")
        return Expression.Call(callee, paren, arguments, null)
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

        if (match(TokenType.TK_NUMERO_INTEIRO_LITERAL)) return Expression.Literal(
            previous().lexeme.toInt(),
            previous().type
        )
        if (match(TokenType.TK_NUMERO_REAL_LITERAL)) return Expression.Literal(
            previous().lexeme.toDouble(),
            previous().type
        )
        if (match(TokenType.TK_CHAR_LITERAL)) return Expression.Literal(
            previous().lexeme[0],
            previous().type
        )
        if (match(TokenType.TK_STRING_LITERAL)) return Expression.Literal(
            previous().lexeme,
            previous().type
        )

        if (match(TokenType.TK_IDENTIFICADOR)) {
            val name = previous()
            if(match(TokenType.TK_ABRE_COLCHETE)){
                return arrayAccessExpr(name)
            }
            return Expression.Variable(previous(), null)
        }
        throw ParseException("Esperado expressão, mas encontrou: ${tokens.getOrNull(current)?.lexeme ?: "EOF"} ")
    }

    private fun arrayAccessExpr(name: Token): Expression {
       val index = consume("Esperado índice do vetor, mas encontrou '${peek().lexeme}'", TokenType.TK_NUMERO_INTEIRO_LITERAL)
       consume("Esperado ']' , mas encontrou '${peek().lexeme}'", TokenType.TK_FECHA_COLCHETE)

        return Expression.ArrayAccess(
            name,
            index.lexeme.toInt(),
            null
        )
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

    private fun consume(msgError: String, vararg types: TokenType): Token {
        for (type in types) {
            if (check(type)) return advance()
        }
        throw RuntimeException(msgError)
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return tokens[current].type == type
    }

    private fun peek(): Token {
        return tokens[current]
    }
}