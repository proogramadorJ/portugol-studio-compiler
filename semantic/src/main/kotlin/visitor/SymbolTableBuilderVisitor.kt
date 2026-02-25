package visitor

import TokenTypeConverter
import com.pedrodev.Statement
import symbols.SymbolTable
import types.Type

/**
 * Primeira travessia na AST -> Apenas cria Simbolos globais(Variavéis e funções)
 */
class SymbolTableBuilderVisitor(
    private val symbolTable: SymbolTable
) : Statement.Visitor<Unit> {

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        val symbol = symbolTable.defineGlobal(
            stmt.name.lexeme,
            TokenTypeConverter.internalTypeFromTokenType(stmt.declaredType.type)
        )
        stmt.symbol = symbol
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        val paramsType: List<Type> =
            stmt.params.map { p -> TokenTypeConverter.internalTypeFromTokenType(p.dataTypeToken.type) }

        val defineFunction = symbolTable.defineFunction(
            stmt.name.lexeme,
            paramsType,
            TokenTypeConverter.internalTypeFromTokenType(stmt.returnType.type)
        )
        stmt.symbol = defineFunction
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