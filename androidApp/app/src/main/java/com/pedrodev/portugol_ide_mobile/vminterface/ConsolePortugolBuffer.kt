package com.pedrodev.portugol_ide_mobile.vminterface

import io.PortugolConsole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ConsolePortugolBuffer : PortugolConsole {
    private val _output = MutableSharedFlow<String>(replay = 100)
    val output = _output.asSharedFlow()

    override fun print(message: String) {
        _output.tryEmit(message)
    }

    override fun println(message: String) {
        _output.tryEmit(message + "\n")
    }

    override fun clear() {
        //_output.resetReplayCache()
    }
}
