package com.pds.chambitas.ui.acercade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AcercaDeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is acerca de Fragment"
    }
    val text: LiveData<String> = _text
}