package com.example.quetzalli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.quetzalli.data.models.SequenceGraph
import com.example.quetzalli.data.models.TestMemory
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoryVM @Inject constructor(private val memRepo: MemoryRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val sequences: LiveData<List<SequenceGraph>> = liveData {
        val data = memRepo.getSequences()
        if (data is FetchResult.Success) {
            emit(data.data)
        } else if (data is FetchResult.Error) {
            _error.postValue(data.exception.message ?: "An unknown error occurred")
            emit(emptyList<SequenceGraph>())
        }
    }

    fun addTestMemoryData(userId: String, scoreTotal: Int, totalTime: String): LiveData<Boolean> {
        val isUploadSuccessful = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val testMemory = TestMemory(userId, scoreTotal, totalTime)
            val result = memRepo.insertTestMemoryData(testMemory)
            isUploadSuccessful.value = result is FetchResult.Success
            if (result is FetchResult.Error) {
                _error.postValue(result.exception.message ?: "An unknown error occurred")
            }
        }
        return isUploadSuccessful
    }

}
