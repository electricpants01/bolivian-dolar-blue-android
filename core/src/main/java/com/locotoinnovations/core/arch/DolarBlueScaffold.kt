package com.locotoinnovations.core.arch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface DolarBlueScaffold<ViewState, ViewEvent, SideEffect> {
    val viewState: StateFlow<ViewState>
    val sideEffects: Flow<SideEffect>
    fun handleEvent(event: ViewEvent)
    fun updateViewState(block: (ViewState) -> ViewState)
    fun CoroutineScope.emitSideEffect(sideEffect: SideEffect)
}

class DolarBlueScaffoldDelegate<ViewState, ViewEvent, SideEffect>(
    initialState: ViewState,
) : DolarBlueScaffold<ViewState, ViewEvent, SideEffect> {
    private val _viewState = MutableStateFlow(initialState)

    override val viewState: StateFlow<ViewState>
        get() = _viewState.asStateFlow()

    private val _sideEffects by lazy { Channel<SideEffect>(Channel.BUFFERED) }

    override val sideEffects: Flow<SideEffect> by lazy { _sideEffects.receiveAsFlow() }

    override fun handleEvent(event: ViewEvent) {}

    override fun updateViewState(block: (ViewState) -> ViewState) {
        _viewState.update(block)
    }

    override fun CoroutineScope.emitSideEffect(sideEffect: SideEffect) {
        this.launch { _sideEffects.send(sideEffect) }
    }
}

fun <ViewState, ViewEvent, SideEffect> dolarBlueScaffold(
    initialState: ViewState,
): DolarBlueScaffoldDelegate<ViewState, ViewEvent, SideEffect> {
    return DolarBlueScaffoldDelegate(initialState = initialState)
}