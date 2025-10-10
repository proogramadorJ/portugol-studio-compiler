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
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left * right)
                }

                "DIV" -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left / right)
                }

                "ADD" -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left + right)
                }

                "SUB" -> {
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left - right)
                }

                "MOD" ->{
                    val right = stack.pop()
                    val left = stack.pop()
                    stack.push(left % right)
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