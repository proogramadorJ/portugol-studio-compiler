package com.pedrodev

import java.io.File

fun main() {

    val codeFile =
        File("C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\teste_bloco_programa.portugol")
    val codeText = codeFile.readText(Charsets.UTF_8)

    val scanner = Scanner(codeText)
    val tokens = scanner.scanTokens()

    val parser = Parser(tokens)
    val statements = parser.parse()
    println("Análise Completa - OK")


    // println("\nast")
    val printer = ASTPrinter()
    printer.print(statements)

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