package sample.test.hinote.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sample.test.hinote.home.data.NoteRepository
import sample.test.hinote.home.data.local.Note

const val PAGING = 20

class HomeViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.UiStateLoading)
    val uiState = _uiState.asStateFlow()
    private val _refresh = MutableSharedFlow<Unit>()
    private val _originalData = MutableStateFlow<MutableList<Note>>(mutableListOf())

    //paging manually, lazy load
    private var offset = 0

    init {
        viewModelScope.launch {
            try {
                _refresh.map {
                    val items = noteRepository.getAllNotes()
                    _originalData.value = items.toMutableList()
                    if (items.isNotEmpty()) {
                        if (offset + PAGING < items.size) {
                            offset += PAGING
                        } else {
                            offset = items.size
                        }
                    }
                    val uiState = UiState.UiStateLoaded(items = _originalData.value.take(offset))
                    _uiState.value = uiState
                    uiState
                }.collect { newState: UiState.UiStateLoaded ->
                    _uiState.value = newState
                }

            } catch (e: Exception) {
                val uiState = UiState.UiStateError(e.message)
                _uiState.value = uiState
            }


        }
        viewModelScope.launch {
            _refresh.emit(Unit)
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            try {
                if (offset >= _originalData.value.size) {
                    return@launch
                }
                _uiState.value = UiState.UiStateLoading

                if (offset + PAGING < _originalData.value.size) {
                    offset += PAGING
                } else {
                    offset = _originalData.value.size
                }
                val uiState = UiState.UiStateLoaded(items = _originalData.value.take(offset))
                _uiState.value = uiState
            } catch (e: Exception) {
                val uiState = UiState.UiStateError(e.message)
                _uiState.value = uiState
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _refresh.emit(Unit)
        }
    }

    sealed class UiState {
        data object UiStateLoading : UiState()
        data class UiStateLoaded(val items: List<Note> = emptyList()) : UiState()
        data class UiStateError(val message: String? = null) : UiState()
    }
}