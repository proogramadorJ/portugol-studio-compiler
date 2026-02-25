import com.pedrodev.Statement
import symbols.SymbolTable
import visitor.NameAndScopeResolverVisitor
import visitor.SymbolTableBuilderVisitor

class SemanticAnalyzer {

    fun analyze(statements: List<Statement>) {

        val symbolTable = SymbolTable()
        val symbolTableBuilderVisitor = SymbolTableBuilderVisitor(symbolTable)
        val nameAndScopeResolverVisitor = NameAndScopeResolverVisitor(symbolTable)

        statements.forEach { s ->
            s.accept(symbolTableBuilderVisitor)
            s.accept(nameAndScopeResolverVisitor)
        }
    }

}