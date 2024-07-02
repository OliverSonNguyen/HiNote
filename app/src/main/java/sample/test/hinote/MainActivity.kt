package sample.test.hinote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import sample.test.hinote.databinding.ActivityMainBinding
import sample.test.hinote.home.HomeFragment
import sample.test.hinote.notedetails.NoteDetailFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        init()
    }
    private fun init() {
        val fm = supportFragmentManager
        fm.beginTransaction()
            .add(R.id.homeContainer, HomeFragment(), HomeFragment::class.java.name)
            .commitNow()
    }

    private fun initView() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                binding.btnAdd.visibility = View.VISIBLE
            }
        }
        binding.btnAdd.setOnClickListener {
            val fm = supportFragmentManager
            val homeFragment = fm.findFragmentByTag(HomeFragment::class.java.name)
            val noteDetailFragment = fm.findFragmentByTag(NoteDetailFragment::class.java.name)

            fm.beginTransaction().apply {
                if (homeFragment != null) {
                    hide(homeFragment)
                }
                if (noteDetailFragment == null) {
                    add(R.id.homeContainer, NoteDetailFragment(), NoteDetailFragment::class.java.name)
                } else {
                    show(noteDetailFragment)
                }
                addToBackStack(NoteDetailFragment::class.java.name)
                commit()
            }
            binding.btnAdd.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            if (supportFragmentManager.backStackEntryCount == 1) {
                binding.btnAdd.visibility = View.VISIBLE
            }
        } else {
            super.onBackPressed()
        }
    }
}