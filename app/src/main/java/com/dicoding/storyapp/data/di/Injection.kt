package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.paging.entity.StoryDatabase
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore
import com.dicoding.storyapp.data.repo.MapsRepository
import com.dicoding.storyapp.data.repo.StoryRepository
import com.dicoding.storyapp.data.repo.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(pref, storyDatabase)
    }

    fun provideMapsRepository(context: Context): MapsRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return MapsRepository.getInstance(pref)
    }
}

//object Injection {
//    fun provideRepository(context: Context): UserRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
//        return UserRepository.getInstance(apiService, pref)
//    }
//
//    fun provideStoryRepository(context: Context): StoryRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
//        val database = StoryDatabase.getDatabase(context) // Ensure you have a method to get the database instance
//        return StoryRepository.getInstance(apiService, pref, database)
//    }
//
//    fun provideMapsRepository(context: Context): MapsRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        return MapsRepository.getInstance(pref)
//    }
//}

//object Injection {
//    fun provideRepository(context: Context): UserRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
//        return UserRepository.getInstance(apiService, pref)
//    }
//
//    fun provideStoryRepository(context: Context): StoryRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
//        val database = StoryDatabase.getDatabase(context) // Ensure you have a method to get the database instance
//        return StoryRepository.getInstance(apiService, pref, database)
//    }
//
//    fun provideMapsRepository(context: Context): MapsRepository {
//        val pref = UserPreference.getInstance(context.dataStore)
//        return MapsRepository.getInstance(pref)
//    }
//}

