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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.SingersViewPagerAdapter;
import com.rachit.tripathi75.soundspot.classes.DissolvePageTransformer;
import com.rachit.tripathi75.soundspot.classes.PrefsManager;
import com.rachit.tripathi75.soundspot.databinding.ActivityCreateAccountBinding;
import com.rachit.tripathi75.soundspot.model.UserDetails;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;
    private ActivityOptionsCompat options;
    private FirebaseAuth firebaseAuth;
    String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!*]).{6,}$";
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;
    private CallbackManager callbackManager;

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

        initialisers();
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
        binding.tvLoginInAnnotation.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(intent, options.toBundle());
            finish();
        });

        binding.btnRegister.setOnClickListener(view -> {
            if (isValidDetails()) {
                registerUser();
            }
        });

        binding.vgGoogle.setOnClickListener(view -> {
            signInWithGoogle();
        });

        binding.vgInstagram.setOnClickListener(view -> {

        });

        binding.vgX.setOnClickListener(view -> {
            PrefsManager.setLoginInType(CreateAccountActivity.this, 4);
            Toast.makeText(this, "Feature under development.", Toast.LENGTH_SHORT).show();
        });

    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void registerUser() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setVisibility(View.INVISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnRegister.setVisibility(View.VISIBLE);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        UserDetails userDetails = new UserDetails(cleanEmail(user.getEmail()), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
                        storeDataToFirebase("credential");
                        PrefsManager.setSession(CreateAccountActivity.this, true); // set session true as user has logged in!!
                        PrefsManager.setUserDetails(CreateAccountActivity.this, userDetails);
                        PrefsManager.setLoginInType(CreateAccountActivity.this, 1);
                        startActivity(new Intent(CreateAccountActivity.this, HostActivity.class));
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnRegister.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Email already registered. Try with a different one", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "An error occurred. Please try again later" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean isValidDetails() {
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            binding.etEmail.setError("Please enter a valid email");
            return false;
        } else if (!binding.etPassword.getText().toString().matches(passwordRegex)) {
            binding.etPassword.setError("A password must have at least: \n1. Have a length of minimum 6 characters\n2. A capital letter [A-Z]\n3. A small letter [a-z]\n3. A digit [0-9]\n4. A special character");
            return false;
        }
        return true;
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
                    if (binding.btnRegister.getVisibility() == View.GONE) {
                        slideDownAnimation(binding.btnRegister);
                        binding.btnRegister.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.btnRegister.setVisibility(View.GONE);
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
                        UserDetails userDetails = new UserDetails(cleanEmail(user.getEmail()), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
                        PrefsManager.setSession(CreateAccountActivity.this, true);
                        PrefsManager.setUserDetails(CreateAccountActivity.this, userDetails);
                        PrefsManager.setLoginInType(CreateAccountActivity.this, 2);
                        storeDataToFirebase("google");
                        startActivity(new Intent(CreateAccountActivity.this, ChooseArtistSelectionActivity.class));
                    } else {
                        Toast.makeText(this, "Authentication failed. Choose a different account", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                });
    }

    private void storeDataToFirebase(String signInMethod) {
        FirebaseAuth firebaseAuth = ApplicationClass.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> mp = new HashMap<>();
        mp.put("name", user.getDisplayName());
        mp.put("email", user.getEmail());
        mp.put("method", signInMethod);

        if (user.getPhotoUrl() != null) {
            mp.put("photo", user.getPhotoUrl().toString());
        }

        db.child("users").child(cleanEmail(user.getEmail())).setValue(mp);
    }

    private String cleanEmail(String email) {

        if (email == null) {
            return "";
        }

        StringBuilder cleanEmailString = new StringBuilder();
        final char[] FORBIDDEN_CHARS = {'.', '#', '$', '[', ']'};

        for (char character : email.toCharArray()) {
            boolean isForbidden = false;

            for (char forbiddenChar : FORBIDDEN_CHARS) {
                if (character == forbiddenChar) {
                    isForbidden = true;
                    break;
                }
            }
            if (!isForbidden) {
                cleanEmailString.append(character);
            }
        }


        return cleanEmailString.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}