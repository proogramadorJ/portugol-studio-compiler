package com.pedrodev

object ReservedWords {
    val reservedWords = mutableMapOf<String, TokenType>(
        "programa" to TokenType.TK_PROGRAMA,
        "funcao" to TokenType.TK_FUNCAO,
        "se" to TokenType.TK_SE,
        "senao" to TokenType.TK_SENAO,
        "escolha" to TokenType.TK_ESCOLHA,
        "caso" to TokenType.TK_CASO,
        "contrario" to TokenType.TK_CASO_CONTRARIO,
        "enquanto" to TokenType.TK_ENQUANTO,
        "para" to TokenType.TK_PARA,
        "inteiro" to TokenType.TK_INTEIRO,
        "real" to TokenType.TK_REAL,
        "caracter" to TokenType.TK_CARACTER,
        "cadeia" to TokenType.TK_CADEIA,
        "logico" to TokenType.TK_LOGICO,
        "vazio" to TokenType.TK_VAZIO, // É UMA PALAVRA RESERVADA MESMO OU SÓ É USADA QUANDO O TIPO DE RETORNO DA FUNÇÃO É OMITIDO
        "constante" to TokenType.TK_CONSTANTE,
        "e" to TokenType.TK_E,
        "ou" to TokenType.TK_OU,
        "nao" to TokenType.TK_NAO,
        "const" to TokenType.TK_CONST,
        "verdadeiro" to TokenType.TK_VERDADEIRO_LITERAL,
        "falso" to TokenType.TK_FALSO_LITERAL
    )

}