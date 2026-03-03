package values

class BooleanValue(var value: Boolean) : Value() {
    override fun str(): String {
        return value.toString()
    }

    override fun add(other: Value): Value {
        throw RuntimeException("Operador '+' incompativel com tipo 'logico'.")
    }

    override fun sub(other: Value): Value {
        throw RuntimeException("Operador '-' incompativel com tipo 'logico'.")
    }

    override fun mul(other: Value): Value {
        throw RuntimeException("Operador '*' incompativel com tipo 'logico'.")
    }

    override fun div(other: Value): Value {
        throw RuntimeException("Operador '/' incompativel com tipo 'logico'.")
    }

    override fun eq(other: Value): Boolean {
        return when (other) {
            is BooleanValue -> value == other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'logico'.")
            }
        }
    }

    override fun ne(other: Value): Boolean {
        return !eq(other)
    }

    override fun lt(other: Value): Boolean {
        throw RuntimeException("Operador '<' incompativel com tipo 'logico'.")
    }

    override fun le(other: Value): Boolean {
        throw RuntimeException("Operador '<=' incompativel com tipo 'logico'.")
    }

    override fun gt(other: Value): Boolean {
        throw RuntimeException("Operador '>' incompativel com tipo 'logico'.")
    }

    override fun ge(other: Value): Boolean {
        throw RuntimeException("Operador '>=' incompativel com tipo 'logico'.")
    }
}