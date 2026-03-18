class Disassembler {
    fun run(code: List<Instruction>) {
        var i = 0
        code.forEach {
            val operating = if (it.operating != null) it.operating else ""
            println("$i -  ${it.opCode} $operating")
            i++
        }
    }
}