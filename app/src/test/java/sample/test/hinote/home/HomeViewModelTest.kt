package sample.test.hinote.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sample.test.hinote.home.data.NoteRepository
import sample.test.hinote.home.data.local.Note

class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel

    private var repo: NoteRepository = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    val notes = mutableListOf<Note>()
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        for (i in 1 .. 60) {
            notes.add(Note("note$i", "content$i", id = i.toLong()))
        }
        coEvery { repo.getAllNotes() } returns notes
        viewModel = HomeViewModel(repo)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load 20 notes from db`() = runTest {
        //when
        //init
        // Then
        coVerify { repo.getAllNotes() }
        val expected = HomeViewModel.UiState.UiStateLoaded(notes.take(20))
        Truth.assertThat(viewModel.uiState.value).isEqualTo(expected)
    }


    @Test
    fun `loadMore should append more notes`() = runTest {
        // When
        viewModel.loadMore()
        // Then
        coVerify { repo.getAllNotes() }
        val expectedLoadedItems = notes.take(PAGING * 2)
        val expected = HomeViewModel.UiState.UiStateLoaded(expectedLoadedItems)
        Truth.assertThat(viewModel.uiState.value).isEqualTo(expected)
    }
}