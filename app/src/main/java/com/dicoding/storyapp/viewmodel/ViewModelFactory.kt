package com.dicoding.storyapp.viewmodel

import MainViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.repo.UserRepository
import com.dicoding.storyapp.data.di.Injection
import com.dicoding.storyapp.data.repo.MapsRepository
import com.dicoding.storyapp.data.repo.StoryRepository

class ViewModelFactory(private val repository: UserRepository, private val storyRepository: StoryRepository, private val mapsRepository: MapsRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, storyRepository) as T
            }

            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(TambahStoryViewModel::class.java) -> {
                TambahStoryViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(mapsRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideStoryRepository(context),
                    Injection.provideMapsRepository(context)
                )
            }.also { instance = it }
    }
}