package visitor

import com.pedrodev.Statement

/**
 * Segunda travessia na AST -> Cria simbolos locais(variaveis), resolve nomes e cria escopos.
 */
class NameAndScopeResolverVisitor : Statement.Visitor< Unit> {
    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        TODO("Not yet implemented")
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        TODO("Not yet implemented")
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        TODO("Not yet implemented")
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
}