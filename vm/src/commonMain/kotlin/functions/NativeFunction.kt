package functions

import PortugolVM

interface NativeFunction {
    fun arity() : Int
    suspend fun run(vm: PortugolVM)
}