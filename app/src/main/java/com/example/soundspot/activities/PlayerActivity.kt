package com.example.soundspot.activities

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.soundspot.R
import com.example.soundspot.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var options: ActivityOptionsCompat
    private var packageName: String = "com.example.soundspot"
//    private lateinit var currentTimeTextView: TextView
//    private lateinit var totalDurationTextView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying: Boolean = false
    private var isCanvasPlaying: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listeners()
        loadAlbumArt()
        mediaControls()
        setCanvas()
        binding.videoView.visibility = View.INVISIBLE

        binding.btnTurnOnCanvas.setOnClickListener {
            if(!isCanvasPlaying) {
                binding.ivAlbumArt.visibility = View.INVISIBLE
                binding.tvSongName.visibility = View.VISIBLE
                binding.tvSongArtist.visibility = View.VISIBLE
                binding.seekBar.visibility = View.VISIBLE
                binding.llMediaControls.visibility = View.VISIBLE
                binding.videoView.visibility = View.VISIBLE
                binding.videoView.alpha = 0.5f
                binding.videoView.start()
                options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
                isCanvasPlaying = true
            }
            else {
                binding.ivAlbumArt.visibility = View.VISIBLE
                binding.tvSongName.visibility = View.VISIBLE
                binding.tvSongArtist.visibility = View.VISIBLE
                binding.seekBar.visibility = View.VISIBLE
                binding.llMediaControls.visibility = View.VISIBLE
                binding.videoView.visibility = View.INVISIBLE
                binding.videoView.stopPlayback()
                options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
                isCanvasPlaying = false
            }

        }


    }

    private fun setCanvas() {
        val videoUri = Uri.parse("android.resource://${packageName}/raw/aankhcanvas")
        binding.videoView.setVideoURI(videoUri)
    }

    private fun listeners() {
        binding.ivDragDown.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadAlbumArt() {
        Glide.with(this).load(R.drawable.aankhalbumart).transform(CenterCrop(), RoundedCorners(10))
            .into(binding.ivAlbumArt)
    }

    private fun mediaControls() {

        mediaPlayer = MediaPlayer.create(this, R.raw.aankhaudio)
        binding.seekBar.max = mediaPlayer.duration
//        totalDurationTextView.text = formatTime(mediaPlayer.duration)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
//                    currentTimeTextView.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.ivPlayPause.setOnClickListener {
            if(isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
            }
            else {
                mediaPlayer.start()
                isPlaying = true
            }
        }
        updateSeekBar()
    }

    private fun updateSeekBar() {
        binding.seekBar.progress = mediaPlayer.currentPosition
//        currentTimeTextView.text = formatTime(mediaPlayer.currentPosition)
        if (mediaPlayer.isPlaying) {
            handler.postDelayed({ updateSeekBar() }, 1000)
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
    }
}