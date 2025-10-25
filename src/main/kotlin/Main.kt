package com.pedrodev

import java.io.File

fun main() {

    val codeFile =
        File("C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\teste_caracter_nao_finalizado.portugol")
    val codeText = codeFile.readText(Charsets.UTF_8)

    val scanner = Scanner(codeText)
    val tokens = scanner.scanTokens()

    val parser = Parser(tokens)
    val expression = parser.parse()

    println("\nast")
    val printer = ASTPrinter()
    printer.print(expression)

    /**
    val codeGenerator = ByteCodeGenerator()
    val byteCode = codeGenerator.generate(expression)

    println("\nbytecode")
    for (instruction in byteCode) {
    println(instruction)
    }

    val instructions = mutableListOf<String>()
    instructions.addAll(byteCode)
    instructions.add("PRINT")

    println("\nevaluation")
    val vm = VM(instructions)
    vm.run()
     **/
}