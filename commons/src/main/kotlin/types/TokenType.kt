package types

enum class TokenType {

    TK_PROGRAMA, //programa
    TK_FUNCAO, // funcao
    TK_SE, // se
    TK_SENAO, // senao
    TK_IDENTIFICADOR, // nome de variavel ou função
    TK_ESCOLHA, // escolha
    TK_CASO, // caso
    TK_CASO_CONTRARIO, // caso contrario
    TK_ENQUANTO, // enquanto

    // TODO incluir do-while/ faca{ }enquanto(condicao)
    TK_PARA, // para
    TK_CONST, // const

    //valores literais
    TK_NUMERO_REAL_LITERAL,
    TK_NUMERO_INTEIRO_LITERAL,
    TK_CHAR_LITERAL,
    TK_STRING_LITERAL,
    TK_VERDADEIRO_LITERAL,
    TK_FALSO_LITERAL,


    //tipos
    TK_INTEIRO, // inteiro
    TK_REAL, // real
    TK_CARACTER, // caracter
    TK_CADEIA, // cadeia
    TK_LOGICO, // logico
    TK_VAZIO, // vazio
    TK_CONSTANTE, // constante

    TK_SOMA, // +
    TK_SUBTRACAO, // -
    TK_MULTPLICACAO, // *
    TK_DIVISAO, // /
    TK_MODULO, // %

    TK_MAIOR, // >
    TK_MENOR, // <
    TK_MAIOR_OU_IGUAL, // >=
    TK_MENOR_OU_IGUAL, //<=
    TK_IGUAL, // =
    TK_IGUAL_IGUAL, // ==
    TK_DIFERENTE, // !=

    TK_E, // e
    TK_OU, // ou
    TK_NAO, //nao

    TK_BIT_AND, // &
    TK_BIT_OR, // |
    TK_BIT_NOT, // ~
    TK_BIT_XOR, // ^
    TK_BIT_SHIFT, // <<

    TK_VIRGULA, // ,
    TK_ABRE_CHAVE, //{
    TK_FECHA_CHAVE, // }
    TK_ABRE_PARENTESE, // (
    TK_FECHA_PARENTESE, // )
    TK_ABRE_COLCHETE, // [
    TK_FECHA_COLCHETE, // ]
    TK_DOIS_PONTOS, // :

    EOF
}