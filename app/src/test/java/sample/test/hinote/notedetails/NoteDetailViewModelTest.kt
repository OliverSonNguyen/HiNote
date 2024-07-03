package sample.test.hinote.notedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sample.test.hinote.home.data.NoteRepository
import sample.test.hinote.home.data.local.Note

class NoteDetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NoteDetailViewModel

    private var noteRepository: NoteRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = NoteDetailViewModel(noteRepository)
    }

    @Test
    //viewModel.loadNote(noteId)
    fun `when loadNote load valid NoteId, then uiState value should contain the Note getting from load that note id`() =
        runTest {
            //given
            val noteId = 1L
            val mockNote = Note("noteA", "contentA", id = noteId)
            coEvery { noteRepository.getNote(noteId) } returns mockNote
            //when
            viewModel.loadNote(noteId)
            //then
            val expected = NoteDetailViewModel.UiState.UiStateView(mockNote)
            Truth.assertThat(viewModel.uiState.value).isEqualTo(expected)
        }

    @Test
    fun `when title updated, then it should be update in the database`() = runTest {
        //given
        val noteId = 1L
        viewModel.editMode()
        coEvery { noteRepository.insert(any()) } returns noteId
        //when
        viewModel.onTitleUpdated("this is new title")
        viewModel.onContentUpdated("this is new content")
        advanceTimeBy(600)
        //then
        coVerify(exactly = 1) {
            noteRepository.insert(withArg { note: Note ->
                assert(note.title == "this is new title")
                assert(note.content == "this is new content")
                assert(note.createdDate != null)
                assert(note.updatedDate != null)
            })
        }
        Truth.assertThat(viewModel.uiState.value)
            .isEqualTo(NoteDetailViewModel.UiState.UiStateSaved)

    }

    @Test
    fun deleteNote() = runTest{
        //given
        val currentNodeId = 1L
        viewModel.editMode()
        coEvery { noteRepository.insert(any()) } returns currentNodeId
        coEvery { noteRepository.deleteById(any()) } just Runs
        viewModel.onTitleUpdated("this is new title")
        viewModel.onContentUpdated("this is new content")
        advanceUntilIdle()

                //when
        viewModel.deleteNote()
        advanceUntilIdle()
        //then
        coVerify(exactly = 1) {
            noteRepository.deleteById(withArg { id: Long ->
                assert(id == currentNodeId)
            })
        }
        Truth.assertThat(viewModel.uiState.value)
            .isEqualTo(NoteDetailViewModel.UiState.UiStateDelete(currentNodeId))
    }


}