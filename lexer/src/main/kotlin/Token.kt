package com.pedrodev

data class Token(var type: TokenType, var line: Int, var column: Int, var lexeme: String)