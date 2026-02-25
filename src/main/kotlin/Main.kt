package com.pedrodev

import SemanticAnalyzer
import java.io.File

fun main() {

    val codePath = "C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\test_analise_semantica_01.portugol"
    println("Running code $codePath")
    val codeFile =
        File(codePath)
    val codeText = codeFile.readText(Charsets.UTF_8)
    println("code $codeText")

    val scanner = Scanner(codeText)
    val tokens = scanner.scanTokens()
    println("Análise léxica Completa - OK")

    val parser = Parser(tokens)
    val statements = parser.parse()
    println("Análise sintatica Completa - OK")

    val semantic = SemanticAnalyzer()
    semantic.analyze(statements)
    println("Análise semantica Completa - OK")


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