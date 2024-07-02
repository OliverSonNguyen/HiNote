package sample.test.hinote.notedetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sample.test.hinote.databinding.FragmentNodeDetailBinding

class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNodeDetailBinding
    private val viewModel: NoteDetailViewModel by viewModels {
        NodeDetailViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
    }
    private fun initView() {
        binding.edtTitle.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onTitleUpdated(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.edtTitleContent.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onContentUpdated(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState->

            }
        }

    }
}