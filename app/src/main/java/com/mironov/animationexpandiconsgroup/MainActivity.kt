package com.mironov.animationexpandiconsgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mironov.animationexpandiconsgroup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.container1.setOnClickListener { binding.container1.touch() }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}