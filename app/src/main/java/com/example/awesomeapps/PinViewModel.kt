package com.example.awesomeapps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PinViewModel: ViewModel() {
    val pinCode = MutableLiveData<String>().also {
        it.value = ""
    }


}