package com.pedrodev.portugol_ide_mobile.ui.screens.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditorViewModel : ViewModel() {
    var codeText by mutableStateOf(
        """
        funcao inicio(){
           inteiro a = -7
           escreva(a) // esperado -7
           
           logico b = verdadeiro
           b = nao b
           escreva(b) // esperado falso
           
           inteiro c = ~7
           escreva(c) // esperado -8
              
        }
        """.trimIndent()
    )
        private set

    fun updateCode(newCode: String) {
        codeText = newCode
    }
}