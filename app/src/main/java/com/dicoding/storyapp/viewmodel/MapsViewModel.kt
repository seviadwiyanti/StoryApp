package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.repo.MapsRepository

class MapsViewModel ( private val mapsRepository: MapsRepository
) : ViewModel() {

    fun getStoriesWithLocation() = mapsRepository.getStoriesWithLocation()
}