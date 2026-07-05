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

    //array
    ALLOC_NEW_ARRAY,
    STORE_ARRAY,
    LOAD_ARRAY,

    //const
    LOAD_CONST,

    //functions
    CALL, CALL_NATIVE, RETURN, PUSH_NULL,

    //VM Internal
    HALT,
    PUSH,
    POP, //remove top element
    DUP, //duplicate top element

    STR_TO_INT, // convert string to int
    STR_TO_DOUBLE, // convert string to double
    STR_TO_CHAR, // convert string to char

    //JMPs
    JMP_IF_FALSE,
    JMP_IF_TRUE,
    JMP
}