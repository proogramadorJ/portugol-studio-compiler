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
                    tokens.add(Token(TokenType.TK_VIRGULA, currentLine, currentColumn, ","))
                    currentColumn++
                }

                '{' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_ABRE_CHAVE, currentLine, currentColumn, "{"))
                    currentColumn++
                }

                '}' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_FECHA_CHAVE, currentLine, currentColumn, "}"))
                    currentColumn++
                }

                '(' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_ABRE_PARENTESE, currentLine, currentColumn, "("))
                    currentColumn++
                }

                ')' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_FECHA_PARENTESE, currentLine, currentColumn, ")"))
                    currentColumn++
                }

                '[' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_ABRE_COLCHETE, currentLine, currentColumn, "]"))
                    currentColumn++
                }

                ']' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_FECHA_COLCHETE, currentLine, currentColumn, "]"))
                    currentColumn++
                }

                ':' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_DOIS_PONTOS, currentLine, currentColumn, ":"))
                    currentColumn++
                }

                '&' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_AND, currentLine, currentColumn, "&"))
                    currentColumn++
                }

                '|' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_OR, currentLine, currentColumn, "|"))
                    currentColumn++
                }

                '~' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_BIT_NOT, currentLine, currentColumn, "~"))
                    currentColumn++
                }

                '<' -> {
                    if (next() == '<') {
                        tokens.add(Token(TokenType.TK_BIT_SHIFT, currentLine, currentColumn, "<<"))
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
                    tokens.add(Token(TokenType.TK_BIT_XOR, currentLine, currentColumn, "^"))
                    currentColumn++
                }

                '+' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_SOMA, currentLine, currentColumn, "+"))
                    currentColumn++
                }

                '-' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_SUBTRACAO, currentLine, currentColumn, "-"))
                    currentColumn++
                }

                '*' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_MULTPLICACAO, currentLine, currentColumn, "*"))
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
                        tokens.add(Token(TokenType.TK_DIVISAO, currentLine, currentColumn, "/"))
                        currentColumn++

                    }
                }

                '%' -> {
                    pos++
                    tokens.add(Token(TokenType.TK_MODULO, currentLine, currentColumn, "%"))
                    currentColumn++
                }

                ' ' -> {
                    pos++
                    currentColumn++
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
                    tokens.add(Token(TokenType.TK_STRING_LITERAL, currentLine, currentColumn, stringLiteral))
                }


                '\r' -> {pos++}
                '\t' -> {pos++}

                '\n' -> {
                    pos++
                    currentLine++
                    currentColumn = 1
                }

                else -> {
                    if (isDigit(currentChar)) {
                        val begin = pos
                        while (isDigit(peek())) pos++

                        if (sourceCode[pos] == '.' && isDigit(next())) {
                            pos++
                            while (isDigit(peek())) pos++
                        }
                        val literalNumber = sourceCode.substring(begin, pos)
                        tokens.add(Token(TokenType.TK_NUMERO_LITERAL, currentLine, currentColumn, literalNumber))

                    } else if (isAlpha(currentChar)) {
                        val begin = pos
                        while (isAlphaNumeric(peek())) pos++

                        val literalValue = sourceCode.substring(begin, pos)
                        val reservedWord = ReservedWords.reservedWords[literalValue.lowercase()]
                        val type = reservedWord ?: TokenType.TK_IDENTIFICADOR
                        tokens.add(Token(type, currentLine, currentColumn, literalValue))

                    } else {
                        //TODO No futuro apenas exibir mensagem de erro e interromper scan
                        throw RuntimeException("Simbolo [$currentChar] não identificado na linha $currentLine coluna $currentColumn")
                    }
                }
            }

        }

        tokens.add(Token(TokenType.EOF, -1, -1, ""))
        return tokens
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
            tokens.add(Token(type1, currentLine, currentColumn, "" + lexeme + c))
            currentColumn += 2
            pos += 2
            return
        }
        tokens.add(Token(type2, currentLine, currentColumn, "" + lexeme))
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
}