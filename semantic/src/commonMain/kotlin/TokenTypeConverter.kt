import exception.SemanticException
import types.*

object TokenTypeConverter {
    fun internalTypeFromTokenType(tokenType: TokenType): Type {
        return when (tokenType) {
            TokenType.TK_INTEIRO -> IntType
            TokenType.TK_REAL -> RealType
            TokenType.TK_LOGICO -> BoolType
            TokenType.TK_CARACTER -> CaracterType
            TokenType.TK_CADEIA -> StringType
            TokenType.TK_VAZIO -> VoidType
            else -> throw SemanticException("Não existe tipo interno mapeado para '${tokenType.name}' ") // TODO trocar para uma Execption personalizada mais generica
        }
    }
}