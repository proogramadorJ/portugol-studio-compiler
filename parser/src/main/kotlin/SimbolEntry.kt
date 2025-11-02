package com.pedrodev

data class SimbolEntry(
    val name: String,
    val type: TokenType,
    val kind: SymbolKind,
    val declarationToken: Token,
    val offset: Int? = null
)