package com.hyejis.booksearchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hyejis.booksearchapp.databinding.ActivityMainBinding
import com.hyejis.booksearchapp.ui.view.FavoriteFragment
import com.hyejis.booksearchapp.ui.view.SearchFragment
import com.hyejis.booksearchapp.ui.view.SettingsFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigationView()
        if (savedInstanceState == null) {  //앱이 처음 실행되었을 경우에만!
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SearchFragment()).commit()
                    true
                }
                R.id.fragment_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FavoriteFragment()).commit()
                    true
                }
                R.id.fragment_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SettingsFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}