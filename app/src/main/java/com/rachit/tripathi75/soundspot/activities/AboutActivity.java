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
        binding.discord.setOnClickListener(view -> Toast.makeText(AboutActivity.this, "Oops, No Discord Server found.", Toast.LENGTH_SHORT).show());
        binding.instagram.setOnClickListener(view -> openUrl("https://www.instagram.com/harsh_.s._shah/"));
        binding.telegram.setOnClickListener(view -> openUrl("https://t.me/legendary_streamer_official"));

    }

    private void openUrl(final String url){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
    }
}