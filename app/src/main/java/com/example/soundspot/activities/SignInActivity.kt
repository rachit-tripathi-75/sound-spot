package com.example.soundspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.soundspot.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    public var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.tvSignUpAnnotation.setOnClickListener {
//            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
//        }
        binding.btnSignIn.setOnClickListener {
            var isLoading: Boolean = true
            if(isValidSignIn() == true) {
                signIn(binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.progressBar, isLoading, binding.btnSignIn)
            }

        }


    }

    private fun isValidSignIn(): Boolean {

        if(binding.etEmail.text.toString().isEmpty() == true) {
            binding.etEmail.error = "Enter the email"
            return false;
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches() == false) {
            binding.etEmail.error = "Enter valid email"
            return false;
        }
        else if(binding.etPassword.text.toString().isEmpty() == true) {
            binding.etPassword.error = "Enter the password"
            return false;
        }
        return true;

    }

    private fun signIn(email: String, password: String, progressBar: ProgressBar, isLoading: Boolean, btnSignIn: Button) {
        if (isLoading == true) {
            binding.btnSignIn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                btnSignIn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                finish()
            } else {
                btnSignIn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
//            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }
    }
}