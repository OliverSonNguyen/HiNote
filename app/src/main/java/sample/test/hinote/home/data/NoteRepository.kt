package sample.test.hinote.home.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sample.test.hinote.home.data.local.Note
import sample.test.hinote.home.data.local.NoteDao

interface NoteRepository {
    suspend fun getAllNotes(): List<Note>
    suspend fun getNotes(offset: Int, limit: Int): List<Note>
    suspend fun getNote(nodeId: Long): Note?
    suspend fun insert(note: Note): Long
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    suspend fun deleteById(noteId: Long)
}

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun getAllNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            noteDao.getAllNote()
        }
    }

    override suspend fun getNotes(offset: Int, limit: Int): List<Note> {
        return withContext(Dispatchers.IO) {
            noteDao.getNotes(offset, limit)
        }
    }

    override suspend fun getNote(nodeId: Long): Note? {
        return withContext(Dispatchers.IO) {
            noteDao.getNote(nodeId)
        }
    }

    override suspend fun insert(note: Note): Long {
        return withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    override suspend fun update(note: Note) {
        return withContext(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    override suspend fun delete(note: Note) {
        return withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    override suspend fun deleteById(noteId: Long) {
        return withContext(Dispatchers.IO) {
            noteDao.deleteById(noteId)
        }
    }
}
