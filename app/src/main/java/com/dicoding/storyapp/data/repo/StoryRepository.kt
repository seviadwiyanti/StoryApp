package com.dicoding.storyapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.paging.StoryRemoteMediator
import com.dicoding.storyapp.data.paging.entity.StoryDatabase
import com.dicoding.storyapp.data.paging.entity.StoryEntity
import com.dicoding.storyapp.data.pref.ResultValue
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
){
//    fun getStory() = liveData {
//        emit(ResultValue.Loading)
//        try {
//            val user = runBlocking { userPreference.getSession().first() }
//            val apiService = ApiConfig.getApiService(user.token)
//            val successGetStories = apiService.getStory()
//            emit(ResultValue.Success(successGetStories))
//        } catch (e: Exception) {
//            val errorMessage = when (e) {
//                is HttpException -> {
//                    val jsonInString = e.response()?.errorBody()?.string()
//                    val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
//                    errorBody?.message ?: e.message()
//                }
//                else -> e.message ?: "Unknown error occurred"
//            }
//            emit(ResultValue.Error(errorMessage))
//        }
//    }



    fun getStoriesPaging(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(userPreference, storyDatabase),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun detailStory(id: String) = liveData {
        emit(ResultValue.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successDetailStory = apiService.detailStory(id)
            emit(ResultValue.Success(successDetailStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let { ResultValue.Error(it) }?.let { emit(it) }
        }
    }

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(ResultValue.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successUploadStory = apiService.uploadStory(multipartBody, requestBody)
            emit(ResultValue.Success(successUploadStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let { ResultValue.Error(it) }?.let { emit(it) }
        }
    }


//    companion object {
//        @Volatile
//        private var instance: StoryRepository? = null
//        fun getInstance(
//            apiService: UserPreference,
//            userPreference: StoryDatabase,
//            storyDatabase: StoryDatabase
//        ): StoryRepository =
//            instance ?: synchronized(this) {
//                instance ?: StoryRepository(userPreference, storyDatabase)
//            }.also { instance = it }
//    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, storyDatabase)
            }.also { instance = it }
    }

}