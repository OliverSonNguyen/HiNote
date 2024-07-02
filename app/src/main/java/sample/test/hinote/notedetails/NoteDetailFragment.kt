package sample.test.hinote.notedetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sample.test.hinote.R
import sample.test.hinote.databinding.FragmentNodeDetailBinding

class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNodeDetailBinding
    private val viewModel: NoteDetailViewModel by viewModels {
        NodeDetailViewModelFactory(requireContext())
    }

    companion object {
        private const val ARG_NOTE_ID = "note_id"

        fun newInstance(noteId: Long): NoteDetailFragment {
            val args = Bundle().apply {
                putLong(ARG_NOTE_ID, noteId)
            }
            val fragment = NoteDetailFragment()
            fragment.arguments = args
            return fragment
        }
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
        setupToolbar()
        initView()
        observe()
        start()

    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_note_detail)
        binding.toolbar.setOnMenuItemClickListener { item ->
            Log.d("", ">>>onOptionsItemSelected item:$item")
            when (item.itemId) {
                R.id.action_edit -> {
                    // Handle Edit action
                    binding.edtTitle.isEnabled = true
                    binding.edtContent.isEnabled = true
                    binding.edtContent.requestFocus()
                    viewModel.editMode()
                    true
                }

                R.id.action_delete -> {
                    // Handle Delete action
                    viewModel.deleteNote()
                    true
                }

                else -> false
            }
        }
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.edtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onTitleUpdated(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.edtContent.addTextChangedListener(object : TextWatcher {
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
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is NoteDetailViewModel.UiState.UiStateView -> {
                        binding.edtTitle.setText(uiState.node.title)
                        binding.edtContent.setText(uiState.node.content)
                        binding.txtState.text = ""
                    }

                    is NoteDetailViewModel.UiState.UiStateEdit -> {
                        binding.txtState.text = uiState.message ?: ""
                    }
                    is NoteDetailViewModel.UiState.UiStateDelete -> {
                        Toast.makeText(requireContext(), "Deleted ${uiState.id}", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }

    private fun start() {
        val noteId = arguments?.getLong(ARG_NOTE_ID)
        noteId?.let {
            viewModel.loadNote(noteId)
            binding.edtTitle.isEnabled = false
            binding.edtContent.isEnabled = false
        } ?: kotlin.run {
            binding.toolbar.menu.findItem(R.id.action_edit).isVisible = false
            binding.edtContent.requestFocus()
        }

    }
}