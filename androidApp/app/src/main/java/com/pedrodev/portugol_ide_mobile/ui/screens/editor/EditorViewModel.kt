package com.pedrodev.portugol_ide_mobile.ui.screens.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditorViewModel : ViewModel() {
    var codeText by mutableStateOf(
        """
        funcao inicio(){
             real vetor3[2] = {1.4,2.5}
              vetor3[0] = 10.0
              escreva(vetor3[0])
        }
        """.trimIndent()
    )
        private set

    fun updateCode(newCode: String) {
        codeText = newCode
    }
}