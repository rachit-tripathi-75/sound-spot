package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rachit.tripathi75.soundspot.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        binding.email.setOnClickListener(view -> openUrl("mailto:harshsandeep23@gmail.com"));
        binding.sourceCode.setOnClickListener(view -> openUrl("https://github.com/harshshah6/SaavnMp3-Android"));
        binding.github.setOnClickListener(view -> openUrl("https://github.com/rachit-tripathi-75"));
        binding.x.setOnClickListener(view -> openUrl("https://x.com/rtripatthi75"));
        binding.linkedin.setOnClickListener(view -> openUrl("https://www.linkedin.com/in/rachittripathi75/"));


        binding.sourceCode.setOnClickListener(view -> openUrl("https://github.com/rachit-tripathi-75/sound-spot"));

        binding.rate.setOnClickListener(view -> showToast("Not yet uploaded to play store!!!"));

        binding.email.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:rachit.tripathi.75@gmail.com"));

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void openUrl(final String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}