package com.example.soundspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.soundspot.databinding.ActivityHomeBinding
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivProfilePic.setOnClickListener {
            signOut()
        }

        var bottomNav = binding.bottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.importPlaylist -> {
                    replaceFragment(ImportPlaylistFragment())
                    true
                }
                R.id.library -> {
                    replaceFragment(LibraryFragment())
                    true
                }
            }
            true
        }
        replaceFragment(HomeFragment()) // with this, the home screen will be displayed automatically clicking on it beforehand
        replacePlayerFragment(PlayerFragment())

        binding.flPlayerContainer.setOnClickListener {
//            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_up)
//            overridePendingTransition(R.anim.slide_up, 0)
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))

        }
    }

    private fun signOut() {
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
        finish()
    }

    private fun replacePlayerFragment(playerFragment: PlayerFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.flPlayerContainer, playerFragment).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
