package com.pedrodev.portugol_ide_mobile.ui.viewmodel

import ByteCodeGenerator
import Disassembler
import PortugolVM
import SemanticAnalyzer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrodev.Parser
import com.pedrodev.Scanner
import com.pedrodev.portugol_ide_mobile.vminterface.ConsolePortugolBuffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import symbols.SymbolTable

class PortugolViewModel : ViewModel() {
    private val consoleBuffer = ConsolePortugolBuffer()
    val consoleLines = consoleBuffer.output

    fun onExecute(codeText: String, onNavigate: () -> Unit) {
        onNavigate()

        //TODO nao está limpando o terminal antes da execução
        viewModelScope.launch(Dispatchers.Default) {
            try {
                consoleBuffer.clear()
                val scanner = Scanner(codeText)
                val tokens = scanner.scanTokens()
                
                val parser = Parser(tokens)
                val statements = parser.parse2()
                
                val semantic = SemanticAnalyzer()
                val symbolTable: SymbolTable = semantic.analyze(statements)
                
                val bytecodeGen = ByteCodeGenerator(symbolTable)
                val code = bytecodeGen.genCode(statements)

                val disassembler = Disassembler()
                disassembler.run(code)

                
                val vm = PortugolVM(code, bytecodeGen.constantPool, consoleBuffer)
                vm.run()
                
                consoleBuffer.print("\n> Execução finalizada.")
            } catch (e: Exception) {
                consoleBuffer.print("\n> ERRO: ${e.message}")
            }
        }
    }
}
