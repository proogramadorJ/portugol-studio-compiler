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
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }

    override fun sub(other: Value): Value {
        return when (other) {
            is IntValue -> IntValue(value - other.value)
            is RealValue -> RealValue(value - other.value)
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }

    override fun mul(other: Value): Value {
        return when (other) {
            is IntValue -> IntValue(value * other.value)
            is RealValue -> RealValue(value * other.value)
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
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
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }

    override fun eq(other: Value): Boolean {
        return when (other) {
            is IntValue -> value == other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
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
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }

    override fun le(other: Value): Boolean {
        return when (other) {
            is IntValue -> value <= other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }

    override fun gt(other: Value): Boolean {
        return when (other) {
            is IntValue -> value > other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }

    override fun ge(other: Value): Boolean {
        return when (other) {
            is IntValue -> value >= other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'inteiro'")
            }
        }
    }
}
