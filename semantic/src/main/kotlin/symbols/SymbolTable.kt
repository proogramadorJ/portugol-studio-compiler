package symbols

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

    fun defineFunction(name: String, paramsType: List<Type>, returnType: Type): Symbol {
        if (globals.containsKey(name)) {
            throw SemanticException("Função '$name' já declarada.")
        }

        val symbol = FunctionSymbol(
            name = name,
            parametersType = paramsType,
            returnType = returnType,
            entryPoint = null,
        )

        globals[name] = symbol
        return symbol
    }

    fun defineVar(name: String, type: Type): Symbol {
        if (localIndex.empty()) {
            return defineGlobal(name, type)
        }
        return defineLocal(name, type)

    }
    fun defineLocal(name: String, type: Type): Symbol {
        if (scopes.empty()) {
            throw SemanticException("Variáveis locais só podem ser declaradas dentro de funções.")
        }
        if (scopes.peek().containsKey(name)) {
            throw SemanticException("Variável '$name' já declarada neste escopo.")
        }
        val varIndex = localIndex.peek()
        localIndex[localIndex.size - 1] = varIndex + 1

        val symbol = VarSymbol(
            name = name,
            storage = StorageKind.LOCAL,
            kind = SymbolKind.VARIABLE,
            type = type,
            index = varIndex
        )

        scopes.peek()[name] = symbol
        return symbol
    }

    fun defineGlobal(name: String, type: Type): Symbol {
        if (globals.containsKey(name)) {
            throw SemanticException("Variável '$name' já declarada.")
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
        for (i in scopes.size - 1 downTo 0) {
            if (scopes[i].containsKey(name)) {
                return scopes[i][name]
            }
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

    fun beginFunction() {
        localIndex.push(0)
        beginScope()
    }

    fun endFunction() {
        endScope()
        localIndex.pop()
    }

    fun isInFucntion() : Boolean{
        return !scopes.empty()
    }
}