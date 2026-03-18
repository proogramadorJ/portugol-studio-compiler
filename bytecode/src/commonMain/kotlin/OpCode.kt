enum class OpCode {
    //math
    ADD, SUB, MUL, DIV,

    //logical
    EQ, // EQUAL
    NE, // NOT EQUAL
    LT, // LESS THEN
    LE, // LESS EQUAL
    GT, // GREATER
    GE, // GREATER EQUAL

    //variable
    LOAD_LOCAL, LOAD_GLOBAL,
    STORE_LOCAL, STORE_GLOBAL,

    //const
    LOAD_CONST,

    //functions
    CALL, CALL_NATIVE, RETURN, PUSH_NULL,

    //VM Internal
    HALT,

    //JMPs
    JMP_IF_FALSE,
    JMP
}