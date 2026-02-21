package symbols

import types.StorageKind
import types.SymbolKind
import types.Type

class SymbolTable {
    private val globals = mutableMapOf<String, Symbol>()
    private var globalIndex = 0

    fun defineGlobal(name: String, type: Type): Symbol {
        if (globals.containsKey(name)) {
            //TODO criar um tipo semantic exception
            throw RuntimeException("Variável '$name' já declarada.")
        }
        val symbol = Symbol(
            name = name,
            kind = SymbolKind.VARIABLE,
            type = type,
            storage = StorageKind.GLOBAL,
            index = globalIndex++
        )
        globals[name] = symbol
        return symbol
    }

    fun resolve(name: String): Symbol {
        val symbol = globals[name]
            ?: throw RuntimeException("Variável '$name' não declarada.")
        return symbol
    }
}