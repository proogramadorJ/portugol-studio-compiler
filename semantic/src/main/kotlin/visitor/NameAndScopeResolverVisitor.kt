package visitor

import TokenTypeConverter
import com.pedrodev.Expression
import com.pedrodev.Statement
import symbols.Symbol
import symbols.SymbolTable

/**
 * Segunda travessia na AST -> Cria simbolos locais(variaveis), resolve nomes e cria escopos.
 */
class NameAndScopeResolverVisitor(val symbolTable: SymbolTable) : Statement.Visitor<Unit>, Expression.Visitor<Unit> {

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        if (symbolTable.isInFucntion()) {
            val declaredVar: Symbol = symbolTable.defineVar(
                stmt.name.lexeme,
                TokenTypeConverter.internalTypeFromTokenType(stmt.declaredType.type)
            )
            stmt.symbol = declaredVar
        } else {
            stmt.symbol = symbolTable.resolve(stmt.name.lexeme)
        }

    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        symbolTable.beginFunction()
        for (param in stmt.params) {
            val sym = symbolTable.defineLocal(
                param.identifierToken.lexeme,
                TokenTypeConverter.internalTypeFromTokenType(param.dataTypeToken.type)
            )
            param.symbol = sym
        }

        stmt.body.forEach { it.accept(this) }
        symbolTable.endFunction()
    }

    override fun visitBlockStatement(stmt: Statement.Block) {
        symbolTable.beginScope()
        stmt.body.forEach { it.accept(this) }
        symbolTable.endScope()
    }

    override fun visitIfStatement(stmt: Statement.If) {
        stmt.condition.accept(this)
        symbolTable.beginScope()
        stmt.thenBranch.accept(this)
        symbolTable.endScope()
        symbolTable.beginScope()
        stmt.elseBranch?.accept(this)
        symbolTable.endScope()
    }

    override fun visitWhileStatement(stmt: Statement.While) {
        stmt.condition.accept(this)
        symbolTable.beginScope()
        stmt.body.accept(this)
        symbolTable.endScope()
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

    override fun visitVariable(expression: Expression.Variable) {
        val symbolVar = symbolTable.resolve(expression.name.lexeme)
        expression.symbol = symbolVar
    }

    override fun visitAssignExpr(expression: Expression.Assign) {
        val targetVar = symbolTable.resolve(expression.name.lexeme)
        expression.symbol = targetVar
        expression.value.accept(this)
    }
}