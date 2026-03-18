package functions.string

import PortugolVM
import NativeFunction
import values.IntValue
import values.StringValue

class NumeroCaracteres : NativeFunction {
    override fun arity(): Int {
        return 1
    }

    override fun run(vm: PortugolVM) {
        val text = vm.pop() as StringValue
        vm.push(IntValue(text.str().length))
    }
}