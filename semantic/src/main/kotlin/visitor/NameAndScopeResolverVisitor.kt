package visitor

import com.pedrodev.Expression
import com.pedrodev.Statement
import symbols.SymbolTable

/**
 * Segunda travessia na AST -> Cria simbolos locais(variaveis), resolve nomes e cria escopos.
 */
class NameAndScopeResolverVisitor(val symbolTable: SymbolTable) : Statement.Visitor<Unit>, Expression.Visitor<Unit> {

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        val symbol = symbolTable.resolve(stmt.name.lexeme)


    }

    override fun visitFuncStatement(stmt: Statement.Function) {

    }

    override fun visitBlockStatement(stmt: Statement.Block) {
        TODO("Not yet implemented")
    }

    override fun visitIfStatement(stmt: Statement.If) {
        TODO("Not yet implemented")
    }

    override fun visitWhileStatement(stmt: Statement.While) {
        TODO("Not yet implemented")
    }

    override fun visitLiteral(expression: Expression.Literal) {
        TODO("Not yet implemented")
    }

    override fun visitBinary(expression: Expression.Binary) {
        TODO("Not yet implemented")
    }

    override fun visitLogical(expression: Expression.Logical) {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary) {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable) {
        TODO("Not yet implemented")
    }

    override fun visitAssignExpr(expression: Expression.Assign) {
        TODO("Not yet implemented")
    }
}