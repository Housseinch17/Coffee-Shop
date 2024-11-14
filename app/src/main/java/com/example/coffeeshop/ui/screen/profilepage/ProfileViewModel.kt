package com.example.coffeeshop.ui.screen.profilepage

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {

    init {
        Log.d("ViewModelInitialization","Profile created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModelInitialization","profile destroyed")
    }
}