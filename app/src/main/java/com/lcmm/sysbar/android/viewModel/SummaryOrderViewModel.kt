package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SummaryOrderViewModel : ViewModel() {

    private val _orderSummary = MutableLiveData<String>()
    val orderSummary: LiveData<String> get() = _orderSummary

    /**
     *
     */
    fun updateOrderSummary(message: String) {
        _orderSummary.value = message
    }

}