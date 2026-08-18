package values

class IntValue(var value: Int) : Value() {
    override fun str(): String {
        return value.toString()
    }

    override fun add(other: Value): Value {
        return when (other) {
            is IntValue -> IntValue(value + other.value)
            is RealValue -> RealValue(value + other.value)
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun sub(other: Value): Value {
        return when (other) {
            is IntValue -> IntValue(value - other.value)
            is RealValue -> RealValue(value - other.value)
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun mul(other: Value): Value {
        return when (other) {
            is IntValue -> IntValue(value * other.value)
            is RealValue -> RealValue(value * other.value)
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun div(other: Value): Value {
        return when (other) {
            is IntValue -> {
                if (other.value == 0) throw RuntimeException("Divisão por zero.")
                IntValue(value / other.value)
            }
            is RealValue -> {
                if (other.value == 0.0) throw RuntimeException("Divisão por zero.")
                RealValue(value / other.value)
            }
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun eq(other: Value): Boolean {
        return when (other) {
            is IntValue -> value == other.value
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun ne(other: Value): Boolean {
        return !eq(other)
    }

    override fun lt(other: Value): Boolean {
        return when (other) {
            is IntValue -> value < other.value
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun le(other: Value): Boolean {
        return when (other) {
            is IntValue -> value <= other.value
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun gt(other: Value): Boolean {
        return when (other) {
            is IntValue -> value > other.value
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    override fun ge(other: Value): Boolean {
        return when (other) {
            is IntValue -> value >= other.value
            else -> {
                throw RuntimeException("Tipo incompatível com tipo 'inteiro'")
            }
        }
    }

    fun bitAnd(other: IntValue): IntValue {
        return IntValue(value and other.value)
    }

    fun bitOr(other: IntValue): IntValue {
        return IntValue(value or other.value)
    }

    fun bitXor(other: IntValue): IntValue {
        return IntValue(value xor other.value)
    }

    fun bitShiftLeft(other: IntValue): IntValue {
        return IntValue(value shl other.value)
    }

    fun bitShiftRight(other: IntValue): IntValue {
        return IntValue(value shr other.value)
    }

    override fun toString(): String {
        return str()
    }
}
