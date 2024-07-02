package sample.test.hinote.notedetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sample.test.hinote.home.HomeViewModel
import sample.test.hinote.home.data.NoteRepositoryImpl
import sample.test.hinote.home.data.local.AppDatabase
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class NodeDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            val appDatabase = AppDatabase.getDatabase(context)
            return NoteDetailViewModel(noteRepository = NoteRepositoryImpl(appDatabase.noteDao())) as T
        }
        throw IllegalArgumentException("Unknown NoteDetailViewModel")
    }
}