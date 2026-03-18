package values

abstract class Value() {
    abstract fun str(): String
    abstract fun add(other: Value): Value
    abstract fun sub(other: Value): Value
    abstract fun mul(other: Value): Value
    abstract fun div(other: Value): Value
    abstract fun eq(other: Value): Boolean
    abstract fun ne(other: Value): Boolean
    abstract fun lt(other: Value): Boolean
    abstract fun le(other: Value): Boolean
    abstract fun gt(other: Value): Boolean
    abstract fun ge(other: Value): Boolean
}