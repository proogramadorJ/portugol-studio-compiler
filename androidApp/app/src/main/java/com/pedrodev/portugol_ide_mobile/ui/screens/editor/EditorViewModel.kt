package com.pedrodev.portugol_ide_mobile.ui.screens.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditorViewModel : ViewModel() {
    var codeText by mutableStateOf(
        """
        funcao inicio(){
            escreva("Olá, Mundo!")
        }
        """.trimIndent()
    )
        private set

    fun updateCode(newCode: String) {
        codeText = newCode
    }
}