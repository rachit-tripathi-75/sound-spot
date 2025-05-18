package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.SingersViewPagerAdapter;
import com.rachit.tripathi75.soundspot.classes.DissolvePageTransformer;
import com.rachit.tripathi75.soundspot.classes.PrefsManager;
import com.rachit.tripathi75.soundspot.databinding.ActivityLoginBinding;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ActivityOptionsCompat options;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialisers();
        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        binding.progressBar.setVisibility(View.GONE);
        listeners();
        loadSingerViewPager();
        showingViewAsUserEnters();

    }

    private void initialisers() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        firebaseAuth = ApplicationClass.getFirebaseAuth(); // for google sign in
        callbackManager = CallbackManager.Factory.create(); // for facebook sign in
        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void listeners() {
        binding.tvSignUpAnnotation.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
            finish();
        });

        binding.btnSignIn.setOnClickListener(view -> {
            if (isValidDetails()) {
                signIn();
            }
        });

        binding.vgFacebook.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NonNull FacebookException e) {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.vgApple.setOnClickListener(view -> {
            PrefsManager.setLoginInType(LoginActivity.this, 4);
            Toast.makeText(this, "Feature under development", Toast.LENGTH_SHORT).show();
        });

        binding.vgGoogle.setOnClickListener(view -> {
            signInWithGoogle();
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        PrefsManager.setSession(LoginActivity.this, true); // set session true as user has logged in!!
                        PrefsManager.setUserDetails(LoginActivity.this, user);
                        PrefsManager.setLoginInType(LoginActivity.this, 3);
                        startActivity(new Intent(LoginActivity.this, HostActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "An error occurred. Please try again later", Toast.LENGTH_SHORT).show();
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isValidDetails() {
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            binding.etEmail.setError("Please enter a valid email");
            return false;
        }
        return true;
    }

    private void signIn() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSignIn.setVisibility(View.INVISIBLE);
        firebaseAuth.signInWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnSignIn.setVisibility(View.VISIBLE);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        PrefsManager.setSession(LoginActivity.this, true); // set session true as user has logged in!!
                        PrefsManager.setUserDetails(LoginActivity.this, user);
                        PrefsManager.setLoginInType(LoginActivity.this, 1);
                        startActivity(new Intent(LoginActivity.this, HostActivity.class));

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnSignIn.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnSignIn.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "An error occurred. Please try again later" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                if (!s.toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    if (binding.cvPassword.getVisibility() == View.GONE) {
                        slideDownAnimation(binding.cvPassword);
                        binding.cvPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.cvPassword.setVisibility(View.GONE);
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
                if (!s.toString().isEmpty() && s.length() >= 6) {
                    if (binding.btnSignIn.getVisibility() == View.GONE) {
                        slideDownAnimation(binding.btnSignIn);
                        binding.btnSignIn.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.btnSignIn.setVisibility(View.GONE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(Exception.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        PrefsManager.setSession(LoginActivity.this, true); // set session true as user has logged in!!
                        PrefsManager.setUserDetails(LoginActivity.this, user);
                        PrefsManager.setLoginInType(LoginActivity.this, 2);
                        startActivity(new Intent(LoginActivity.this, HostActivity.class));
                    } else {
                        Toast.makeText(this, "Authentication failed. Choose a different account", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}