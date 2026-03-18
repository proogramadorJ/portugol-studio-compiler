package values

class FunctionValue( var name : String, var arity : Int, var localCount : Int, var startAdres: Int) : Value( ) {

    override fun str(): String {
       throw RuntimeException("Method not allowed")
    }

    override fun add(other: Value): Value {
       throw RuntimeException("Method not allowed")
    }

    override fun sub(other: Value): Value {
       throw RuntimeException("Method not allowed")
    }

    override fun mul(other: Value): Value {
       throw RuntimeException("Method not allowed")
    }

    override fun div(other: Value): Value {
       throw RuntimeException("Method not allowed")
    }

    override fun eq(other: Value): Boolean {
       throw RuntimeException("Method not allowed")
    }

    override fun ne(other: Value): Boolean {
       throw RuntimeException("Method not allowed")
    }

    override fun lt(other: Value): Boolean {
       throw RuntimeException("Method not allowed")
    }

    override fun le(other: Value): Boolean {
       throw RuntimeException("Method not allowed")
    }

    override fun gt(other: Value): Boolean {
       throw RuntimeException("Method not allowed")
    }

    override fun ge(other: Value): Boolean {
       throw RuntimeException("Method not allowed")
    }
}