package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.Table
import com.lcmm.sysbar.android.services.RetrofitClient
import com.lcmm.sysbar.android.utils.ErrorHandler
import kotlinx.coroutines.launch

class TableViewModel : ViewModel() {


    private val tableService = RetrofitClient.tableService

    // LiveData to hold the list of tables
    val tablesLiveData = MutableLiveData<List<Table>>()

    // LiveData to hold the list of tables
    val tableLiveData = MutableLiveData<Table>()

    // LiveData to hold errors or loading state
    val errorLiveData = MutableLiveData<ErrorResponse>()

    /**
     *  Fetch users from the API
     */
    fun fetchTables() {
        viewModelScope.launch {
            try {
                val tables = tableService.getAll()
                tablesLiveData.postValue(tables)
            }
            catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }
        }
    }

    /**
     *
     */
    fun getTable(id: Long) {
        viewModelScope.launch {
            try {
                val table = tableService.get(id)
                tableLiveData.postValue(table)
            }
            catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }
        }
    }


}