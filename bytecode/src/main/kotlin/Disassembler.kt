class Disassembler {
    fun run(code: List<Instruction>) {
        code.forEach {
            if (it.operating != null) {
                println("${it.opCode} ${it.operating}")
            } else {
                println(it.opCode.name)
            }
        }
    }
}