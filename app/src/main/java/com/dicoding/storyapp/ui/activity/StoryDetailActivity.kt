package com.dicoding.storyapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.pref.ResultValue
import com.dicoding.storyapp.data.response.Story
import com.dicoding.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.storyapp.viewmodel.DetailStoryViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivActionBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        IdStory = intent.getStringExtra(STORY_ID).toString()
        viewModel.detailStory(IdStory).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultValue.Loading -> {
                        showLoading(true)
                    }

                    is ResultValue.Success -> {
                        result.data.story?.let { showStoryDetail(it) }
                        showLoading(false)
                    }

                    is ResultValue.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            } else {
                showToast(getString(R.string.empty_detail_story))
            }
        }
    }

    private fun showStoryDetail(story: Story) {
        with(binding) {
            story.photoUrl?.let { ivDetailPhoto.loadImage(it) }
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            tvDetailDate.text = story.createdAt?.let { convertToIndonesiaTime(it) }
        }
    }

//    private fun ImageView.loadImage(url: String) {
//        Glide.with(this.context)
//            .load(url)
//            .fitCenter()
//            .skipMemoryCache(true)
//            .into(this)
//    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(context)
            .load(url)
            .apply {
                fitCenter()
                skipMemoryCache(true)
            }
            .into(this)
    }


    private fun convertToIndonesiaTime(iso8601DateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputDate = SimpleDateFormat("d MMMM yyyy, HH:mm:ss", Locale.getDefault())

        val timeZoneUTC = TimeZone.getTimeZone("UTC")
        val timeZoneIndonesia = TimeZone.getTimeZone("Asia/Jakarta")

        inputFormat.timeZone = timeZoneUTC

        return try {
            val dateCreated = inputFormat.parse(iso8601DateTime)
            dateCreated?.let {
                val calendar = Calendar.getInstance()
                calendar.time = dateCreated
                calendar.timeZone = timeZoneIndonesia
                return outputDate.format(calendar.time)
            } ?: "Invalid Date"
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "There is an error: ${e.message}", Toast.LENGTH_SHORT).show()
            "Invalid Date"
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val STORY_ID = "STORY_ID"
        var IdStory = String()
    }
}