package com.hyejis.booksearchapp.ui.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hyejis.booksearchapp.R
import com.hyejis.booksearchapp.data.db.BookSearchDatabase
import com.hyejis.booksearchapp.data.repository.BookSearchRepositoryImpl
import com.hyejis.booksearchapp.databinding.ActivityMainBinding
import com.hyejis.booksearchapp.ui.viewmodel.BookSearchViewModel
import com.hyejis.booksearchapp.ui.viewmodel.BookSearchViewModelProviderFactory
import com.hyejis.booksearchapp.util.Constants.DATASTORE_NAME

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var bookSearchViewModel: BookSearchViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        setupBottomNavigationView()
//        if (savedInstanceState == null) {  //앱이 처음 실행되었을 경우에만!
//            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
//        }

        setupJetpackNavigation()

        val database = BookSearchDatabase.getInstance(this)
        val bookSearchRepository = BookSearchRepositoryImpl(database, dataStore)

        val factory = BookSearchViewModelProviderFactory(bookSearchRepository, this)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]
    }

    private fun setupJetpackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        // 앱바에 변화되는 화면 타이틀 표시
        appBarConfiguration = AppBarConfiguration(
//            navController.graph
            //모든 프래그먼트가 top level destination으로 지정되어 화살표 표시가 안됨
            setOf(
                R.id.fragment_search, R.id.fragment_favorite, R.id.fragment_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun setupBottomNavigationView() {
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.fragment_search -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, SearchFragment()).commit()
//                    true
//                }
//                R.id.fragment_favorite -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, FavoriteFragment()).commit()
//                    true
//                }
//                R.id.fragment_settings -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, SettingsFragment()).commit()
//                    true
//                }
//                else -> false
//            }
//        }
//    }
}