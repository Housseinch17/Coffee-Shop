package com.example.coffeeshop.ui.screen.settingspage

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    init {
        Log.d("ViewModelInitialization","settings")
    }
}