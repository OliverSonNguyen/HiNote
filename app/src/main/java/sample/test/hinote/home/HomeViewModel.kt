package sample.test.hinote.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sample.test.hinote.home.data.NoteRepository
import sample.test.hinote.home.data.local.Note

class HomeViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.UiStateLoading)
    val uiState = _uiState.asStateFlow()
    private val loadedNotes = mutableListOf<Note>()
    private var offset = 0
    private val limit = 13
    fun loadNotes(refresh: Boolean = false) {
        Log.d("HomeViewModel", ">>>loadNote offset:$offset -  refresh:$refresh")
        viewModelScope.launch {
            try {
                if (refresh) {
                    offset = 0
                    loadedNotes.clear()
                }
//                val items = noteRepository.getNotes(offset, limit)
                val items = noteRepository.getAllNotes()
                Log.d("HomeViewModel", ">>>  items:${items.size} refresh:$refresh")
                if (items.isNotEmpty()) {
                    loadedNotes.addAll(items)
                    offset += items.size
                }
//                val uiState = UiState.UiStateLoaded(items = loadedNotes)
                val uiState = UiState.UiStateLoaded(items = items)
                _uiState.value = uiState
            } catch (e: Exception) {
                val uiState = UiState.UiStateError(e.message)
                _uiState.value = uiState
            }

        }
    }

    sealed class UiState {
        data object UiStateLoading : UiState()
        data class UiStateLoaded(val items: List<Note> = emptyList()) : UiState()
        data class UiStateError(val message: String? = null) : UiState()
    }
}