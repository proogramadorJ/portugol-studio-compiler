package functions.io

import PortugolVM
import functions.NativeFunction
import kotlinx.coroutines.CompletableDeferred
import values.StringValue

class Leia : NativeFunction {
    override fun arity(): Int {
       return 0;
    }

    override suspend fun run(vm: PortugolVM) {
        //Promisse
        val deferred = CompletableDeferred<String>()

        vm.pendingInput = deferred

        val input = deferred.await()
        vm.pendingInput = null
        //TODO Por enquanto só recebe String
        vm.push(StringValue(input))
    }
}