package sample.test.hinote.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import sample.test.hinote.R
import sample.test.hinote.databinding.FragmentHomeBinding
import sample.test.hinote.home.data.local.Note
import sample.test.hinote.home.tool.HomeViewModelFactory
import sample.test.hinote.home.view.adapter.HomeAdapter
import sample.test.hinote.notedetails.NoteDetailFragment

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }
    private lateinit var adapter: HomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        viewModel.start()
    }

    private fun initView() {
        adapter = HomeAdapter { item ->
            Log.d("", ">>>item click:$item")
            navigateToNoteDetailFragment(item)
        }
        binding.homeRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeRcv.adapter = adapter
        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                viewModel.start()
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                Log.d("", ">>>uiState:$uiState")
                when (uiState) {

                    is HomeViewModel.UiState.UiStateLoading -> {

                    }

                    is HomeViewModel.UiState.UiStateLoaded -> {
                        adapter.submitList(uiState.items)
                    }

                    is HomeViewModel.UiState.UiStateError -> {

                    }
                }
            }
        }

    }

    private fun navigateToNoteDetailFragment(note: Note) {
        val fragment = NoteDetailFragment.newInstance(note.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.homeContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

}