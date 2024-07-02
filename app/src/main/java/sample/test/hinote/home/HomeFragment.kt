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
import sample.test.hinote.databinding.FragmentHomeBinding
import sample.test.hinote.home.view.adapter.HomeAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
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
        }
        binding.homeRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeRcv.adapter = adapter
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

}