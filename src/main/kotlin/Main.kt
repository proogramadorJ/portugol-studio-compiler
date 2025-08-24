package com.pedrodev

import java.io.File

fun main() {

    val codeFile =
        File("C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\test_01.portugol")
    val codeText = codeFile.readText(Charsets.UTF_8)

    val scanner = Scanner(codeText)
    val tokens = scanner.scanTokens()

    val parser = Parser(tokens)
    val expression = parser.parse()

    val printer = ASTPrinter()
    printer.print(expression)

    val codeGenerator = ByteCodeGenerator()
    val byteCode = codeGenerator.generate(expression)

    for (line in byteCode) {
        println(line)
    }
}