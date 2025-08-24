package com.pedrodev

import java.util.Stack

class VM(var instructions: List<String>) {

    private var stack: Stack<Double> = Stack()

    fun run() {
        for (instruction in instructions) {
            val code = instruction.split(" ")
            when (code[0]) {
                "PUSH" -> {
                    stack.push(code[1].toDouble())
                }
                "MUL" -> {
                    stack.push(stack.pop() * stack.pop())
                }

                "DIV" -> {
                    stack.push(stack.pop() / stack.pop())
                }

                "ADD" -> {
                    stack.push(stack.pop() + stack.pop())
                }

                "SUB" -> {
                    stack.push(stack.pop() - stack.pop())
                }

                "PRINT" -> {
                    println(
                        if (stack.isNotEmpty()) stack.peek() else {
                            0
                        }
                    )


                }
                else -> {
                    throw RuntimeException("Instrução ${code[0]} não implementada.")
                }
            }
        }

    }

}