package com.pedrodev

import ByteCodeGenerator
import Disassembler
import PortugolVM
import SemanticAnalyzer

fun main() {

    val codePath =
        "C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\test_bytecode_01_parser2.portugol"
    println("Running code $codePath")

    val inMemoryCode = """
    real teste = 43.98
    inteiro c  = 2
    inteiro d = 2
    cadeia a = "Olá meu nome é Pedro"
    cadeia x = "Pe"
    cadeia xb = "PE"
    "Ola mundo" + c
    """.trimIndent()

//    val codeFile =
//        File(codePath)
//
    //  val codeText = codeFile.readText(Charsets.UTF_8)
    val codeText = inMemoryCode
    println("code $codeText")

    println("=======================Analise Lexica=======================")
    val scanner = Scanner(codeText)
    val tokens = scanner.scanTokens()
    println("Análise Léxica - OK")

    println("=======================Analise Sintatica=======================")
    val parser = Parser(tokens)
    val statements = parser.parse2()
    println("Análise Sintatica - OK")

    println("=======================Analise Semantica=======================")
    val semantic = SemanticAnalyzer()
    semantic.analyze(statements)
    println("Análise Semantica - OK")

    println("=======================Ast Printer=======================")
    val printer = ASTPrinter()
    printer.print(statements)

    println("=======================Bytecode=======================")
    val bytecodeGen = ByteCodeGenerator()
    val code = bytecodeGen.genCode(statements)
    println("Bytecode Generation - OK")

    println("=======================Disassembler=======================")
    val disassembler = Disassembler()
    disassembler.run(code)

    println("=======================Running VM=======================")
    val vm = PortugolVM(code, bytecodeGen.constantPool)
    vm.run()
    println("VM execution finished with status 0")


}