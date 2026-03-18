package com.pedrodev

import ByteCodeGenerator
import Disassembler
import PortugolVM
import SemanticAnalyzer
import symbols.SymbolTable

fun main() {

    val codePath =
        "C:\\Users\\pedro\\Desenvolvimento\\desktop\\portugol-studio-compiler\\src\\test\\test_bytecode_01_parser2.portugol"
    println("Running code $codePath")

    val inMemoryCode = """
    funcao inteiro fibonacci(inteiro n) {
        se (n <= 1) {
            retorne n
        }
        retorne fibonacci(n - 1) + fibonacci(n - 2)
    }

   
    funcao inicio(){
     escreva(fibonacci(10))
    }
    """.trimIndent()

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
    val symbolTable : SymbolTable = semantic.analyze(statements)
    println("Análise Semantica - OK")

    println("=======================Ast Printer=======================")
    val printer = ASTPrinter()
    printer.print(statements)

    println("=======================Bytecode=======================")
    val bytecodeGen = ByteCodeGenerator(symbolTable)
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