package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;
    private ActivityOptionsCompat options;
    private String packageName = "com.rachit.tripathi75.soundspot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        listeners();
        playSplashScreenVideo();

    }

    private void listeners() {
        binding.btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
        });


        binding.btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SplashScreenActivity.this, CreateAccountActivity.class);
            startActivity(intent, options.toBundle());
        });

        binding.llHeadToHome.setOnClickListener(view -> {
            startActivity(new Intent(SplashScreenActivity.this, HostActivity.class));
        });

        binding.tvContinueAsGuest.setOnClickListener(view -> {
            startActivity(new Intent(this, HostActivity.class));
            finish();
        });

    }

    private void playSplashScreenVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashscreenvideo);
        binding.videoView.setVideoURI(videoUri);
        binding.videoView.requestFocus();
        binding.videoView.setOnCompletionListener(view -> {
            binding.videoView.start();
        });
        binding.videoView.start();
    }
}