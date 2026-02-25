import types.BoolType
import types.CaracterType
import types.IntType
import types.RealType
import types.StringType
import types.TokenType
import types.Type

object TokenTypeConverter {
    fun internalTypeFromTokenType(tokenType: TokenType): Type {
        return when (tokenType) {
            TokenType.TK_INTEIRO -> IntType
            TokenType.TK_REAL -> RealType
            TokenType.TK_LOGICO -> BoolType
            TokenType.TK_CARACTER -> CaracterType
            TokenType.TK_CADEIA -> StringType
            else -> throw RuntimeException("Não existe tipo interno mapeado para '${tokenType.name}' ")// TODO Semantic Analyser exception
        }
    }
}