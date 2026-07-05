package functions

import VM

interface NativeFunction {
    fun arity() : Int
    suspend fun run(vm: VM)
}