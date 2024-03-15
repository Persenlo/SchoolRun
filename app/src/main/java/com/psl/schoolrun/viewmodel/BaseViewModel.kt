package com.psl.schoolrun.viewmodel

import androidx.lifecycle.*
import com.psl.schoolrun.state.IUiEffect
import com.psl.schoolrun.state.IUiEvent
import com.psl.schoolrun.state.IUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * ViewModel基类：记录所有正在请求的job，在viewModel将要销毁时，取消所有job
 */
abstract class BaseViewModel<Event : IUiEvent, State : IUiState, Effect : IUiEffect> :
    ViewModel() {

    // 当前异步任务列表
    private val asyncJobs: MutableList<Job> = mutableListOf()

    // 初始化视图状态
    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    /**
     * 获取当前视图状态值
     */
    protected val currentState: State
        get() = uiState.value

    /**
     * 当前的视图状态
     */
    private val _uiState : MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    /**
     * UI使用一次副作用的操作
     */
    private val _effect : Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    /**
     * 特定的用户操作,如：按钮点击，收藏，删除等
     */
    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    init {
        subscribeToEvents()
    }

    /**
     * 执行挂起任务
     */
    fun asyncLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = context) {
        block.invoke(this)
    }.apply {
        asyncJobs.add(this)
    }

    /**
     * ViewModel即将销毁的时候，取消所有正在进行的任务
     */
    override fun onCleared() {
        _effect.close()
        val iterator = asyncJobs.iterator()
        while(iterator.hasNext()) {
            val job = iterator.next()
            if(!job.isCancelled) {
                job.cancel()
            }
            iterator.remove()
        }
        super.onCleared()
    }

    /**
     * 设置当前视图状态
     */
    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     * UI使用一次副作用的操作
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        asyncLaunch(Dispatchers.IO) {
            _effect.send(effectValue)
        }
    }

    /**
     * 特定的用户操作,如：按钮点击，收藏，删除等
     */
    protected fun setEvent(event : Event) {
        val newEvent = event
        asyncLaunch(Dispatchers.IO) {
            _event.emit(newEvent)
        }
    }

    private fun subscribeToEvents() = asyncLaunch(Dispatchers.IO) {
        _event.collect {
            handleEvents(it)
        }
    }

    /**
     * 处理用户的操作
     */
    abstract fun handleEvents(event: Event)

}