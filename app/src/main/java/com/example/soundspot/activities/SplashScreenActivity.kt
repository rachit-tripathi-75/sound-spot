package com.example.soundspot.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.soundspot.R
import com.example.soundspot.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var options: ActivityOptionsCompat
    private var packageName: String = "com.example.soundspot"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
        listeners()
        playSplashScreenVideo()


    }

    private fun listeners() {

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent, options.toBundle())
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent, options.toBundle())
        }

        binding.llHeadToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playSplashScreenVideo() {
        val videoUri = Uri.parse("android.resource://${packageName}/raw/splashscreenvideo")
        binding.videoView.setVideoURI(videoUri)
        binding.videoView.requestFocus()
        binding.videoView.setOnCompletionListener {
            binding.videoView.start()
        }
        binding.videoView.start()

    }

    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}