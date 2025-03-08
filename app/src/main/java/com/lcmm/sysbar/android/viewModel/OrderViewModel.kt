package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.services.RetrofitClient
import com.lcmm.sysbar.android.utils.ErrorHandler
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {


    private val orderService = RetrofitClient.orderService

    // LiveData to hold the list of tables
    val orderLiveData = MutableLiveData<Order>()

    // LiveData to hold errors or loading state
    val errorLiveData = MutableLiveData<ErrorResponse>()

    /**
     *  Fetch users from the API
     */
    fun getByTable(tableId: Long) {
        viewModelScope.launch {
            try {
                val order = orderService.findByTable(tableId)
                orderLiveData.postValue(order)
            }
            catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }
        }
    }


}