package com.pedrodev

class ASTPrinter : Expression.Visitor<String>, Statement.Visitor<Void?> {

    var shift = 4

    override fun visitLiteral(expression: Expression.Literal): String {
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

    fun print(statements: List<Statement>) {
        println("=== AST ===")
        println("Programa")
        statements.forEach {
            it.accept(this)
            shift = 4
        }
    }

    override fun visitExprStatement(expr: Statement.Expr): Void? {
        print(expr.expr.accept(this))
        return null
    }

    override fun visitVarStatement(stmt: Statement.Var): Void? {
        println(
            " ".repeat(shift) + "VarDeclaration<type: ${stmt.type.type}, name: ${stmt.name.lexeme}, initializer: ${
                stmt.initializer?.accept(
                    this
                )
            }>"
        )
        return null
    }
}