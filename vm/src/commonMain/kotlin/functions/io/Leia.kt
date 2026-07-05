package functions.io

import VM
import functions.NativeFunction
import kotlinx.coroutines.CompletableDeferred
import values.StringValue

class Leia : NativeFunction {
    override fun arity(): Int {
       return 0;
    }

    override suspend fun run(vm: VM) {
        //Promisse
        val deferred = CompletableDeferred<String>()

        vm.pendingInput = deferred

        vm.updateIsWaitingForInput() //Abre console

        val input = deferred.await()
        vm.pendingInput = null
        vm.push(StringValue(input))

        vm.updateIsWaitingForInput()//Fecha console
    }
}