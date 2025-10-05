package com.filipdadgar.fd_citycitat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Toast
import com.filipdadgar.fd_citycitat.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var quotes: List<String>
    private var currentIndex: Int = 0
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
        currentIndex = prefs.getInt(getString(R.string.prefs_key_index), 0)

        quotes = loadQuotesFromAssets()

        if (quotes.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_quotes), Toast.LENGTH_LONG).show()
            binding.btnNext.isEnabled = false
            binding.btnPrevious.isEnabled = false
            return
        }

        if (currentIndex < 0 || currentIndex >= quotes.size) currentIndex = 0

        updateUI()

        binding.btnNext.setOnClickListener {
            if (currentIndex < quotes.size - 1) {
                currentIndex++
                updateUI()
            }
        }

        binding.btnPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                updateUI()
            }
        }

        binding.btnQuit.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        // Save current index
        prefs.edit().putInt(getString(R.string.prefs_key_index), currentIndex).apply()
    }

    private fun loadQuotesFromAssets(): List<String> {
        val list = mutableListOf<String>()
        try {
            assets.open("cites.txt").use { input ->
                BufferedReader(InputStreamReader(input)).useLines { lines ->
                    val buffer = StringBuilder()
                    lines.forEach { line ->
                        if (line.trim().isEmpty()) return@forEach
                        // The file contains numbered lines like "1. \"Quote\" - Author"
                        // We'll strip the leading number and dot if present
                        val cleaned = line.replaceFirst("^\\s*\\d+\\.\\s*".toRegex(), "")
                        list.add(cleaned)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun updateUI() {
        binding.tvQuote.text = quotes[currentIndex]
        binding.btnPrevious.isEnabled = currentIndex > 0
        binding.btnNext.isEnabled = currentIndex < quotes.size - 1
    }
}