package com.dicoding.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.repo.UserRepository
import com.dicoding.storyapp.data.pref.ResultValue

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<ResultValue<Any>> {
        return repository.register(name, email, password)
    }
}
