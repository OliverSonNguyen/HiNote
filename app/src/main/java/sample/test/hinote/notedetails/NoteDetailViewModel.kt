package sample.test.hinote.notedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sample.test.hinote.home.data.NoteRepository
import sample.test.hinote.home.data.local.Note

class NoteDetailViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState())
    val uiState = _uiState.asStateFlow()

    private val _titleState = MutableStateFlow<String>("")
    private val _contentState = MutableStateFlow<String>("")

    init {
        viewModelScope.launch {
            _titleState.map {
                _uiState.value = _uiState.value.copy(message = "Typing....")
                it
            }
                .debounce { 500 }
                .flatMapLatest { title ->
                    flow {
                        if (title.isNotEmpty() && _contentState.value.isNotEmpty()) {
                            emit(noteRepository.insert(Note(title, _contentState.value)))
                        }
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = _uiState.value.copy(message = "Saved")
                }
        }
        viewModelScope.launch {
            _contentState.map {
                _uiState.value = _uiState.value.copy(message = "Typing....")
                it
            }
                .debounce { 500 }
                .flatMapLatest { content ->
                    flow {
                        if (content.isNotEmpty() && _titleState.value.isNotEmpty()) {
                            emit(noteRepository.insert(Note(_titleState.value, content)))
                        }
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = _uiState.value.copy(message = "Saved")
                }
        }
    }

    fun onTitleUpdated(newTitle: String) {
        _titleState.value = newTitle

    }

    fun onContentUpdated(newContent: String) {
        _contentState.value = newContent

    }

    data class UiState(
        //let update user when app save when user type
        val message: String? = null
    )
}