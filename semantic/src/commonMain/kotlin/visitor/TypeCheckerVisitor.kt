package visitor

import com.pedrodev.Expression
import com.pedrodev.Statement
import exception.SemanticException
import symbols.ArraySymbol
import symbols.VarSymbol
import types.BoolType
import types.CaracterType
import types.IntType
import types.RealType
import types.StringType
import types.TokenType
import types.Type
import types.VoidType

/**
 *  Valida a compatibilidade de tipos de variaveis/expressões
 */
class TypeCheckerVisitor : Statement.Visitor<Unit>, Expression.Visitor<Type> {

    val numericTypes = listOf(IntType, RealType)

    override fun visitLiteral(expression: Expression.Literal): Type {
        return getTypeFromTokenType(expression.type)
    }

    /**
     * TODO incluir as operações de bitwise
     */
    override fun visitBinary(expression: Expression.Binary): Type {
        val leftType = expression.left.accept(this)
        val rightType = expression.right.accept(this)

        when (expression.operator.type) {

            //Operadores aritmeticos
            TokenType.TK_SUBTRACAO, TokenType.TK_MULTPLICACAO, TokenType.TK_DIVISAO -> {
                if (!isNumericType(leftType, rightType)) {
                    val wrongType = if (!numericTypes.contains(leftType)) leftType else rightType
                    throw SemanticException("O tipo '${wrongType.name}' não é suportado para operações aritméticas. '${expression.operator.lexeme}'")
                }
                return getNumericType(leftType, rightType)

            }

            TokenType.TK_MODULO -> {
                if (!isNumericType(leftType, rightType)) {
                    val wrongType = if (leftType != IntType) leftType else rightType
                    throw SemanticException("O tipo '${wrongType}' não é suportado para operações aritméticas.  '${expression.operator.lexeme}'")
                }
                return IntType
            }

            TokenType.TK_SOMA -> {
                if (!isNumericType(
                        leftType,
                        rightType
                    ) && leftType != StringType && rightType != StringType
                ) {
                    val wrongType = if (!numericTypes.contains(leftType)) leftType else rightType
                    throw SemanticException("O tipo '${wrongType.name}' não é suportado para operações aritméticas. '${expression.operator.lexeme}'")
                }

                if (leftType == StringType || rightType == StringType) {
                    return StringType
                }
                return getNumericType(leftType, rightType)

            }

            //Operadores relacionais
            TokenType.TK_IGUAL_IGUAL, TokenType.TK_DIFERENTE -> {
                if (leftType != rightType) {
                    throw SemanticException("Não é possível utilizar o operador ${expression.operator.lexeme} em tipos diferentes. '${leftType.name}' e '${rightType.name}'")
                }
                return BoolType
            }

            TokenType.TK_MAIOR, TokenType.TK_MAIOR_OU_IGUAL, TokenType.TK_MENOR, TokenType.TK_MENOR_OU_IGUAL -> {
                if (!isNumericType(leftType, rightType)) {
                    val wrongType = if (!numericTypes.contains(leftType)) leftType else rightType
                    throw SemanticException("O tipo '${wrongType.name}' não é suportado para operações relacionais. '${expression.operator.lexeme}'")
                }
                return BoolType
            }

            //TODO incluir operadores bitwise

            else -> {
                throw RuntimeException("Operador '${expression.operator.lexeme}' não suportado.")
            }
        }
    }

    override fun visitLogical(expression: Expression.Logical): Type {
        val leftType = expression.left.accept(this)
        val rightType = expression.right.accept(this)

        return when (expression.operator.type) {
            TokenType.TK_E, TokenType.TK_OU -> {
                if (leftType != BoolType || rightType != BoolType) {
                    val wrongType = if (leftType != BoolType) leftType else rightType
                    throw SemanticException("O tipo '${wrongType.name}' não é suportado para operações lógicas. '${expression.operator.lexeme}'")
                }
                BoolType
            }

            else -> {
                //Teoricamente esse código é inalcançável
                throw RuntimeException("Operador '${expression.operator.lexeme}' não suportado para operações lógicas")
            }
        }


    }

    override fun visitUnary(expression: Expression.Unary): Type {
        val type = expression.right.accept(this)

        return when (expression.operator.type) {
            TokenType.TK_SUBTRACAO -> {
                if (type != IntType && type != RealType) {
                    throw SemanticException("O tipo '${type.name}' não é suportado para operação unária '${expression.operator.lexeme}'.")
                }
                type
            }

            TokenType.TK_NAO -> {
                if (type != BoolType) {
                    throw SemanticException("O tipo '${type.name}' não é suportado para operação lógica '${expression.operator.lexeme}'")
                }
                BoolType
            }

            else -> {
                throw SemanticException("Operador '${expression.operator.lexeme}' não suportado para operação lógica")
            }
        }
    }

    override fun visitVariable(expression: Expression.Variable): Type {
        val symbol = expression.symbol as VarSymbol
        return symbol.type
    }

    override fun visitAssignExpr(expression: Expression.Assign): Type {
        val expressionType = expression.value.accept(this)
        val targetType = (expression.symbol as VarSymbol).type
        if (expressionType != targetType) {
            throw SemanticException("Não é póssivel atribuir um valor do tipo '${expressionType.name}' para uma variável do tipo '${targetType.name}'")
        }
        return VoidType
    }

    // TODO incluir Symbol na expression Call, a ser preenchido com o Symbol da função.
    // TODO deve permitir chamar funções que foram declaradas após sua chamada
    // TODO Deve retornar o tipo de retorno da função sendo chamada
    // TODO Por enquanto qualquer atribuicao do tipo x = funcao() vai quebrar a analise semantica
    override fun visitCallExpr(expression: Expression.Call): Type {

        return VoidType
    }

    override fun visitArrayAccess(expression: Expression.ArrayAccess): Type {
        val arraySymbol = expression.symbol as ArraySymbol
        return arraySymbol.type
    }

    fun getTypeFromTokenType(tkType: TokenType): Type {
        return when (tkType) {
            TokenType.TK_INTEIRO -> IntType
            TokenType.TK_NUMERO_INTEIRO_LITERAL -> IntType
            TokenType.TK_REAL -> RealType
            TokenType.TK_NUMERO_REAL_LITERAL -> RealType
            TokenType.TK_CARACTER -> CaracterType
            TokenType.TK_CHAR_LITERAL -> CaracterType
            TokenType.TK_STRING_LITERAL -> StringType
            TokenType.TK_CADEIA -> StringType
            TokenType.TK_LOGICO -> BoolType
            TokenType.TK_VAZIO -> VoidType
            TokenType.TK_VERDADEIRO_LITERAL -> BoolType
            TokenType.TK_FALSO_LITERAL -> BoolType
            else -> {
                throw RuntimeException("Tipo não suportado para variavel") // TODO essa mensagem mesmo?
            }
        }
    }

    override fun visitExprStatement(exprStatement: Statement.ExprStatement) {
        exprStatement.expr.accept(this)
    }

    override fun visitVarDeclarationStatement(stmt: Statement.VarDeclaration) {
        if (stmt.initializer != null) {
            val valueType = stmt.initializer!!.accept(this)
            val varSymbol = stmt.symbol as VarSymbol

            val isIntToDouble = varSymbol.type == RealType && valueType == IntType
            if ((valueType != varSymbol.type) && !isIntToDouble) {
                throw SemanticException("O tipo '${valueType.name}' não é compatível com o tipo de variável '${varSymbol.type.name}'")
            }
        }
    }

    //TODO Isso aqui vai dar trabalho :(
    //TODO precisa validar se todos os retornos são iguais ao tipo de retorno da função
    //TODO precisa verificar se todos os fluxos póssiveis retornam um valor
    override fun visitFuncStatement(stmt: Statement.Function) {
        stmt.body.forEach { it.accept(this) }
    }

    override fun visitBlockStatement(stmt: Statement.Block) {
        stmt.body.forEach { it.accept(this) }
    }

    override fun visitIfStatement(stmt: Statement.If) {
        if (stmt.condition.accept(this) != BoolType) {
            throw SemanticException("A expressão do comando 'se' deve ser do tipo 'logico'")
        }
    }

    override fun visitWhileStatement(stmt: Statement.While) {
        if (stmt.condition.accept(this) != BoolType) {
            throw SemanticException("A expressão do comando 'enquanto' deve ser do tipo 'logico'")
        }
    }

    override fun visitDoWhileStatement(stmt: Statement.DoWhile) {
        if (stmt.condition.accept(this) != BoolType) {
            throw SemanticException("A expressão do comando 'faça enquanto' deve ser do tipo 'logico'")
        }
    }

    override fun visitForStatement(stmt: Statement.For) {
        if (stmt.condition.accept(this) != BoolType) {
            throw SemanticException("A expressão do comando 'para' deve ser do tipo 'logico'")
        }
    }

    //TODO validar se estou dentro de função e se o tipo de retorno da função é != de Vazio
    override fun visitReturnStatement(stmt: Statement.Return) {

    }

    override fun visitSwitchStatement(stmt: Statement.Switch) {
    }

    override fun visitArrayDeclarationStatement(stmt: Statement.ArrayDeclaration) {
    }

    private fun isNumericType(leftType: Type, rightType: Type): Boolean {
        return numericTypes.contains(leftType) && numericTypes.contains(rightType)
    }

    private fun getNumericType(leftType: Type, rightType: Type): Type {
        if (leftType == IntType && rightType == IntType) {
            return IntType
        } else if (leftType == RealType && rightType == RealType) {
            return RealType
        } else if (leftType == RealType && rightType == IntType) {
            return RealType
        }
        throw SemanticException("Tipo não suportado para operações aritméticas.")
    }

}