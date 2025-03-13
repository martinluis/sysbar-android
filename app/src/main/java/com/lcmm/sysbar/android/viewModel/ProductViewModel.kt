package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.Product
import com.lcmm.sysbar.android.services.RetrofitClient
import com.lcmm.sysbar.android.utils.ErrorHandler
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val productService = RetrofitClient.productService

    // LiveData to hold the list of users
    val productsLiveData = MutableLiveData<List<Product>>()

    // LiveData to hold errors or loading state
    val errorLiveData = MutableLiveData<ErrorResponse>()

    /**
     *  Fetch users from the API
      */
    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val products = productService.getAll()
                productsLiveData.postValue(products)
            }
            catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }
        }
    }

}