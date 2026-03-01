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
    CALL, RETURN,

    //VM Internal
    PRINT,
    HALT,

    //JMPs
    JMP_IF_FALSE,
    JMP
}