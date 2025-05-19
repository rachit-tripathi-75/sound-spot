package com.rachit.tripathi75.soundspot.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ChooseArtistAdapter;
import com.rachit.tripathi75.soundspot.classes.ChooseArtist;
import com.rachit.tripathi75.soundspot.databinding.ActivityArtistSelectionBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseArtistSelectionActivity extends AppCompatActivity implements ChooseArtistAdapter.OnArtistClickListener {

    private ActivityArtistSelectionBinding binding;

    private ChooseArtistAdapter ChooseArtistAdapter;

    private List<ChooseArtist> allChooseArtists = new ArrayList<>();
    private List<ChooseArtist> relatedChooseArtists = new ArrayList<>();
    private List<ChooseArtist> displayedChooseArtists = new ArrayList<>();

    private String showingRelatedFor = null;
    private Set<String> selectedChooseArtistIds = new HashSet<>();

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


        // Setup RecyclerView
        binding.artistsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ChooseArtistAdapter = new ChooseArtistAdapter(displayedChooseArtists, (com.rachit.tripathi75.soundspot.adapters.ChooseArtistAdapter.OnArtistClickListener) this);
        binding.artistsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.artistsRecyclerView.setAdapter(ChooseArtistAdapter);

        // Add item animation
        binding.artistsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Setup button
        binding.continueButton.setOnClickListener(v -> {
            if (!selectedChooseArtistIds.isEmpty()) {
                // Proceed to next screen
                Toast.makeText(this, "Selected " + selectedChooseArtistIds.size() + " ChooseArtists", Toast.LENGTH_SHORT).show();
                // Intent to next activity would go here
            } else {
                Toast.makeText(this, "Please select at least one ChooseArtist", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize data
        setupChooseArtistData();
        updateDisplayedChooseArtists();

    }


    private void setupChooseArtistData() {
        // Main ChooseArtists
        allChooseArtists.add(new ChooseArtist("1", "Arijit Singh", "https://c.saavncdn.com/artists/Arijit_Singh_004_20241118063717_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("2", "Guru Randhawa", "https://c.saavncdn.com/artists/Guru_Randhawa_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("3", "Karan Aujla", "https://c.saavncdn.com/artists/Karan_Aujla_002_20250228125836_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("4", "Yo Yo Honey Singh", "https://c.saavncdn.com/artists/Yo_Yo_Honey_Singh_002_20221216102650_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("4", "Badshah", "https://c.saavncdn.com/artists/Badshah_006_20241118064015_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("4", "Raftaar", "https://c.saavncdn.com/artists/Raftaar_009_20230223100912_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("5", "Shreya Ghoshal", "https://c.saavncdn.com/artists/Shreya_Ghoshal_007_20241101074144_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("6", "Tulsi Kumar", "https://c.saavncdn.com/artists/Tulsi_Kumar_005_20250124104447_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("7", "Kr$na", "https://c.saavncdn.com/artists/KR_NA_004_20240722131412_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("8", "Divine", "https://c.saavncdn.com/artists/DIVINE_005_20240307055212_500x500.jpg"));
        allChooseArtists.add(new ChooseArtist("9", "King", "https://c.saavncdn.com/artists/King_004_20240819092014_500x500.jpg"));

        // Related ChooseArtists
        relatedChooseArtists.add(new ChooseArtist("101", "Anuv Jain", "https://c.saavncdn.com/artists/Anuv_Jain_001_20231206073013_500x500.jpg", "1"));
        relatedChooseArtists.add(new ChooseArtist("102", "Parampara Tandon", "https://c.saavncdn.com/artists/Parampara_Thakur_20191130070743_500x500.jpg", "1"));
        relatedChooseArtists.add(new ChooseArtist("103", "Prateek Kuhad", "https://c.saavncdn.com/artists/Prateek_Kuhad_005_20230506101352_500x500.jpg", "1"));

        relatedChooseArtists.add(new ChooseArtist("201", "AP Dhillon", "https://c.saavncdn.com/artists/AP_Dhillon_002_20230918073841_500x500.jpg", "2"));
        relatedChooseArtists.add(new ChooseArtist("202", "Harrdy Sandhu", "https://c.saavncdn.com/artists/Hardy_Sandhu_001_20190913112018_500x500.jpg", "2"));
        relatedChooseArtists.add(new ChooseArtist("203", "Shubh", "https://c.saavncdn.com/artists/Shubh_000_20220921112507_500x500.jpg", "2"));

        relatedChooseArtists.add(new ChooseArtist("302", "Asees Kaur", "https://c.saavncdn.com/artists/Asees_Kaur_007_20250303070610_500x500.jpg", "3"));
        relatedChooseArtists.add(new ChooseArtist("301", "Dhwani Bhanushali", "https://c.saavncdn.com/artists/Dhvani_Bhanushali_001_20241015090250_500x500.jpg", "3"));
        relatedChooseArtists.add(new ChooseArtist("303", "Iqlipse Nova", "https://c.saavncdn.com/artists/Iqlipse_Nova_000_20230727101616_500x500.jpg", "3"));

        // Add more related ChooseArtists for other main ChooseArtists
    }

    private void updateDisplayedChooseArtists() {
        displayedChooseArtists.clear();

        if (showingRelatedFor == null) {
            // Show all main ChooseArtists
            displayedChooseArtists.addAll(allChooseArtists);
        } else {
            // Show all main ChooseArtists except the one we're showing related ChooseArtists for
            for (ChooseArtist ChooseArtist : allChooseArtists) {
                if (!ChooseArtist.getId().equals(showingRelatedFor)) {
                    displayedChooseArtists.add(ChooseArtist);
                }
            }

            // Add related ChooseArtists for the selected ChooseArtist
            for (ChooseArtist ChooseArtist : relatedChooseArtists) {
                if (ChooseArtist.getRelatedTo().equals(showingRelatedFor)) {
                    displayedChooseArtists.add(ChooseArtist);
                }
            }
        }

        // Update selection state
        for (ChooseArtist ChooseArtist : displayedChooseArtists) {
            ChooseArtist.setSelected(selectedChooseArtistIds.contains(ChooseArtist.getId()));
        }

        ChooseArtistAdapter.updateData(new ArrayList<>(displayedChooseArtists));
    }

    @Override
    public void onArtistClick(ChooseArtist artist, int position) {
        String ChooseArtistId = artist.getId();

        if (selectedChooseArtistIds.contains(ChooseArtistId)) {
            // Deselect ChooseArtist
            selectedChooseArtistIds.remove(ChooseArtistId);
            artist.setSelected(false);

            // If we're showing related ChooseArtists for this one, hide them
            if (ChooseArtistId.equals(showingRelatedFor)) {
                showingRelatedFor = null;
                updateDisplayedChooseArtists();
            } else {
                // Just update this item
                ChooseArtistAdapter.notifyItemChanged(position);
            }
        } else {
            // Select ChooseArtist
            selectedChooseArtistIds.add(ChooseArtistId);
            artist.setSelected(true);

            // If this is a main ChooseArtist, show related ChooseArtists
            boolean isMainChooseArtist = false;
            for (ChooseArtist mainChooseArtist : allChooseArtists) {
                if (mainChooseArtist.getId().equals(ChooseArtistId)) {
                    isMainChooseArtist = true;
                    break;
                }
            }

            if (isMainChooseArtist) {
                showingRelatedFor = ChooseArtistId;
                updateDisplayedChooseArtists();
            } else {
                // Just update this item
                ChooseArtistAdapter.notifyItemChanged(position);
            }
        }
    }
}