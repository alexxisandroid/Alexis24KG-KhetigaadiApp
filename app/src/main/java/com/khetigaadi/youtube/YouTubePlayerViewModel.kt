package com.khetigaadi.youtube

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YouTubePlayerViewModel @Inject constructor() : ViewModel() {
    var currentSeconds: Float = 0f
}
