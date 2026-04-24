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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pedrodev.ASTPrinter
import com.pedrodev.Parser
import com.pedrodev.Scanner
import com.pedrodev.portugol_ide_mobile.ui.theme.PortugolidemobileTheme
import kotlinx.coroutines.launch
import symbols.SymbolTable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PortugolidemobileTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(
                                text = "Portugol IDE",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                            
                            Divider(modifier = Modifier.padding(bottom = 8.dp))

                            Text(
                                text = "Arquivo",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)
                            )
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                                label = { Text(text = "Novo Arquivo") },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.List, contentDescription = null) },
                                label = { Text(text = "Abrir") },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                }
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Desenvolvimento",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)
                            )
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                                label = { Text(text = "Exemplos") },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                }
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Opções",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)
                            )
                            NavigationDrawerItem(
                                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                label = { Text(text = "Configurações") },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                title = { Text("Portugol IDE") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                }
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { 
                                    testeCode() 
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Executar Código"
                                )
                            }
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = "Hello World",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PortugolidemobileTheme {
    }
}

fun testeCode(){
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
