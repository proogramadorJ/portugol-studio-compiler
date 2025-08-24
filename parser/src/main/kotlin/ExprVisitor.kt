package com.pedrodev

interface ExprVisitor<R> {
    fun visitLiteral(expr: Expr.Literal): R
    fun visitBinary(expr: Expr.Binary): R
}