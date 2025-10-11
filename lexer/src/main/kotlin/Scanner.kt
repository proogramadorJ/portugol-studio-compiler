package com.pedrodev

class Scanner(source: String) {

    private val tokens = mutableListOf<Token>()
    private var pos: Int = 0
    private var sourceCode: String = source
    private var currentLine = 1
    private var currentColumn = 0

    fun scanTokens(): List<Token> {
        while (pos < sourceCode.length) {
            when (val currentChar = sourceCode[pos]) {
                ',' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_VIRGULA, currentLine, currentColumn, ",", null))
                    currentColumn++
                }

                '{' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_ABRE_CHAVE, currentLine, currentColumn, "{", null))
                    currentColumn++
                }

                '}' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_FECHA_CHAVE, currentLine, currentColumn, "}", null))
                    currentColumn++
                }

                '(' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_ABRE_PARENTESE, currentLine, currentColumn, "(", null))
                    currentColumn++
                }

                ')' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_FECHA_PARENTESE, currentLine, currentColumn, ")", null))
                    currentColumn++
                }

                '[' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_ABRE_COLCHETE, currentLine, currentColumn, "]", null))
                    currentColumn++
                }

                ']' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_FECHA_COLCHETE, currentLine, currentColumn, "]", null))
                    currentColumn++
                }

                ':' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_DOIS_PONTOS, currentLine, currentColumn, ":", null))
                    currentColumn++
                }

                '&' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_AND, currentLine, currentColumn, "&", null))
                    currentColumn++
                }

                '|' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_OR, currentLine, currentColumn, "|", null))
                    currentColumn++
                }

                '~' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_NOT, currentLine, currentColumn, "~", null))
                    currentColumn++
                }

                '<' -> {
                    if (next() == '<') {
                        tokens.add(Token(TokenType.TK_BIT_SHIFT, currentLine, currentColumn, "<<", null))
                        pos += 2
                        currentColumn += 2
                    } else {
                        match('=', TokenType.TK_MENOR_OU_IGUAL, TokenType.TK_MENOR, '<')
                    }
                }

                '>' -> {
                    match('=', TokenType.TK_MAIOR_OU_IGUAL, TokenType.TK_MAIOR, '>')
                }

                '!' -> {
                    match('=', TokenType.TK_DIFERENTE, TokenType.TK_IGUAL, '!')
                }

                '=' -> {
                    match('=', TokenType.TK_IGUAL_IGUAL, TokenType.TK_IGUAL, '=')
                }

                '^' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_XOR, currentLine, currentColumn, "^", null))
                    currentColumn++
                }

                '+' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_SOMA, currentLine, currentColumn, "+", null))
                    currentColumn++
                }

                '-' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_SUBTRACAO, currentLine, currentColumn, "-", null))
                    currentColumn++
                }

                '*' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_MULTPLICACAO, currentLine, currentColumn, "*", null))
                    currentColumn++
                }

                '/' -> {
                    if (next() == '/') {
                        while (pos < sourceCode.length && sourceCode[pos] != '\n') {
                            pos++
                        }
                        if (pos < sourceCode.length) {
                            currentLine++
                            currentColumn = 1
                            pos++
                        }
                    } else {
                        pos++
                        tokens.add(Token(TokenType.TK_DIVISAO, currentLine, currentColumn, "/", null))
                        currentColumn++

                    }
                }

                '%' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_MODULO, currentLine, currentColumn, "%", null))
                    currentColumn++
                }

                ' ' -> {
                    pos++
                    currentColumn++
                }

                '\'' -> {
                    pos++ // consome aspa simples
                    val c = next() // pega o caracter
                    pos++ // teroricamente dever a aspa simples de fechamento
                    if (isAtEnd() || peek() != '\'')
                        throw RuntimeException("Caracter não finalizado corretamente na linha $currentLine")
                    pos++
                }

                '"' -> {
                    val begin = pos
                    pos++
                    while (peek() != '"' && peek() != '\u0000') {
                        if (peek() == '\n') {
                            currentLine++
                        }
                        pos++
                    }
                    if (peek() == '\u0000') {
                        throw RuntimeException("string não finalizada corretamente na linha $currentLine")
                    }

                    pos++
                    val stringLiteral = sourceCode.substring(begin + 1, pos - 1)
                    tokens.add(
                        Token(
                            TokenType.TK_STRING_LITERAL,
                            currentLine,
                            currentColumn,
                            stringLiteral,
                            stringLiteral
                        )
                    ) // TODO fazer scape na string?
                }


                '\r' -> {
                    pos++
                }

                '\t' -> {
                    pos++
                }

                '\n' -> {
                    pos++
                    currentLine++
                    currentColumn = 1
                }

                else -> {
                    if (isDigit(currentChar)) {
                        val begin = pos
                        while (isDigit(peek())) pos++

                        if (pos < sourceCode.length && sourceCode[pos] == '.' && isDigit(next())) {
                            pos++
                            while (isDigit(peek())) pos++
                        }
                        val literalNumber = sourceCode.substring(begin, pos)

                        try {

                            val literalValue = literalNumber.toInt()

                            tokens.add(
                                Token(
                                    TokenType.TK_NUMERO_INTEIRO_LITERAL,
                                    currentLine,
                                    currentColumn,
                                    literalNumber,
                                    literalValue
                                )
                            )
                        } catch (ex: NumberFormatException) {
                            tokens.add(
                                Token(
                                    TokenType.TK_NUMERO_REAL_LITERAL,
                                    currentLine,
                                    currentColumn,
                                    literalNumber,
                                    literalNumber.toDouble()
                                )
                            )
                        }

                    } else if (isAlpha(currentChar)) {
                        val begin = pos
                        while (isAlphaNumeric(peek())) pos++

                        val literalValue = sourceCode.substring(begin, pos)
                        val reservedWord = ReservedWords.reservedWords[literalValue.lowercase()]
                        val type = reservedWord ?: TokenType.TK_IDENTIFICADOR
                        tokens.add(Token(type, currentLine, currentColumn, literalValue, null))

                    } else {
                        //TODO No futuro apenas exibir mensagem de erro e interromper scan
                        throw RuntimeException("Simbolo [$currentChar] não identificado na linha $currentLine coluna $currentColumn")
                    }
                }
            }

        }

        tokens.add(Token(TokenType.EOF, -1, -1, "", null))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return pos >= sourceCode.length
    }

    private fun peek(): Char {
        if (pos >= sourceCode.length) {
            return '\u0000'
        }
        return sourceCode[pos]
    }

    private fun next(): Char {
        if (pos + 1 >= sourceCode.length) {
            return '\u0000'
        }
        return sourceCode[pos + 1]
    }

    private fun match(c: Char, type1: TokenType, type2: TokenType, lexeme: Char) {
        if (next() == c) {
            tokens.add(Token(type1, currentLine, currentColumn, "" + lexeme + c, null))
            currentColumn += 2
            pos += 2
            return
        }
        tokens.add(Token(type2, currentLine, currentColumn, "" + lexeme, null))
        pos++
        currentColumn
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_'
    }

    private fun advance(): Char {
        if (isAtEnd()) throw RuntimeException("Fim inesperado de arquivo na linha $currentLine")
        val ch = sourceCode[pos]
        pos++
        return ch
    }
}