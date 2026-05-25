import com.pedrodev.Statement
import symbols.SymbolTable
import visitor.NameAndScopeResolverVisitor
import visitor.SymbolTableBuilderVisitor
import visitor.TypeCheckerVisitor

class SemanticAnalyzer {


    fun analyze(statements: List<Statement>): SymbolTable {

        val symbolTable = SymbolTable()
        val symbolTableBuilderVisitor = SymbolTableBuilderVisitor(symbolTable)
        val nameAndScopeResolverVisitor = NameAndScopeResolverVisitor(symbolTable)
        val typeCheckerVisitor = TypeCheckerVisitor()

        statements.forEach { s ->
            s.accept(symbolTableBuilderVisitor)
            s.accept(nameAndScopeResolverVisitor)
            s.accept(typeCheckerVisitor)
        }
        return symbolTable
    }

}