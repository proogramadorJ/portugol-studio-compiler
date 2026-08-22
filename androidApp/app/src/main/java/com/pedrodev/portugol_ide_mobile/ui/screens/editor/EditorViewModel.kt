package com.pedrodev.portugol_ide_mobile.ui.screens.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditorViewModel : ViewModel() {
    var codeText by mutableStateOf(
        """
        funcao inicio(){
           inteiro a = 10
           inteiro b = 20
           inteiro c = a & b
           escreva(a & b)
           escreva(c)
           c = a | b
           escreva(c)
           escreva(a | b)
           c = a ^ b
           escreva(c)
           escreva(a ^ b)
           c = a << b
           escreva(c)
           escreva(a << b)
           c = a >> b
           escreva(c)
           escreva(a >> b)
              
        }
        """.trimIndent()
    )
        private set

    fun updateCode(newCode: String) {
        codeText = newCode
    }
}