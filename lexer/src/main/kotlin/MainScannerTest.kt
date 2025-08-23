package com.pedrodev

import java.io.File

class MainScannerTest {

    fun run() {

        // val codeFile = File("C:\\Desenvolvimento\\Projetos\\lexer-portugol-studio\\src\\test\\math.portugol")
        val codeFile = File("C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\lexer\\src\\test\\if.portugol")

        //This method is not recommended on huge files. It has an internal limitation of 2 GB file size.
        //https://stackoverflow.com/questions/55182578/how-to-read-plain-text-file-in-kotlin
        val codeText = codeFile.readText(Charsets.UTF_8)
        val scanner = Scanner(codeText)

        val tokens = scanner.scanTokens()

        for (token in tokens) {
            println("Token ${token.type} lexema ${token.lexeme}")
            println("linha ${token.line} coluna ${token.column}\n")

        }

    }
}
