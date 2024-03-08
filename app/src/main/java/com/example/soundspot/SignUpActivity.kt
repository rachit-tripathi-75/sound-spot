package com.example.soundspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.soundspot.databinding.ActivitySignInBinding
import com.example.soundspot.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    public var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivitySignUpBinding
    private var isLoading: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSignUp.setOnClickListener {
            if(isValidSignUp() == true) {
                signUp()
            }
        }

    }

    private fun signUp() {
        if (isLoading == true) {
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                binding.btnSignUp.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                finish()
            } else {
                binding.btnSignUp.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidSignUp(): Boolean {
        if(binding.etName.text.toString().isEmpty() == true) {
            binding.etName.error = "Enter name"
            return false
        }
        else if(binding.etEmail.text.toString().isEmpty() == true) {
            binding.etEmail.error = "Enter the email"
            return false
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches() == false) {
            binding.etEmail.error = "Enter valid email"
            return false
        }
        else if(binding.etPassword.text.toString().isEmpty() == true) {
            binding.etPassword.error = "Enter the password"
            return false
        }
        return true
    }
}