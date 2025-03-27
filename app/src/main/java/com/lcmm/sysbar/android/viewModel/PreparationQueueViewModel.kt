package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.PreparationQueue
import com.lcmm.sysbar.android.services.RetrofitClient
import com.lcmm.sysbar.android.utils.ErrorHandler
import kotlinx.coroutines.launch

class PreparationQueueViewModel : ViewModel() {


    private val preparationQueueService = RetrofitClient.preparationQueueService

    // LiveData to hold the list of tables
    val preparationQueueListLiveData =MutableLiveData<MutableList<PreparationQueue>>()

    // LiveData to hold errors or loading state
    val errorLiveData = MutableLiveData<ErrorResponse>()

    /**
     *  Fetch users from the API
     */
    fun fetchActives() {
        viewModelScope.launch {
            try {
                val preparationQueueList = preparationQueueService.getActives()
                preparationQueueListLiveData.postValue(preparationQueueList.toMutableList())
            }
            catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }
        }
    }


}