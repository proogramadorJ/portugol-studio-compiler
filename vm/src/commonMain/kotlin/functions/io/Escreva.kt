package functions.io

import PortugolVM
import NativeFunction
import values.Value

class Escreva : NativeFunction {
    override fun arity(): Int {
        return 0
    }

    override fun run(vm: PortugolVM) {
        val value: Value = vm.pop()
        println(value.str())
    }

}