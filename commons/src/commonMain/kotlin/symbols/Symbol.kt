package symbols

import types.StorageKind
import types.SymbolKind
import types.Type

sealed class Symbol {
    abstract val name : String
}

class VarSymbol(
    override val name: String,
    val kind : SymbolKind,
    val type: Type,
    val storage: StorageKind,
    val index: Int? = null
) : Symbol()

class FunctionSymbol(
    override val name: String,
    val parametersType : List<Type>,
    val returnType: Type,
    var nativeIndex: Int?,
    var native : Boolean = false,
    var localCount : Int = 0,
    var constPoolAddres: Int?
): Symbol()
