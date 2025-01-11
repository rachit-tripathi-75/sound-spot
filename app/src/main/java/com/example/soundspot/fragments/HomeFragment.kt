package com.example.soundspot.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundspot.R
import com.example.soundspot.activities.PlayerActivity
import com.example.soundspot.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.btnOpenPlayer.setOnClickListener {
            startActivity(Intent(context, PlayerActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
        }

        return binding.root
    }

}