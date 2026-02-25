package visitor

import TokenTypeConverter
import com.pedrodev.Statement
import com.pedrodev.Token
import symbols.SymbolTable

/**
 * Primeira travessia na AST -> Apenas cria Simbolos globais(Variavéis e funções)
 */
class SymbolTableBuilderVisitor(
    private val symbolTable: SymbolTable
) : Statement.Visitor<Unit> {

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        symbolTable.defineGlobal(
            stmt.name.lexeme,
            stmt.declaredType
        )
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        val paramsType: List<Token> = stmt.params.map { p -> p.dataTypeToken }
        symbolTable.defineFunction(stmt.name.lexeme, paramsType, stmt.returnType)
    }

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
    }

    override fun visitBlockStatement(stmt: Statement.Block) {
    }

    override fun visitIfStatement(stmt: Statement.If) {
    }

    override fun visitWhileStatement(stmt: Statement.While) {
    }
}