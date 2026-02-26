class ConstantPool {
    private var values = mutableListOf<Value>()

    //TODO Por enquanto duplica constantes que já existem
    fun add(value: Value): Int {
        values.add(value)
        return values.size - 1
    }

    fun get(index: Int): Value {
        return values[index]
    }

}