package sample.test.hinote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sample.test.hinote.databinding.ActivityMainBinding
import sample.test.hinote.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init() {
        val fm = supportFragmentManager
        fm.beginTransaction().add(R.id.homeContainer, HomeFragment())
            .commitNow()
    }
}