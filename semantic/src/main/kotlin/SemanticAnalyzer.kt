import com.pedrodev.Statement
import symbols.SymbolTable
import visitor.SymbolTableBuilderVisitor

class SemanticAnalyzer {

    fun analyze(statements: List<Statement>) {

        val symbolTable = SymbolTable()
        val symbolTableBuilderVisitor = SymbolTableBuilderVisitor(symbolTable)

        statements.forEach { s ->
            s.accept(symbolTableBuilderVisitor)
            //TODO outros visitors da analise semantica

        }
    }

}