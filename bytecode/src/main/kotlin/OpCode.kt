enum class OpCode {
    //math
    ADD, SUB, MUL, DIV,

    //variable
    LOAD_LOCAL, LOAD_GLOBAL,
    STORE_LOCAL, STORE_GLOBAL,

    //const
    LOAD_CONST,

    //functions
    CALL, RETURN,

    //VM Internal
    PRINT,
    HALT
}