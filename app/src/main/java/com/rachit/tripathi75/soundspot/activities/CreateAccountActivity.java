package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.SingersViewPagerAdapter;
import com.rachit.tripathi75.soundspot.classes.DissolvePageTransformer;
import com.rachit.tripathi75.soundspot.databinding.ActivityCreateAccountBinding;

import java.util.Arrays;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;
    private ActivityOptionsCompat options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        binding.progressBar.setVisibility(View.GONE);
        listeners();
        loadSingerViewPager();
        showingViewAsUserEnters();
    }

    private void listeners() {
        binding.tvLoginInAnnotation.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
            finish();
        });

    }

    private void loadSingerViewPager() {
        List<Integer> singersImagesList = Arrays.asList(
                R.drawable.taylorswift,
                R.drawable.arijitsingh,
                R.drawable.arianagrande,
                R.drawable.drake,
                R.drawable.theweeknd,
                R.drawable.lanadelrey,
                R.drawable.krsna,
                R.drawable.karanaujla,
                R.drawable.darshanraval
        );

        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAlpha(0.4f);

        if (binding.viewPager.getAdapter() == null) {
            binding.viewPager.setAdapter(new SingersViewPagerAdapter(singersImagesList));
        }

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.viewPager.getCurrentItem();
                int nextItem = (currentItem == singersImagesList.size() - 1) ? 0 : currentItem + 1;
                binding.viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        binding.viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setAlpha(0.3f + (1 - Math.abs(position)));
                page.setScaleY(0.85f + (1 - Math.abs(position)) * 0.15f);
            }
        });

        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setPageTransformer(new DissolvePageTransformer());
    }

    private void showingViewAsUserEnters() {
        // to make etPassword appear


        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    if (s.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        if(binding.etPassword.getVisibility() == View.GONE) {
                            slideDownAnimation(binding.etPassword);
                            binding.etPassword.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.etPassword.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    if (!s.isEmpty() && s.length() >= 6) {
                        if (binding.btnSignIn.getVisibility() == View.GONE) {
                            slideDownAnimation(binding.btnSignIn);
                            binding.btnSignIn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.btnSignIn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void slideDownAnimation(View view) {
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        view.startAnimation(slideDown);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}