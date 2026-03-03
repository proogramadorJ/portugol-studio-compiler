package values

class RealValue(var value: Double) : Value() {
    override fun str(): String {
        return value.toString()
    }

    override fun add(other: Value): Value {
        return when (other) {
            is IntValue -> RealValue(value + other.value)
            is RealValue -> RealValue(value + other.value)
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun sub(other: Value): Value {
        return when (other) {
            is IntValue -> RealValue(value - other.value)
            is RealValue -> RealValue(value - other.value)
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun mul(other: Value): Value {
        return when (other) {
            is IntValue -> RealValue(value * other.value)
            is RealValue -> RealValue(value * other.value)
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun div(other: Value): Value {
        return when (other) {
            is IntValue -> {
                if (other.value == 0) throw RuntimeException("Divisão por zero.")
                RealValue(value / other.value)
            }

            is RealValue -> {
                if (other.value == 0.0) throw RuntimeException("Divisão por zero.")
                RealValue(value / other.value)
            }

            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun eq(other: Value): Boolean {
        return when (other) {
            is RealValue -> value == other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun ne(other: Value): Boolean {
        return !eq(other)
    }

    override fun lt(other: Value): Boolean {
        return when (other) {
            is RealValue -> value < other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun le(other: Value): Boolean {
        return when (other) {
            is RealValue -> value <= other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun gt(other: Value): Boolean {
        return when (other) {
            is RealValue -> value > other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }

    override fun ge(other: Value): Boolean {
        return when (other) {
            is RealValue -> value >= other.value
            else -> {
                throw RuntimeException("Tipo incompativel com tipo 'real'")
            }
        }
    }
}
