package symbols

import types.StorageKind
import types.SymbolKind
import types.Type

open class Symbol (
    val name : String,
    val kind : SymbolKind,
    val type: Type, // Tipo resolvido != do declarado e que está na ast
    val storage: StorageKind,
    val index: Int? = null
)