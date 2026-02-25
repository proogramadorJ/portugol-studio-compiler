package symbols

import TokenTypeConverter
import com.pedrodev.Token
import types.StorageKind
import types.SymbolKind
import types.Type
import java.util.*

class SymbolTable {
    private val globals = mutableMapOf<String, Symbol>()
    private var scopes: Stack<MutableMap<String, Symbol>> = Stack()
    private var globalIndex = 0

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

    fun defineGlobal(name: String, type: Type): Symbol {
        if (globals.containsKey(name)) {
            //TODO criar um tipo semantic exception
            throw RuntimeException("Variável '$name' já declarada.")
        }
        val symbol = VarSymbol(
            name = name,
            kind = SymbolKind.VARIABLE,
            type = type,
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
            ?: throw RuntimeException("Identificador '$name' não declarado.")
        return symbol
    }


    fun beginScope() {
        scopes.push(mutableMapOf())
    }

    fun endScope() {
        scopes.pop()
    }
}