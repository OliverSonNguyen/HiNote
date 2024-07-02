package sample.test.hinote.home.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sample.test.hinote.home.data.local.Note
import sample.test.hinote.home.data.local.NoteDao

interface NoteRepository {
    suspend fun getNotes(): List<Note>
    suspend fun insert(note: Note)
}

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun getNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            noteDao.getNotes()
        }
    }

    override suspend fun insert(note: Note) {
        return withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }
}
