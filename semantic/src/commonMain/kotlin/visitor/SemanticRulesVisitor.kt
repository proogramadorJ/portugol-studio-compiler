package visitor

import com.pedrodev.Expression
import com.pedrodev.Statement
import exception.SemanticException
import symbols.SymbolTable
import symbols.VarSymbol

/*
 Valida as seguintes regras:
   1 - Constante não pode ser declarada sem inicializar
   2 - Constante não pode ser reatribuida
   3-  Funções que não retornam nada não podem ter retorno -- TODO
   4 - Funções que retornam valor não podem ter retorno vazio -- TODO
   5 - Tipo do retorno de uma função não pode ser diferente do tipo declarado -- TODO
 */
class SemanticRulesVisitor(val symbolTable: SymbolTable) : Statement.Visitor<Unit>,
    Expression.Visitor<Unit> {

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        if (stmt.isConst && stmt.initializer == null) {
            throw SemanticException("Constante não pode ser declarada sem inicializar.")
        }
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        stmt.body.forEach { it.accept(this) }
    }

    override fun visitBlockStatement(stmt: Statement.Block) {
        stmt.body.forEach { it.accept(this) }
    }

    override fun visitIfStatement(stmt: Statement.If) {
        stmt.thenBranch.accept(this)
        stmt.elseBranch?.accept(this)
    }

    override fun visitWhileStatement(stmt: Statement.While) {
        stmt.body.accept(this)
    }

    override fun visitDoWhileStatement(stmt: Statement.DoWhile) {
        stmt.body.accept(this)
    }

    override fun visitForStatement(stmt: Statement.For) {
        stmt.body.accept(this)
    }

    override fun visitReturnStatement(stmt: Statement.Return) {
    }

    override fun visitSwitchStatement(stmt: Statement.Switch) {
    }

    override fun visitArrayDeclarationStatement(stmt: Statement.ArrayDeclaration) {

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
        val symbol = expression.symbol as VarSymbol
        if (symbol.isConst) {
            throw SemanticException("Constante não pode ser reatribuida.")
        }
    }

    override fun visitAssignArrayExpr(expression: Expression.AssignArray) {

    }

    override fun visitCallExpr(expression: Expression.Call) {
    }

    override fun visitArrayAccess(expression: Expression.ArrayAccess) {
    }
}