package com.kishordahiwadkar.bloodline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.databinding.ObservableField

const val SPLASH_SCREEN_PAGE = 0
const val REGISTRATION_PAGE = 1
const val REQUEST_PAGE = 2
const val T_C_PAGE = 3

class MainViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * 0 = Splash Screen Page
     * 1 = Registration Page
     * 2 = Request Page
     * 3 = Terms and Conditions Page
     */
    var layoutSwitcher = ObservableField<Int>()

    init {
        layoutSwitcher.set(SPLASH_SCREEN_PAGE)
    }

    fun heartIconClick() {
        layoutSwitcher.set(REGISTRATION_PAGE)
    }
}