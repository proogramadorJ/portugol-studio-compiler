package values

class CharacterValue(var value: Char) : Value() {
    override fun str(): String {
        return value.toString()
    }

    override fun add(other: Value): Value {
        throw RuntimeException("Operação de soma incompativel com tipo 'character'.")  // TODO teoricamente esse erro nunca vai acontecer, pois o analisador semantico não deve deixar usar operador de soma com tipo character('?')
    }

    override fun sub(other: Value): Value {
        throw RuntimeException("Operação de subtração incompativel com tipo 'character'.")
    }

    override fun mul(other: Value): Value {
        throw RuntimeException("Operação de multiplicação incompativel com tipo 'character'.")
    }

    override fun div(other: Value): Value {
        throw RuntimeException("Operação de divisão incompativel com tipo 'character'.")
    }

    override fun eq(other: Value): Boolean {
        return when (other) {
            is CharacterValue -> value == other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'character'")
            }
        }
    }

    override fun ne(other: Value): Boolean {
        return !eq(other)
    }

    override fun lt(other: Value): Boolean {
        throw RuntimeException("Operador '<' incompativel com tipo 'character'.")
    }

    override fun le(other: Value): Boolean {
        throw RuntimeException("Operador '<=' incompativel com tipo 'character'.")
    }

    override fun gt(other: Value): Boolean {
        throw RuntimeException("Operador '>' incompativel com tipo 'character'.")
    }

    override fun ge(other: Value): Boolean {
        throw RuntimeException("Operador '>=' incompativel com tipo 'character'.")
    }
}