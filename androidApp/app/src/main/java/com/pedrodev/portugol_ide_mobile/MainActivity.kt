package com.pedrodev.portugol_ide_mobile

import ByteCodeGenerator
import Disassembler
import PortugolVM
import SemanticAnalyzer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pedrodev.ASTPrinter
import com.pedrodev.Parser
import com.pedrodev.Scanner
import com.pedrodev.portugol_ide_mobile.ui.theme.PortugolidemobileTheme
import symbols.SymbolTable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PortugolidemobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
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

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PortugolidemobileTheme {
        Greeting("Android")
    }
}