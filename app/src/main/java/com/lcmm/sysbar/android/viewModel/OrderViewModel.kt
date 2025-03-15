package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.models.OrderItem
import com.lcmm.sysbar.android.services.RetrofitClient
import com.lcmm.sysbar.android.utils.ErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {


    private val orderService = RetrofitClient.orderService

    private val _orderState = MutableStateFlow<Result<Order>?>(null)
    val orderState: StateFlow<Result<Order>?> get() = _orderState

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

    fun addItemsToOrder(orderId: Long, orderItems: List<OrderItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make the POST request
                val response = orderService.addItems(orderId, orderItems)

                // Emit the success result
                _orderState.value = Result.success(response)
            } catch (e: Exception) {
                // Emit the failure result
                _orderState.value = Result.failure(e)
            }
        }
    }

    /**
     *
     */
    fun createOrder(order: Order, orderItems: List<OrderItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make the POST request
                val response = orderService.create(order)

                // Emit the success result
                _orderState.value = Result.success(response)
            } catch (e: Exception) {
                // Emit the failure result
                _orderState.value = Result.failure(e)
            }
        }
    }


}