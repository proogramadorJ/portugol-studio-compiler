package values

class StringValue(var value: String) : Value() {
    override fun str(): String {
        return value
    }

    override fun add(other: Value): Value {
        return StringValue(value + other.toString())
    }


    override fun sub(other: Value): Value {
        throw RuntimeException("Operação de subtração incompativel com tipo 'cadeia'.")
    }

    override fun mul(other: Value): Value {
        throw RuntimeException("Operação de multiplicação incompativel com tipo 'cadeia'.")
    }

    override fun div(other: Value): Value {
        throw RuntimeException("Operação de divisão incompativel com tipo 'cadeia'.")
    }

    override fun eq(other: Value): Boolean {
        return when (other) {
            is StringValue -> value == other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'cadeia'")
            }
        }
    }

    override fun ne(other: Value): Boolean {
        return !eq(other)
    }

    override fun lt(other: Value): Boolean {
        throw RuntimeException("Operador '<' incompativel com tipo 'cadeia'.")
    }

    override fun le(other: Value): Boolean {
        throw RuntimeException("Operador '<=' incompativel com tipo 'cadeia'.")
    }

    override fun gt(other: Value): Boolean {
        throw RuntimeException("Operador '>' incompativel com tipo 'cadeia'.")
    }

    override fun ge(other: Value): Boolean {
        throw RuntimeException("Operador '>=' incompativel com tipo 'cadeia'.")
    }
}
