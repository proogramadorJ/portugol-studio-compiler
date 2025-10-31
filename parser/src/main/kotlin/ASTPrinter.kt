package com.pedrodev

class ASTPrinter : Expression.Visitor<String>, Statement.Visitor<Void?> {

    var space = 4

    override fun visitLiteral(expression: Expression.Literal): String {
        if (expression.value is Boolean) {
            return if (expression.value) "verdadeiro" else "falso"
        }
        return expression.value.toString()
    }

    override fun visitBinary(expression: Expression.Binary): String {
        val left = expression.left.accept(this)
        val right = expression.right.accept(this)
        val operation = expression.operator.lexeme

        return "(${left} $operation ${right})"
    }

    override fun visitLogical(expression: Expression.Logical): String {
        val left = expression.left.accept(this)
        val right = expression.right.accept(this)
        val operation = expression.operator.lexeme
        return "(${left} $operation ${right})"
    }

    override fun visitUnary(expression: Expression.Unary): String {
        val right = expression.right.accept(this)
        return "(${expression.operator.lexeme}${right})"
    }

    override fun visitVariable(expression: Expression.Variable): String {
        return expression.name.lexeme
    }

    override fun visitAssignExpr(expression: Expression.Assign): String {
        space += 4
        return " ".repeat(space) + "Assign<target: ${expression.name.lexeme}, value: ${expression.value.accept(this)}>"
    }

    fun print(statements: List<Statement>) {
        println("=== AST ===")
        println("Programa")
        statements.forEach {
            it.accept(this)
            space = 4
        }
    }

    override fun visitExprStatement(exprStatement: Statement.ExprStatement): Void? {
        println(exprStatement.expr.accept(this))
        return null
    }

    override fun visitVarStatement(stmt: Statement.Var): Void? {
        space += 4
        println(
            " ".repeat(space) + "VarDeclaration<type: ${stmt.type.type}, name: ${stmt.name.lexeme}, initializer: ${
                stmt.initializer?.accept(
                    this
                )
            }>"
        )
        space -= 4
        return null
    }

    override fun visitFuncStatement(stmt: Statement.Function): Void? {
        space += 4
        println(" ".repeat(space) + "FuncDeclaraction<name: ${stmt.name.lexeme}, paramCount:${stmt.params.size}, returnType: ${stmt.returnType.type.name}>")
        stmt.body.forEach { it.accept(this) }
        space -= 4
        return null
    }
}