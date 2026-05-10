package functions.io

import PortugolVM
import functions.NativeFunction
import values.Value

class Escreva : NativeFunction {
    override fun arity(): Int {
        return 0
    }

    override suspend fun run(vm: PortugolVM) {
        val value: Value? = vm.pop()
        if (value != null) {
            vm.console.print(value.str())
            println(value.str())
        }
    }
}