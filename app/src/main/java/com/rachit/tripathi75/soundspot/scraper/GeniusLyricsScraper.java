package com.rachit.tripathi75.soundspot.scraper;

import android.os.Handler;
import android.os.Looper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeniusLyricsScraper {

    public interface LyricsCallback {
        void onSuccess(String lyrics);
        void onFailure(Exception e);
    }

    public void getLyrics(String url, LyricsCallback callback) {
        new Thread(() -> {
            try {
                // Connect to the URL and get the HTML document
                // In your GeniusLyricsScraper class
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .timeout(30000) // Increased from 10s to 30s
                        .ignoreHttpErrors(true)
                        .followRedirects(true)
                        .get();

                // Try the new Genius lyrics container first
                Elements lyricsContainers = doc.select("div[class^=Lyrics__Container]");

                StringBuilder lyricsBuilder = new StringBuilder();

                if (!lyricsContainers.isEmpty()) {
                    // New Genius structure (multiple containers)
                    for (Element container : lyricsContainers) {
                        lyricsBuilder.append(container.text()).append("\n\n");
                    }
                } else {
                    // Fallback to old structure
                    Element lyricsElement = doc.select("div.lyrics").first();
                    if (lyricsElement != null) {
                        lyricsBuilder.append(lyricsElement.text());
                    }
                }

                // Remove empty lines and trim
                String lyrics = lyricsBuilder.toString().trim();

                if (!lyrics.isEmpty()) {
                    // Success - post result to main thread
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onSuccess(lyrics));
                } else {
                    throw new Exception("No lyrics found on page");
                }

            } catch (Exception e) {
                // Failure - post error to main thread
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onFailure(e));
            }
        }).start();
    }
}