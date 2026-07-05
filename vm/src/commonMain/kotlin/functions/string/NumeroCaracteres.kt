package functions.string

import VM
import functions.NativeFunction
import values.IntValue
import values.StringValue

class NumeroCaracteres : NativeFunction {
    override fun arity(): Int {
        return 1
    }

    override suspend fun run(vm: VM) {
        val text = vm.pop() as StringValue
        vm.push(IntValue(text.str().length))
    }
}