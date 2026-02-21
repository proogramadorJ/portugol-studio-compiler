package types

sealed class Type {
    abstract val name: String
}

object IntType : Type() {
    override val name = "inteiro"
}

object RealType : Type() {
    override val name = "real"
}

object BoolType : Type() {
    override val name = "logico"
}

object StringType : Type() {
    override val name = "cadeia"
}

object CaracterType : Type() {
    override val name = "caracter"
}

object VoidType : Type() {
    override val name = "vazio"
}