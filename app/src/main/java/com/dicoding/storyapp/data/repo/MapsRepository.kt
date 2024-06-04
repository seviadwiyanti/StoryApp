package com.dicoding.storyapp.data.repo

import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.pref.ResultValue
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class MapsRepository private constructor(
    private val userPreference: UserPreference
) {
    fun getStoriesWithLocation() = liveData {
        emit(ResultValue.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successGetStoriesWithLocation = apiService.getStoriesWithLocation()
            emit(ResultValue.Success(successGetStoriesWithLocation))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let { ResultValue.Error(it) }?.let { emit(it) }
        }
    }

    companion object {
        @Volatile
        private var instance: MapsRepository? = null

        fun getInstance(
            userPreference: UserPreference
        ): MapsRepository =
            instance ?: synchronized(this) {
                instance ?: MapsRepository(userPreference)
            }.also { instance = it }
    }
}