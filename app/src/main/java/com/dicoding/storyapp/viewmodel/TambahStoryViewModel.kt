package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.repo.StoryRepository
import java.io.File


class TambahStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadStory(file: File, description: String) =
        storyRepository.uploadImage(file, description)
}

