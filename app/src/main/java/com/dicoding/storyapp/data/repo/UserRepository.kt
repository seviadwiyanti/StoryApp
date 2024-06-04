package com.dicoding.storyapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.pref.ResultValue
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.response.ErrorResponse
import com.dicoding.storyapp.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    fun register(name: String, email: String, password: String): LiveData<ResultValue<Any>> {
        return liveData {
            emit(ResultValue.Loading)
            try {
                val successResponse = apiService.register(name, email, password).message
                emit(ResultValue.Success(successResponse))
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is HttpException -> {
                        val jsonInString = e.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                        errorBody?.message ?: e.message ?: "Unknown error"
                    }
                    else -> e.message ?: "Unknown error"
                }
                emit(ResultValue.Error(errorMessage))
            }
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultValue.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultValue.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            errorBody?.message?.let { ResultValue.Error(it) }?.let { emit(it) }
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
        instance = null
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
