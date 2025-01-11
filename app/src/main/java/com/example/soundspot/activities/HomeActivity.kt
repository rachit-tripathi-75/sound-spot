package com.example.soundspot.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.example.soundspot.R
import com.example.soundspot.databinding.ActivityHomeBinding
import com.example.soundspot.fragments.HomeFragment
import com.example.soundspot.fragments.ImportPlaylistFragment
import com.example.soundspot.fragments.LibraryFragment
import com.example.soundspot.fragments.SearchFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
        binding.bottomBar.setOnItemSelectedListener { itemId ->
            when (itemId) {
                R.id.itemHome -> replaceFragment(HomeFragment())
                R.id.itemSearch -> replaceFragment(SearchFragment())
                R.id.itemLibrary -> replaceFragment(LibraryFragment())
                R.id.itemImportPlaylist -> replaceFragment(ImportPlaylistFragment())
            }
        }

    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

}