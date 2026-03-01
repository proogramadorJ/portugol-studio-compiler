import exception.StackOverflow
import exception.StackUnderflow
import java.io.InputStream
import java.io.OutputStream
import java.util.Stack

//TODO Adiconar no construtor  val defaultInPutStream : InputStream, val defaultOutputStream: OutputStream
class PortugolVM(val bytecode : List<Instruction>, val constantPool: ConstantPool) {
     private var ip : Int = 0 // Instruction pointer
     private var stack : Stack<Value> = Stack()
     private val maxSizeStack : Int = 255
    // TODO Trocar para array de tamanho fixo baseado na quantidade de variaveis globais
     private var globalVars : Array<Value?> = arrayOfNulls(255)

    fun run(){
        while(ip < bytecode.size){
            val currentInstruction = bytecode[ip]
            val opCode = currentInstruction.opCode

            //TODO por enquanto a VM assume que todos os operandos são números inteiros
            //TODO tratar divisão por zero em OpCode.DIV
            when(opCode){
                OpCode.ADD -> {
                    val b = pop() as IntValue
                    val a = pop() as IntValue
                    push(IntValue( a.value + b.value))
                }
                OpCode.SUB -> {
                    val b = pop() as IntValue
                    val a = pop() as IntValue
                    push(IntValue( a.value - b.value))
                }
                OpCode.MUL -> {
                    val b = pop() as IntValue
                    val a = pop() as IntValue
                    push(IntValue( a.value * b.value))
                }
                OpCode.DIV -> {
                    val b = pop() as IntValue
                    val a = pop() as IntValue
                    push(IntValue( a.value / b.value))
                }
                OpCode.LOAD_LOCAL -> {
                   TODO()
                }
                OpCode.LOAD_GLOBAL -> {
                    val globalVar = globalVars[currentInstruction.operating as Int] as IntValue
                    push(globalVar)
                }
                OpCode.STORE_LOCAL -> {
                    TODO()
                }
                OpCode.STORE_GLOBAL -> {
                    val globalVar = pop()
                    globalVars[currentInstruction.operating as Int] = globalVar
                }
                OpCode.LOAD_CONST -> {
                    val const = constantPool.get(currentInstruction.operating as Int) as IntValue
                    push(const)
                }
                OpCode.CALL -> TODO()
                OpCode.RETURN -> TODO()
                OpCode.PRINT -> printPeek()
                OpCode.HALT -> break
            }
            ip++
        }
    }

    //TODO por enquanto somente Inteiros
    private fun printPeek() {
        if(stack.isNotEmpty()){
            val value : IntValue  = stack.pop() as IntValue
            println(value.value)
        }
    }

    private fun pop() : Value {
        if(stack.isEmpty()){
            throw StackUnderflow("Erro na execução do programa: Tentativa de remover de uma pilha vázia.")
        }
        return stack.pop()
    }

    private fun push(value : Value){
        if(stack.size >= maxSizeStack){
            throw StackOverflow("Erro na execução do programa: Pilha de operandos ultrapassa o limite de $maxSizeStack elementos.")
        }
        stack.push(value)
    }
}