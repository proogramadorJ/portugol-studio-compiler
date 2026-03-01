package com.pedrodev

import ByteCodeGenerator
import Disassembler
import PortugolVM
import SemanticAnalyzer
import java.io.File

fun main() {

    val codePath =
        "C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\test_bytecode_01_parser2.portugol"
    println("Running code $codePath")

    val inMemoryCode  = """
        inteiro a = 10
        inteiro b = 10
        inteiro c = 10 * 10       
        b = a + 2
        b
        
    """.trimIndent()

//    val codeFile =
//        File(codePath)
//
  //  val codeText = codeFile.readText(Charsets.UTF_8)
    val codeText = inMemoryCode
    println("code $codeText")

    val scanner = Scanner(codeText)
    val tokens = scanner.scanTokens()
    println("Análise léxica Completa - OK")

    val parser = Parser(tokens)
    val statements = parser.parse2()
    println("Análise sintatica Completa - OK")

    val semantic = SemanticAnalyzer()
    semantic.analyze(statements)
    println("Análise semantica Completa - OK")

    val printer = ASTPrinter()
    printer.print(statements)

    println("=======================Bytecode=======================")
    val bytecodeGen = ByteCodeGenerator()
    val code = bytecodeGen.genCode(statements)

    println("=======================Disassembler=======================")
    val disassembler = Disassembler()
    disassembler.run(code)

    println("=======================Running VM=======================")
    val vm = PortugolVM(code, bytecodeGen.constantPool)
    vm.run()
    println("VM execution finished with status 0")



}