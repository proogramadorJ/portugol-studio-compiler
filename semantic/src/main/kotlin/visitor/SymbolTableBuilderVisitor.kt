package visitor

import com.pedrodev.Expression
import com.pedrodev.Statement
import symbols.SymbolTable
import types.IntType
import types.TokenType
import types.Type

class SymbolTableBuilderVisitor(
    private val symbolTable: SymbolTable
) : Statement.Visitor<Unit>, Expression.Visitor<Unit> {

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        val symbol = symbolTable.defineGlobal(stmt.name.lexeme, internalTypeFromTokenType(stmt.name.type))
        stmt.symbol = symbol

        stmt.initializer?.accept(this)
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        TODO("Not yet implemented")
    }

    override fun visitBlockStatement(stmt: Statement.Block) {
        for (statement in stmt.body) {
            statement.accept(this)
        }
    }

    //Por enquanto só propaga e não cria escopos
    override fun visitIfStatement(stmt: Statement.If) {
        stmt.condition.accept(this)
        stmt.thenBranch.accept(this)
        stmt.elseBranch?.accept(this)
    }

    override fun visitWhileStatement(stmt: Statement.While) {
        stmt.condition.accept(this)
        stmt.body.accept(this)
    }

    override fun visitLiteral(expression: Expression.Literal) {
    }

    override fun visitBinary(expression: Expression.Binary) {
        expression.left.accept(this)
        expression.right.accept(this)
    }

    override fun visitLogical(expression: Expression.Logical) {
        expression.left.accept(this)
        expression.right.accept(this)
    }

    override fun visitUnary(expression: Expression.Unary) {
        expression.right.accept(this)
    }

    // uso de variavel
    override fun visitVariable(expression: Expression.Variable) {
        val symbol = symbolTable.resolve(expression.name.lexeme)
        expression.symbol = symbol
    }

    //atribuição de variavel e também expressão a direita
    override fun visitAssignExpr(expression: Expression.Assign) {
        val symbol = symbolTable.resolve(expression.name.lexeme)
        expression.symbol = symbol

        expression.value.accept(this)
    }

    private fun internalTypeFromTokenType(tokenType: TokenType): Type {
        return when (tokenType) {
            TokenType.TK_INTEIRO -> IntType
            else -> throw RuntimeException("Não existe tipo interno mapeado para '${tokenType.name}' ")// TODO Semantic Analyser exception
        }
    }
}