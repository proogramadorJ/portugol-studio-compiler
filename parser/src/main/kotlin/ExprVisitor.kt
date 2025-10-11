package com.pedrodev

interface ExprVisitor<R> {
    fun visitLiteral(expr: Expr.Literal): R
    fun visitBinary(expr: Expr.Binary): R
    fun visitLogical(expr: Expr.Logical) : R
    fun visitUnary(expr: Expr.Unary) : R
}