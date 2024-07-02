package sample.test.hinote.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import sample.test.hinote.home.data.local.Note

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.UiStateLoading)
    private val uiState = _uiState.asStateFlow()

    sealed class UiState {
        data object UiStateLoading : UiState()
        data class UiStateLoaded(val items: List<Note> = emptyList()) : UiState()
        data class UiStateError(val message: String? = null) : UiState()
    }
}