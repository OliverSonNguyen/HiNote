package sample.test.hinote.home.tool

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sample.test.hinote.home.HomeViewModel
import sample.test.hinote.home.data.NoteRepositoryImpl
import sample.test.hinote.home.data.local.AppDatabase

//remove when using Hilt or dagger
@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val appDatabase = AppDatabase.getDatabase(context)
            return HomeViewModel(noteRepository = NoteRepositoryImpl(appDatabase.noteDao())) as T
        }
        throw IllegalArgumentException("Unknown HomeViewModel")
    }
}