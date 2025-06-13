package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ChooseArtistAdapter;
import com.rachit.tripathi75.soundspot.classes.ChooseArtist;
import com.rachit.tripathi75.soundspot.classes.PrefsManager;
import com.rachit.tripathi75.soundspot.databinding.ActivityArtistSelectionBinding;
import com.rachit.tripathi75.soundspot.model.UserDetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseArtistSelectionActivity extends AppCompatActivity implements ChooseArtistAdapter.OnArtistClickListener {

    private ActivityArtistSelectionBinding binding;
    private ChooseArtistAdapter chooseArtistAdapter;

    private final List<ChooseArtist> allChooseArtists = new ArrayList<>();
    private final List<ChooseArtist> relatedChooseArtists = new ArrayList<>();
    private final List<ChooseArtist> displayedChooseArtists = new ArrayList<>();

    private final Set<String> selectedChooseArtistIds = new HashSet<>();
    private String showingRelatedFor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityArtistSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // RecyclerView setup
        binding.artistsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        chooseArtistAdapter = new ChooseArtistAdapter(displayedChooseArtists, this);
        binding.artistsRecyclerView.setAdapter(chooseArtistAdapter);
        binding.artistsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Button click
        binding.continueButton.setOnClickListener(v -> {
            if (!selectedChooseArtistIds.isEmpty()) {
                binding.continueButton.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                showChosenArtists(selectedChooseArtistIds);
                storeChosenArtistsToFirebase();
            } else {
                Toast.makeText(this, "Please select at least one artist", Toast.LENGTH_SHORT).show();
            }
        });

        setupChooseArtistData();
        updateDisplayedChooseArtists();
    }

    private void storeChosenArtistsToFirebase() {
        Gson gson = new Gson();
        UserDetails user = PrefsManager.getUserDetails(this);
        Log.d("userData", gson.toJson(user));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        List<String> chosenArtistIds = new ArrayList<>(selectedChooseArtistIds);

        db.child("users").child(user.getId()).child("followedArtistsId").setValue(chosenArtistIds)
                .addOnSuccessListener(v -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.continueButton.setVisibility(View.VISIBLE);
                    startActivity(new Intent(ChooseArtistSelectionActivity.this, HostActivity.class));
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.continueButton.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Error occurred. Try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showChosenArtists(Set<String> ids) {
        for (String id : ids) {
            Log.d("artist_selected", id);
        }
    }

    private void setupChooseArtistData() {
        allChooseArtists.clear();
        relatedChooseArtists.clear();

        allChooseArtists.add(new ChooseArtist("459320", "Arijit Singh", "https://c.saavncdn.com/artists/Arijit_Singh_004_20241118063717_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("712878", "Guru Randhawa", "https://c.saavncdn.com/artists/Guru_Randhawa_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("697691", "Karan Aujla", "https://c.saavncdn.com/artists/Karan_Aujla_002_20250228125836_500x500.jpg"));
//        allChooseArtists.add(new ChooseArtist("456863", "Badshah", "https://c.saavncdn.com/artists/Badshah_006_20241118064015_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("458918", "Raftaar", "https://c.saavncdn.com/artists/Raftaar_009_20230223100912_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("455130", "Shreya Ghoshal", "https://c.saavncdn.com/artists/Shreya_Ghoshal_007_20241101074144_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("455137", "Tulsi Kumar", "https://c.saavncdn.com/artists/Tulsi_Kumar_005_20250124104447_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("3811097", "Kr$na", "https://c.saavncdn.com/artists/KR_NA_004_20240722131412_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("653605", "Divine", "https://c.saavncdn.com/artists/DIVINE_005_20240307055212_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("14327531", "King", "https://c.saavncdn.com/artists/King_004_20240819092014_500x500.jpg"));

        relatedChooseArtists.add(new ChooseArtist("4878402", "Anuv Jain", "https://c.saavncdn.com/artists/Anuv_Jain_001_20231206073013_500x500.jpg", "1"));
        relatedChooseArtists.add(new ChooseArtist("3623111", "Parampara Tandon", "https://c.saavncdn.com/artists/Parampara_Thakur_20191130070743_500x500.jpg", "1"));
        relatedChooseArtists.add(new ChooseArtist("1546334", "Prateek Kuhad", "https://c.saavncdn.com/artists/Prateek_Kuhad_005_20230506101352_500x500.jpg", "1"));
        relatedChooseArtists.add(new ChooseArtist("681966", "AP Dhillon", "https://c.saavncdn.com/artists/AP_Dhillon_002_20230918073841_500x500.jpg", "2"));
        relatedChooseArtists.add(new ChooseArtist("672167", "Harrdy Sandhu", "https://c.saavncdn.com/artists/Hardy_Sandhu_001_20190913112018_500x500.jpg", "2"));
        relatedChooseArtists.add(new ChooseArtist("485955", "Shubh", "https://c.saavncdn.com/artists/Shubh_000_20220921112507_500x500.jpg", "2"));
        relatedChooseArtists.add(new ChooseArtist("706985", "Asees Kaur", "https://c.saavncdn.com/artists/Asees_Kaur_007_20250303070610_500x500.jpg", "3"));
        relatedChooseArtists.add(new ChooseArtist("3623109", "Dhwani Bhanushali", "https://c.saavncdn.com/artists/Dhvani_Bhanushali_001_20241015090250_500x500.jpg", "3"));
        relatedChooseArtists.add(new ChooseArtist("5235796", "Iqlipse Nova", "https://c.saavncdn.com/artists/Iqlipse_Nova_000_20230727101616_500x500.jpg", "3"));
    }

    private void updateDisplayedChooseArtists() {
        displayedChooseArtists.clear();

        if (showingRelatedFor == null) {
            displayedChooseArtists.addAll(allChooseArtists);
        } else {
            for (ChooseArtist artist : allChooseArtists) {
                if (!artist.getId().equals(showingRelatedFor)) {
                    displayedChooseArtists.add(artist);
                }
            }
            for (ChooseArtist artist : relatedChooseArtists) {
                if (artist.getRelatedTo().equals(showingRelatedFor)) {
                    displayedChooseArtists.add(artist);
                }
            }
        }

        for (ChooseArtist artist : displayedChooseArtists) {
            artist.setSelected(selectedChooseArtistIds.contains(artist.getId()));
        }

        chooseArtistAdapter.updateData(new ArrayList<>(displayedChooseArtists));
    }

    @Override
    public void onArtistClick(ChooseArtist artist, int position) {
        String id = artist.getId();
        if (selectedChooseArtistIds.contains(id)) {
            selectedChooseArtistIds.remove(id);
            artist.setSelected(false);
            if (id.equals(showingRelatedFor)) {
                showingRelatedFor = null;
                updateDisplayedChooseArtists();
            } else {
                chooseArtistAdapter.notifyItemChanged(position);
            }
        } else {
            selectedChooseArtistIds.add(id);
            artist.setSelected(true);
            boolean isMain = allChooseArtists.stream().anyMatch(a -> a.getId().equals(id));
            if (isMain) {
                showingRelatedFor = id;
                updateDisplayedChooseArtists();
            } else {
                chooseArtistAdapter.notifyItemChanged(position);
            }
        }
    }
}
