package visitor

import TokenTypeConverter
import com.pedrodev.Expression
import com.pedrodev.Statement
import com.pedrodev.Token
import symbols.SymbolTable

/**
 * Primeira travessia na AST -> Apenas cria Simbolos globais(Variavéis e funções)
 */
class SymbolTableBuilderVisitor(
    private val symbolTable: SymbolTable
) : Statement.Visitor<Unit>, Expression.Visitor<Unit> {

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        symbolTable.defineGlobal(
            stmt.name.lexeme,
            TokenTypeConverter.internalTypeFromTokenType(stmt.declaredType.type)
        )
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        val paramsType: List<Token> = stmt.params.map { p -> p.dataTypeToken }
        symbolTable.defineFunction(stmt.name.lexeme, paramsType, stmt.returnType)
    }

    override fun visitBlockStatement(stmt: Statement.Block) {

    }

    override fun visitIfStatement(stmt: Statement.If) {
    }

    override fun visitWhileStatement(stmt: Statement.While) {
    }

    override fun visitLiteral(expression: Expression.Literal) {
    }

    override fun visitBinary(expression: Expression.Binary) {
    }

    override fun visitLogical(expression: Expression.Logical) {
    }

    override fun visitUnary(expression: Expression.Unary) {
    }

    override fun visitVariable(expression: Expression.Variable) {
    }

    override fun visitAssignExpr(expression: Expression.Assign) {
    }
}