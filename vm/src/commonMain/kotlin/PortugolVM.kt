import exception.StackOverflow
import exception.StackUnderflow
import functions.io.Escreva
import NativeFunction
import functions.string.NumeroCaracteres
import internal.CallFrame
import values.BooleanValue
import values.FunctionValue
import values.Value

class PortugolVM(val bytecode: List<Instruction>, val constantPool: ConstantPool) {
    private var ip: Int = 0
    private var stack: MutableList<Value?> = mutableListOf()
    private val maxSizeStack: Int = 255
    private var nativeFunctions: MutableList<NativeFunction> = mutableListOf()
    private var callFrames: MutableList<CallFrame> = mutableListOf()

    // TODO Trocar para array de tamanho fixo baseado na quantidade de variaveis globais
    private var globalVars: Array<Value?> = arrayOfNulls(255)

    init {
        initNativeFunctions()
        initMainFrame()
    }

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
                   val currentFrame = callFrames.last()
                   val localVarIndex = currentFrame.basePointer + currentInstruction.operating as Int
                    stack.add(stack[localVarIndex])
                }

                OpCode.LOAD_GLOBAL -> {
                    val globalVar = globalVars[currentInstruction.operating as Int]
                    push(globalVar as Value)
                }

                OpCode.STORE_LOCAL -> {
                    val currentFrame = callFrames.last()
                    val localVarIndex = currentFrame.basePointer + currentInstruction.operating as Int
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
                    val function = constantPool.get(currentInstruction.operating as Int) as FunctionValue
                    val basePointer = stack.size - function.arity
                    val callFrame = CallFrame(
                        basePointer = basePointer,
                        function = function,
                        returnAdress = ip + 1
                    )

                    callFrames.add(callFrame)
                    var localFrameSizeToAdd = function.localCount - function.arity
                    while (localFrameSizeToAdd > 0) {
                        stack.add(null)
                        localFrameSizeToAdd -= 1
                    }
                    ip = function.startAdres
                    continue
                }

                OpCode.RETURN -> {
                   val currentFrame = callFrames.removeAt(callFrames.size - 1)
                    val returnValue = pop()
                    while(stack.size > currentFrame.basePointer){
                        pop()
                    }
                    stack.add(returnValue)
                    ip = currentFrame.returnAdress
                    continue
                }
                OpCode.PUSH_NULL -> {
                    pushNull()
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

                OpCode.CALL_NATIVE -> {
                    val fIndex: Int = currentInstruction.operating as Int
                    val function = nativeFunctions[fIndex]
                    function.run(this)
                }
            }
            ip++
        }
    }


    fun pop(): Value {
        if (stack.isEmpty()) {
            throw StackUnderflow("Erro na execução do programa: Tentativa de remover de uma pilha vázia.")
        }
        return stack.removeAt(stack.size - 1) ?: throw IllegalStateException("A pilha continha um valor nulo inesperado")
    }

    fun push(value: Value) {
        if (stack.size >= maxSizeStack) {
            throw StackOverflow("Erro na execução do programa: Pilha de operandos ultrapassa o limite de $maxSizeStack elementos.")
        }
        stack.add(value)
    }

    fun pushNull(){
        if (stack.size >= maxSizeStack) {
            throw StackOverflow("Erro na execução do programa: Pilha de operandos ultrapassa o limite de $maxSizeStack elementos.")
        }
        stack.add(null)
    }

    private fun initNativeFunctions() {
        nativeFunctions.add(Escreva())
        nativeFunctions.add(NumeroCaracteres())
    }

    private fun initMainFrame(){
        //TODO tem que criar um frame para a função inicio
    }

}