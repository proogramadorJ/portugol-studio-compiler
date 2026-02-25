package symbols

import TokenTypeConverter
import com.pedrodev.Token
import exception.SemanticException
import types.StorageKind
import types.SymbolKind
import types.Type
import java.util.*

class SymbolTable {
    private val globals = mutableMapOf<String, Symbol>()
    private var scopes: Stack<MutableMap<String, Symbol>> = Stack()
    private var globalIndex: Int = 0
    private var localIndex: Stack<Int> = Stack()

    fun defineFunction(name: String, paramsType: List<Token>, returnType: Token): Symbol {
        if (globals.containsKey(name)) {
            throw RuntimeException("Função '$name' já declarada.")
        }
        val types = paramsType.map { token -> TokenTypeConverter.internalTypeFromTokenType(token.type) }

        val symbol = FunctionSymbol(
            name = name,
            parametersType = types,
            returnType = TokenTypeConverter.internalTypeFromTokenType(returnType.type), // TODO funções do tipo void vai quebrar -> adicionar no mapa um tipo interno para void
            entryPoint = null,
        )

        globals[name] = symbol
        return symbol
    }

//    fun defineLocal(name: String, type: Type): Symbol {
//        if(scopes.empty()){
//            throw
//        }
//    }

    fun defineGlobal(name: String, type: Token): Symbol {
        if (globals.containsKey(name)) {
            //TODO criar um tipo semantic exception
            throw SemanticException("Variável '$name' já declarada.")
        }
        val symbol = VarSymbol(
            name = name,
            kind = SymbolKind.VARIABLE,
            type = TokenTypeConverter.internalTypeFromTokenType(type.type),
            storage = StorageKind.GLOBAL,
            index = globalIndex++
        )
        globals[name] = symbol
        return symbol
    }

    fun resolve(name: String): Symbol? {
        if (!scopes.empty() && scopes.peek().containsKey(name)) {
            return scopes.peek()[name]
        }
        val symbol = globals[name]
            ?: throw SemanticException("Identificador '$name' não declarado.")
        return symbol
    }


    fun beginScope() {
        scopes.push(mutableMapOf())
    }

    fun endScope() {
        scopes.pop()
    }
}