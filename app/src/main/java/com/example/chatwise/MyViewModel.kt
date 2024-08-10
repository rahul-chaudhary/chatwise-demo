package com.example.chatwise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwise.model.Root
import com.example.chatwise.repository.getResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyViewModel : ViewModel() {

    // Initialize MutableLiveData
    private val _response = MutableLiveData<Response<Root>?>()

    // Public LiveData to expose to observers
    val response: LiveData<Response<Root>?> get() = _response

    init {
        fetchResponse()
    }

    private fun fetchResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getResponse()
            // Update the LiveData in a thread-safe manner
            _response.postValue(result)
        }
    }
}