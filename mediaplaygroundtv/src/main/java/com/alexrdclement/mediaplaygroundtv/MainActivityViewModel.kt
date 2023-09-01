package com.alexrdclement.mediaplaygroundtv

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(

) : ViewModel() {

    fun onLoginClick(activity: ComponentActivity) {

    }
}
