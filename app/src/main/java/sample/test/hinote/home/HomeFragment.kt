package sample.test.hinote.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        viewModel.loadNotes(true)
    }

    private fun initView() {
        binding.btnAdd.setOnClickListener {
            navigateToNoteDetailFragment()
        }
        adapter = HomeAdapter { item ->
            Log.d("", ">>>item click:$item")
            navigateToNoteDetailFragment(item)
        }
        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                binding.homeRcv.scrollToPosition(0)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.homeRcv.scrollToPosition(0)
            }
        }
        adapter.registerAdapterDataObserver(observer)
        binding.homeRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeRcv.adapter = adapter
        binding.homeRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    viewModel.loadNotes()
                }
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
//                    viewModel.loadNotes()
//                }
            }
        })
//        parentFragmentManager.addOnBackStackChangedListener {
//            if (parentFragmentManager.backStackEntryCount == 0) {
//                Log.d("",">>>backStackEntryCount == 0 hash:${hashCode()} - vmhash:${viewModel.hashCode()}")
//                viewModel.loadNotes(true)
//            }
//        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                Log.d("", ">>>uiState:$uiState")
                when (uiState) {
                    is HomeViewModel.UiState.UiStateLoading -> {
                        binding.loadingIndicator.isVisible = true
                    }

                    is HomeViewModel.UiState.UiStateLoaded -> {
                        binding.loadingIndicator.isVisible = false
                        adapter.submitList(uiState.items)
                    }

                    is HomeViewModel.UiState.UiStateError -> {
                        binding.loadingIndicator.isVisible = false
                        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

    private fun navigateToNoteDetailFragment(note: Note? = null) {
        val fragment =
            if (note != null) NoteDetailFragment.newInstance(note.id) else NoteDetailFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.homeContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}