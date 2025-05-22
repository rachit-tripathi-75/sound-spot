package com.rachit.tripathi75.soundspot.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.ActivityLyricsTextBinding;
import com.rachit.tripathi75.soundspot.scraper.GeniusLyricsScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LyricsTextActivity extends AppCompatActivity {

    private ActivityLyricsTextBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLyricsTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String apiPath = "/songs/5762594";

//        String songUrl = "https://genius.com/Kendrick-lamar-humble-lyrics";
      String songLyricsUrl = "https://genius.com" + apiPath + "-lyrics";     //==> "api_path": "/songs/3039923",

        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(songLyricsUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .get();

                Elements containers = doc.select("div[class^=Lyrics__Container]");

                StringBuilder lyricsBuilder = new StringBuilder();
                boolean startCollecting = false;

                for (Element container : containers) {
                    for (Node node : container.childNodes()) {
                        String text;
                        if (node instanceof TextNode) {
                            text = ((TextNode) node).text();
                        } else if (node.nodeName().equals("br")) {
                            lyricsBuilder.append("\n");
                            continue;
                        } else if (node instanceof Element) {
                            text = ((Element) node).text();
                        } else {
                            continue;
                        }

                        // Trigger collection only when we find something like [Intro], [Verse 1], etc.
                        if (!startCollecting && text.matches("\\[.*\\]")) {
                            startCollecting = true;
                        }

                        if (startCollecting) {
                            lyricsBuilder.append(text).append("\n");
                        }
                    }

                    if (startCollecting) {
                        lyricsBuilder.append("\n\n");
                    }
                }

                String lyrics = lyricsBuilder.toString().trim();

                runOnUiThread(() -> {
                    binding.lyricsTextView.setText(lyrics);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();




    }
}