package sample.test.hinote.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sample.test.hinote.home.data.local.Note

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.UiStateLoading)
    val uiState = _uiState.asStateFlow()
    fun start() {
        viewModelScope.launch {
            // mock data
            val items = mutableListOf<Note>()
            for (i in 0..20) {
                items.add(Note("Note $i", "Content $i"))
            }
            val uiState = UiState.UiStateLoaded(items = items)
            _uiState.value = uiState
        }
    }

    sealed class UiState {
        data object UiStateLoading : UiState()
        data class UiStateLoaded(val items: List<Note> = emptyList()) : UiState()
        data class UiStateError(val message: String? = null) : UiState()
    }
}