import com.pedrodev.Expression
import com.pedrodev.Statement
import symbols.VarSymbol
import types.StorageKind
import types.TokenType

class ByteCodeGenerator : Statement.Visitor<Unit>, Expression.Visitor<Unit> {
    private var bytecode = mutableListOf<Instruction>()
    val constantPool = ConstantPool()

    fun genCode(program: List<Statement>): MutableList<Instruction> {
        program.forEach { it.accept(this) }
        bytecode.add(Instruction(OpCode.HALT))
        return bytecode
    }

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        stmt.initializer?.accept(this)

        if (stmt.initializer != null) {
            val symbol = stmt.symbol as VarSymbol
            val opCode = if (symbol.storage == StorageKind.LOCAL) OpCode.STORE_LOCAL else OpCode.STORE_GLOBAL

            bytecode.add(Instruction(opCode, symbol.index as Int))
        } else {
            //TODO inicializar variaveis com valor padrão Int -> 0, Real ->0.0,
        }
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

    override fun visitLiteral(expression: Expression.Literal) {
        val portugolValue = when (expression.type) {
            TokenType.TK_NUMERO_INTEIRO_LITERAL -> IntValue(expression.value as Int)
            TokenType.TK_NUMERO_REAL_LITERAL -> RealValue(expression.value as Double)
            TokenType.TK_STRING_LITERAL -> StringValue(expression.value as String)
            TokenType.TK_VERDADEIRO_LITERAL -> BooleanValue(true)
            TokenType.TK_FALSO_LITERAL -> BooleanValue(false)
            else -> throw RuntimeException("Valor literal do tipo ${expression.type} ainda não mapeado") // TODO criar Exception personalizada
        }

        val constIndex = constantPool.add(portugolValue)
        bytecode.add(Instruction(OpCode.LOAD_CONST, constIndex))
    }

    override fun visitBinary(expression: Expression.Binary) {
        expression.left.accept(this)
        expression.right.accept(this)

        val opOperator = when (expression.operator.type) {
            TokenType.TK_SOMA -> OpCode.ADD
            TokenType.TK_SUBTRACAO -> OpCode.SUB
            TokenType.TK_MULTPLICACAO -> OpCode.MUL
            TokenType.TK_DIVISAO -> OpCode.DIV

            else -> throw RuntimeException("Operador ${expression.operator.lexeme} não mapeado") // TODO criar exceção personalizada
        }

        bytecode.add(Instruction(opOperator))
    }

    override fun visitLogical(expression: Expression.Logical) {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary) {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable) {
        val symbol = expression.symbol as VarSymbol
        val opCode = if (symbol.storage == StorageKind.LOCAL) OpCode.LOAD_LOCAL else OpCode.LOAD_GLOBAL
        bytecode.add(Instruction(opCode, symbol.index))
    }

    override fun visitAssignExpr(expression: Expression.Assign) {
        expression.value.accept(this)

        val symbolTarget = expression.symbol as VarSymbol
        val opCode = if (symbolTarget.storage == StorageKind.LOCAL) OpCode.STORE_LOCAL else OpCode.STORE_GLOBAL

        bytecode.add(Instruction(opCode, symbolTarget.index))
    }
}