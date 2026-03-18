import com.pedrodev.Expression
import com.pedrodev.Statement
import symbols.FunctionSymbol
import symbols.SymbolTable
import symbols.VarSymbol
import types.StorageKind
import types.TokenType
import types.VoidType
import values.BooleanValue
import values.CharacterValue
import values.FunctionValue
import values.IntValue
import values.RealValue
import values.StringValue

class ByteCodeGenerator(val symbolTable: SymbolTable) : Statement.Visitor<Unit>, Expression.Visitor<Unit> {
    private var bytecode = mutableListOf<Instruction>()
    val constantPool = ConstantPool()
    var indexMainFunction = -1

    fun genCode(program: List<Statement>): MutableList<Instruction> {
        bytecode.add(Instruction(OpCode.JMP))
        program.forEach { it.accept(this) }
        bytecode.add(Instruction(OpCode.HALT))

        if(indexMainFunction == - 1){ //função inicio não declarada
            throw RuntimeException("Função inicio não declarada.")
        }
        bytecode[0] = Instruction(OpCode.JMP, indexMainFunction)
        return bytecode
    }

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        stmt.initializer?.accept(this)
        val symbol = stmt.symbol as VarSymbol
        val opCode = if (symbol.storage == StorageKind.LOCAL) OpCode.STORE_LOCAL else OpCode.STORE_GLOBAL

        if (stmt.initializer == null) {
            when (symbol.type.name) {
                "inteiro" -> {
                    stmt.initializer = Expression.Literal(0, TokenType.TK_NUMERO_INTEIRO_LITERAL)
                }

                "real" -> {
                    stmt.initializer = Expression.Literal(0.0, TokenType.TK_NUMERO_REAL_LITERAL)
                }

                "logico" -> {
                    stmt.initializer = Expression.Literal(false, TokenType.TK_FALSO_LITERAL)
                }

                "cadeia" -> {
                    stmt.initializer = Expression.Literal("", TokenType.TK_STRING_LITERAL)
                }

                "caracter" -> { // TODO testar ver se isso não vai quebrar
                    stmt.initializer = Expression.Literal('\u0000', TokenType.TK_CHAR_LITERAL)
                }
            }
            stmt.initializer?.accept(this)
        }
        bytecode.add(Instruction(opCode, symbol.index as Int))
    }

    override fun visitFuncStatement(stmt: Statement.Function) {
        val functionSymbol = stmt.symbol as FunctionSymbol
        val startAdrr = bytecode.size
        val isMainFunction = stmt.name.lexeme == "inicio"

        val fValue = FunctionValue(
            name = functionSymbol.name,
            arity = stmt.params.size,
            localCount = functionSymbol.localCount,
            startAdres = startAdrr
        )
        val constIndex = constantPool.add(fValue)
        functionSymbol.constPoolAddres = constIndex
        stmt.body.forEach { it.accept(this) }

        if(functionSymbol.returnType == VoidType && !isMainFunction){ //TODO Eu ACHO que se chamar a função inicio a partir de outra função vai quebrar
            bytecode.add(Instruction(OpCode.PUSH_NULL))
            bytecode.add(Instruction(OpCode.RETURN))
        }

        if(isMainFunction){
            indexMainFunction = startAdrr
            bytecode.add(Instruction(OpCode.HALT))
        }
    }

    override fun visitBlockStatement(stmt: Statement.Block) {
        stmt.body.forEach { it.accept(this) }
    }

    override fun visitIfStatement(stmt: Statement.If) {
        stmt.condition.accept(this)
        val jumpToElseAddr = bytecode.size
        bytecode.add(Instruction(OpCode.JMP_IF_FALSE, 0))

        stmt.thenBranch.accept(this)
        if (stmt.elseBranch != null) {
            val jumpToEndAddr = bytecode.size
            bytecode.add(Instruction(OpCode.JMP, 0))
            bytecode[jumpToElseAddr] = Instruction(OpCode.JMP_IF_FALSE, bytecode.size)
            stmt.elseBranch?.accept(this)
            bytecode[jumpToEndAddr] = Instruction(OpCode.JMP, bytecode.size)
        } else {
            bytecode[jumpToElseAddr] = Instruction(OpCode.JMP_IF_FALSE, bytecode.size)
        }
    }

    override fun visitWhileStatement(stmt: Statement.While) {
        val conditionBeginAddr = bytecode.size
        stmt.condition.accept(this)
        bytecode.add(Instruction(OpCode.JMP_IF_FALSE))
        val jmpIfFalseAddr = bytecode.size - 1
        stmt.body.accept(this)
        bytecode.add(Instruction(OpCode.JMP, conditionBeginAddr))
        val endOfWhileAddr = bytecode.size
        bytecode[jmpIfFalseAddr] = Instruction(OpCode.JMP_IF_FALSE, endOfWhileAddr)
    }

    override fun visitReturnStatement(stmt: Statement.Return) {
       stmt.expression.accept(this)
       bytecode.add(Instruction(OpCode.RETURN))
    }

    override fun visitLiteral(expression: Expression.Literal) {
        val portugolValue = when (expression.type) {
            TokenType.TK_NUMERO_INTEIRO_LITERAL -> IntValue(expression.value as Int)
            TokenType.TK_NUMERO_REAL_LITERAL -> RealValue(expression.value as Double)
            TokenType.TK_STRING_LITERAL -> StringValue(expression.value as String)
            TokenType.TK_VERDADEIRO_LITERAL -> BooleanValue(true)
            TokenType.TK_FALSO_LITERAL -> BooleanValue(false)
            TokenType.TK_CHAR_LITERAL -> CharacterValue(expression.value as Char)
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
            TokenType.TK_IGUAL_IGUAL -> OpCode.EQ
            TokenType.TK_DIFERENTE -> OpCode.NE
            TokenType.TK_MAIOR -> OpCode.GT
            TokenType.TK_MAIOR_OU_IGUAL -> OpCode.GE
            TokenType.TK_MENOR -> OpCode.LT
            TokenType.TK_MENOR_OU_IGUAL -> OpCode.LE
            else -> throw RuntimeException("Operador ${expression.operator.lexeme} não mapeado") // TODO criar exceção personalizada
        }

        bytecode.add(Instruction(opOperator))
    }

    override fun visitLogical(expression: Expression.Logical) {
        TODO()
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

    override fun visitCallExpr(expression: Expression.Call) {
        expression.arguments.forEach { it.accept(this) }
        val name = expression.callee as Expression.Variable
        val function = symbolTable.resolve(name.name.lexeme) as FunctionSymbol

        if (function.native) {
            bytecode.add(Instruction(OpCode.CALL_NATIVE, function.nativeIndex))
        } else {
            bytecode.add(Instruction(OpCode.CALL, function.constPoolAddres))
        }
    }
}