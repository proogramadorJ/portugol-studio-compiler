import PortugolVM

interface NativeFunction {
    fun arity() : Int
    fun run(vm: PortugolVM)
}