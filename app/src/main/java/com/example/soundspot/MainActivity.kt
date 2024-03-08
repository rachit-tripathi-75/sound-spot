package com.example.soundspot

import android.content.Intent
import android.media.Image
import android.media.MediaController2
import android.media.browse.MediaBrowser.MediaItem
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
import android.widget.VideoView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.Event
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var canvas : VideoView
    private lateinit var collapse: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        player = SimpleExoPlayer.Builder(this).build()
        val mp3Url = "https://firebasestorage.googleapis.com/v0/b/sound-spot-dd1f8.appspot.com/o/HOUSTONFORNICATION.mp3?alt=media&token=acad90c2-a9c6-4b24-9fa8-8572d2fdc2bc"
        val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(mp3Url)
        player.setMediaItem(mediaItem)
        loadAlbumArt()
        setPlayPauseFunc()
        setSeekBar()
        loadCanvas()

        collapse = findViewById(R.id.ivCollapse)
        collapse.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }
    }

    private fun loadCanvas() {
        canvas = findViewById(R.id.vvCanvas)
        val videoUrl = "https://firebasestorage.googleapis.com/v0/b/sound-spot-dd1f8.appspot.com/o/canvas.mp4?alt=media&token=7a329b98-0832-45ef-8b24-8629b9a5bd1e"
        val uri = Uri.parse(videoUrl)
        canvas.setVideoURI(uri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(canvas)
        mediaController.setMediaPlayer(canvas)
        canvas.setMediaController(mediaController)

        canvas.setOnCompletionListener {
            canvas.start()
        }
        canvas.start()

    }

    private fun setSeekBar() {
        var seekBar: SeekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2 == true) {
                    player.seekTo((p1 * player.duration / 100).toLong())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // chhor de
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // chhor de
            }
        })

        player.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                seekBar.progress = ((player.currentPosition * 100) / player.duration).toInt()
            }
        })
    }

    private fun loadAlbumArt() {
        val retrofitBuilder = Retrofit.Builder().baseUrl("https://api.jsonbin.io/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getAlbumData()
        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                var responseBody = response.body()
                val songDetails = responseBody?.record!!
                var albumArt: ImageView = findViewById(R.id.ivAlbumArt)
                var songName : TextView = findViewById(R.id.tvSongName)
                var artistName : TextView = findViewById(R.id.tvArtistName)

                Picasso.get().load(songDetails.albumart).into(albumArt)
                songName.text = songDetails.title
                artistName.text = songDetails.artist


            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Actitivity", "onFailure" + t.message)
            }
        })
    }

    private fun setPlayPauseFunc() {
        var flag : Boolean = true
        player.prepare()
        var btnPlayPause : ImageView = findViewById(R.id.ivPlayPause)
        btnPlayPause.setOnClickListener {
            if(flag == true) {
                btnPlayPause.setImageResource(R.drawable.baseline_song_play_24)
                flag = false
                player.pause()
            }
            else {
                btnPlayPause.setImageResource(R.drawable.baseline_song_pause_24)
                flag = true
                player.play()
            }
        }
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

}
