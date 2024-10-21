package com.csaim.tracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csaim.tracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val daysCompleted = mutableListOf<String>()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set onClickListeners for each day, passing the correct day name
        binding.btnMonday.setOnClickListener {
            trackDay("Monday")
            binding.btnMonday.isEnabled = false
        }
        binding.btnTuesday.setOnClickListener {
            trackDay("Tuesday")
            binding.btnTuesday.isEnabled = false
        }
        binding.btnWednesday.setOnClickListener {
            trackDay("Wednesday")
            binding.btnWednesday.isEnabled = false
        }
        binding.btnThursday.setOnClickListener {
            trackDay("Thursday")
            binding.btnThursday.isEnabled = false
        }
        binding.btnFriday.setOnClickListener {
            trackDay("Friday")
            binding.btnFriday.isEnabled = false
        }
        binding.btnSaturday.setOnClickListener {
            trackDay("Saturday")
            binding.btnSaturday.isEnabled = false
        }
        binding.btnSunday.setOnClickListener {
            trackDay("Sunday")
            binding.btnSunday.isEnabled = false
        }

        // Show progress on button click
        binding.btnShowProgress.setOnClickListener {
            showProgress()
        }
    }

    private fun trackDay(day: String) {
        if (!daysCompleted.contains(day)) {
            daysCompleted.add(day)
        }
    }

    private fun showProgress() {
        val progress = daysCompleted.size * 100 / 7 // Assuming 7 days in a week
        Toast.makeText(this, "Your progress: $progress%", Toast.LENGTH_LONG).show()
    }
}
