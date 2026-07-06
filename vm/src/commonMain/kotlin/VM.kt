import exception.StackOverflow
import exception.StackUnderflow
import functions.NativeFunction
import functions.io.Escreva
import functions.io.Leia
import functions.string.NumeroCaracteres
import internal.CallFrame
import io.PortugolConsole
import kotlinx.coroutines.CompletableDeferred
import values.BooleanValue
import values.CharacterValue
import values.FunctionValue
import values.IntValue
import values.RealValue
import values.StringValue
import values.Value
import kotlin.arrayOfNulls


class VM(
    val bytecode: List<Instruction>,
    val constantPool: ConstantPool,
    val console: PortugolConsole,
    val updateIsWaitingForInput: () -> Unit
) {
    private var ip: Int = 0
    private var stack: MutableList<Value?> = mutableListOf()
    private val maxSizeStack: Int = 255
    private var nativeFunctions: MutableList<NativeFunction> = mutableListOf()
    private var callFrames: MutableList<CallFrame> = mutableListOf()
    var pendingInput: CompletableDeferred<String>? = null
    var heap = mutableMapOf<Int, Array<Value?>>()

    // TODO Trocar para array de tamanho fixo baseado na quantidade de variaveis globais
    private var globalVars: Array<Value?> = arrayOfNulls(255)

    init {
        initNativeFunctions()
        initMainFrame()
    }

    suspend fun run() {
        while (ip < bytecode.size) {
            val currentInstruction = bytecode[ip]
            val opCode = currentInstruction.opCode

            when (opCode) {
                OpCode.ADD -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(a.add(b))
                }

                OpCode.SUB -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(a.sub(b))
                }

                OpCode.MUL -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(a.mul(b))
                }

                OpCode.DIV -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(a.div(b))
                }

                OpCode.LOAD_LOCAL -> {
                    val currentFrame = callFrames.last()
                    val localVarIndex =
                        currentFrame.basePointer + (currentInstruction.operating as Int)
                    push(stack[localVarIndex])
                }

                OpCode.LOAD_GLOBAL -> {
                    val globalVar = globalVars[currentInstruction.operating as Int]
                    push(globalVar as Value)
                }

                OpCode.STORE_LOCAL -> {
                    val currentFrame = callFrames.last()
                    val localVarIndex =
                        currentFrame.basePointer + (currentInstruction.operating as Int)
                    stack[localVarIndex] = pop()
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
                    val function =
                        constantPool.get(currentInstruction.operating as Int) as FunctionValue
                    val basePointer = stack.size - function.arity
                    val callFrame = CallFrame(
                        basePointer = basePointer,
                        function = function,
                        returnAdress = ip + 1
                    )

                    callFrames.add(callFrame)
                    repeat(function.localCount - function.arity) {
                        push(null)
                    }

                    ip = function.startAdres
                    continue
                }

                OpCode.RETURN -> {
                    val currentFrame = callFrames.removeAt(callFrames.size - 1)
                    val returnValue = pop()
                    while (stack.size > currentFrame.basePointer) {
                        stack.removeAt(stack.size - 1)
                    }

                    push(returnValue)
                    ip = currentFrame.returnAdress
                    continue
                }

                OpCode.PUSH_NULL -> {
                    push(null)
                }

                OpCode.HALT -> {
                    break
                }

                OpCode.EQ -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(BooleanValue(a.eq(b)))
                }

                OpCode.NE -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(BooleanValue(a.ne(b)))
                }

                OpCode.LT -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(BooleanValue(a.lt(b)))
                }

                OpCode.LE -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(BooleanValue(a.le(b)))
                }

                OpCode.GT -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(BooleanValue(a.gt(b)))
                }

                OpCode.GE -> {
                    val b = pop()!!
                    val a = pop()!!
                    push(BooleanValue(a.ge(b)))
                }

                OpCode.JMP_IF_FALSE -> {
                    val bValue = pop() as BooleanValue
                    if (!bValue.value) {
                        ip = currentInstruction.operating as Int
                        continue
                    }
                }

                OpCode.JMP_IF_TRUE -> {
                    val bValue = pop() as BooleanValue
                    if (bValue.value) {
                        ip = currentInstruction.operating as Int
                        continue
                    }
                }

                OpCode.JMP -> {
                    ip = currentInstruction.operating as Int
                    continue
                }

                OpCode.CALL_NATIVE -> {
                    val fIndex: Int = currentInstruction.operating as Int
                    val function = nativeFunctions[fIndex]
                    function.run(this)
                }

                OpCode.POP -> {
                    pop()
                }

                OpCode.DUP -> {
                    push(peek())
                }

                //TODO incluir tratamento de erro para conversão de valores nessas instruções

                OpCode.STR_TO_INT -> {
                    val value = pop() as StringValue
                    push(IntValue(value.value.toInt()))
                }

                OpCode.STR_TO_DOUBLE -> {
                    val value = pop() as StringValue
                    push(RealValue(value.value.toDouble()))
                }

                OpCode.STR_TO_CHAR -> {
                    val value = pop() as StringValue
                    if (value.value.length != 1) {
                        throw RuntimeException("Valor {${value.value}} 'inválido para tipo 'char'")
                    }
                    push(StringValue(value.value.toCharArray()[0].toString()))
                }

                OpCode.ALLOC_NEW_ARRAY -> {
                    val arrayHeapAddr = currentInstruction.operating as Int
                    val size = (pop() as IntValue).value
                    val arrayType = (pop() as IntValue).value
                    allocNewArray(arrayHeapAddr, arrayType, size)
                }

                //TODO incluir verificação de index out of bounds heap[arrayHeapAddr]?.lenght
                OpCode.STORE_ARRAY -> {
                    val arrayHeapAddr = currentInstruction.operating as Int
                    val arrayIndex = (pop() as IntValue).value
                    val value = pop()

                    heap[arrayHeapAddr]?.set(arrayIndex, value)
                }

                //TODO incluir verificação de index out of bounds heap[arrayHeapAddr]?.lenght
                OpCode.LOAD_ARRAY -> {
                    val arrayHeapAddr = currentInstruction.operating as Int
                    val arrayIndex = (pop() as IntValue).value
                    val value = heap[arrayHeapAddr]?.get(arrayIndex)
                    push(value)
                }

                OpCode.PUSH -> {
                    val value = currentInstruction.operating as Int
                    push(IntValue(value))
                }
            }
            ip++
        }
    }

    private fun allocNewArray(arrayIndex: Int, arrayType: Int, size: Int) {
        heap[arrayIndex] = Array(size) {
            when (arrayType) {
                1 -> CharacterValue('\u0000')
                2 -> BooleanValue(false)
                3 -> IntValue(0)
                4 -> RealValue(0.0)
                5 -> StringValue("")
                else -> throw RuntimeException("Tipo de dado inesperado na Máquina Virtual: $arrayType")
            }
        }
    }

    fun peek(): Value? {
        if (stack.isEmpty()) {
            throw StackUnderflow("Erro na execução do programa: Tentativa de remover de uma pilha vazia.")
        }
        return stack[stack.size - 1]
    }

    fun pop(): Value? {
        if (stack.isEmpty()) {
            throw StackUnderflow("Erro na execução do programa: Tentativa de remover de uma pilha vazia.")
        }
        return stack.removeAt(stack.size - 1)
    }

    fun push(value: Value?) {
        if (stack.size >= maxSizeStack) {
            throw StackOverflow("Erro na execução do programa: Pilha de operandos ultrapassa o limite de $maxSizeStack elementos.")
        }
        stack.add(value)
    }

    private fun initNativeFunctions() {
        nativeFunctions.add(Escreva())
        nativeFunctions.add(NumeroCaracteres())
        nativeFunctions.add(Leia())
    }

    private fun initMainFrame() {
        for (i in 0 until constantPool.size()) {
            val value = constantPool.get(i)
            if (value is FunctionValue && value.name == "inicio") {
                val callFrame = CallFrame(
                    basePointer = 0,
                    function = value,
                    returnAdress = bytecode.size
                )
                callFrames.add(callFrame)

                repeat(value.localCount) {
                    push(null)
                }
                break
            }
        }
    }
}