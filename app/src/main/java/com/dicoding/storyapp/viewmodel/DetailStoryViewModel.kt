package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.repo.StoryRepository

class DetailStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun detailStory(id: String) = storyRepository.detailStory(id)
}