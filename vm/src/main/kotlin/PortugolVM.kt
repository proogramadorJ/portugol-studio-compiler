import exception.StackOverflow
import exception.StackUnderflow
import values.BooleanValue
import values.Value
import java.util.*

//TODO Adiconar no construtor  val defaultInPutStream : InputStream, val defaultOutputStream: OutputStream
class PortugolVM(val bytecode: List<Instruction>, val constantPool: ConstantPool) {
    private var ip: Int = 0 // Instruction pointer
    private var stack: Stack<Value> = Stack()
    private val maxSizeStack: Int = 255

    // TODO Trocar para array de tamanho fixo baseado na quantidade de variaveis globais
    private var globalVars: Array<Value?> = arrayOfNulls(255)

    fun run() {
        while (ip < bytecode.size) {
            val currentInstruction = bytecode[ip]
            val opCode = currentInstruction.opCode

            when (opCode) {
                OpCode.ADD -> {
                    val b = pop()
                    val a = pop()
                    push(a.add(b))
                }

                OpCode.SUB -> {
                    val b = pop()
                    val a = pop()
                    push(a.sub(b))
                }

                OpCode.MUL -> {
                    val b = pop()
                    val a = pop()
                    push(a.mul(b))
                }

                OpCode.DIV -> {
                    val b = pop()
                    val a = pop()
                    push(a.div(b))
                }

                OpCode.LOAD_LOCAL -> {
                    TODO()
                }

                OpCode.LOAD_GLOBAL -> {
                    val globalVar = globalVars[currentInstruction.operating as Int]
                    push(globalVar as Value)
                }

                OpCode.STORE_LOCAL -> {
                    TODO()
                }

                OpCode.STORE_GLOBAL -> {
                    val globalVar = pop()
                    globalVars[currentInstruction.operating as Int] = globalVar
                }

                OpCode.LOAD_CONST -> {
                    val const = constantPool.get(currentInstruction.operating as Int)
                    push(const)
                }

                OpCode.CALL -> {
                    TODO()
                }

                OpCode.RETURN -> {
                    TODO()
                }

                OpCode.PRINT -> {
                    printPeek()
                }

                OpCode.HALT -> {
                    break
                }

                OpCode.EQ -> {
                    val b = pop()
                    val a = pop()
                    push(BooleanValue(a.eq(b)))
                }

                OpCode.NE -> {
                    val b = pop()
                    val a = pop()
                    push(BooleanValue(a.ne(b)))
                }

                OpCode.LT -> {
                    val b = pop()
                    val a = pop()
                    push(BooleanValue(a.lt(b)))
                }

                OpCode.LE -> {
                    val b = pop()
                    val a = pop()
                    push(BooleanValue(a.le(b)))
                }

                OpCode.GT -> {
                    val b = pop()
                    val a = pop()
                    push(BooleanValue(a.gt(b)))
                }

                OpCode.GE -> {
                    val b = pop()
                    val a = pop()
                    push(BooleanValue(a.ge(b)))
                }

                OpCode.JMP_IF_FALSE -> {
                    val bValue = pop() as BooleanValue
                    if (!bValue.value) {
                        currentInstruction.operating.let { ip = it as Int }
                        continue
                    }
                }

                OpCode.JMP -> {
                    currentInstruction.operating.let { ip = it as Int }
                    continue
                }
            }
            ip++
        }
    }

    private fun printPeek() {
        if (stack.isNotEmpty()) {
            val sValue = stack.pop()
            println(sValue.str())
        }
    }

    private fun pop(): Value {
        if (stack.isEmpty()) {
            throw StackUnderflow("Erro na execução do programa: Tentativa de remover de uma pilha vázia.")
        }
        return stack.pop()
    }

    private fun push(value: Value) {
        if (stack.size >= maxSizeStack) {
            throw StackOverflow("Erro na execução do programa: Pilha de operandos ultrapassa o limite de $maxSizeStack elementos.")
        }
        stack.push(value)
    }
}