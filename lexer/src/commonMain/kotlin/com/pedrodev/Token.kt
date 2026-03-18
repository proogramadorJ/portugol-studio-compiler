package com.pedrodev

import types.TokenType

data class Token(var type: TokenType, var line: Int, var column: Int, var lexeme: String, var literal: Any?)