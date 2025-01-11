package com.example.soundspot.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.soundspot.R
import com.example.soundspot.adapters.SingersViewPagerAdapter
import com.example.soundspot.classes.DissolvePageTransformer
import com.example.soundspot.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var options: ActivityOptionsCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
        binding.progressBar.visibility = View.GONE
        listeners()
        loadSingerViewPager()
        showingViewAsUserEnters()
    }

    private fun listeners() {
        binding.tvSignUpAnnotation.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent, options.toBundle())
            finish()
        }
    }

    private fun showingViewAsUserEnters() {


        // to make etPassword appear
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    if (binding.etPassword.visibility == View.GONE) {
                        slideDownAnimation(binding.etPassword)
                        binding.etPassword.visibility = View.VISIBLE
                    }
                } else {
                    binding.etPassword.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        // to make sign in button appear
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length >= 6) {
                    if (binding.btnSignIn.visibility == View.GONE) {
                        slideDownAnimation(binding.btnSignIn)
                        binding.btnSignIn.visibility = View.VISIBLE
                    }
                } else {
                    binding.btnSignIn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun slideDownAnimation(view: View) {
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        view.startAnimation(slideDown)
    }

    private fun isValidEmail(emailString: String): Any {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return Pattern.compile(emailRegex).matcher(emailString).matches()

    }

//    private fun startAutoScroll() {
//        val singersImagesList = listOf(R.drawable.theweeknd, R.drawable.arijitsingh)
//        val handler = Handler(Looper.getMainLooper())
//        val update = object : Runnable {
//            override fun run() {
//                val currentItem = binding.viewPager.currentItem
//                val nextItem = if (currentItem == singersImagesList.size - 1) 0 else currentItem + 1
//                binding.viewPager.setCurrentItem(nextItem, true)
//                // Re-run the handler after 3 seconds
//                handler.postDelayed(this, 3000)
//            }
//        }
//        handler.postDelayed(update, 3000)
//    }

    private fun loadSingerViewPager() {
        val singersImagesList = listOf(
            R.drawable.taylorswift,
            R.drawable.arijitsingh,
            R.drawable.arianagrande,
            R.drawable.drake,
            R.drawable.theweeknd,
            R.drawable.lanadelrey,
            R.drawable.krsna,
            R.drawable.karanaujla,
            R.drawable.darshanraval
        )
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.alpha = 0.4f
        if (binding.viewPager.adapter == null) {
            binding.viewPager.adapter = SingersViewPagerAdapter(singersImagesList)
        }

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = binding.viewPager.currentItem
                val nextItem = if (currentItem == singersImagesList.size - 1) 0 else currentItem + 1
                binding.viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
        binding.viewPager.setPageTransformer { page, position ->
            page.alpha = 0.3f + (1 - Math.abs(position))
            page.scaleY = 0.85f + (1 - Math.abs(position)) * 0.15f
        }
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.setPageTransformer(DissolvePageTransformer())

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}