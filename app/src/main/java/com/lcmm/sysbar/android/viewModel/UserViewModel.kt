package com.lcmm.sysbar.android.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.User
import com.lcmm.sysbar.android.services.RetrofitClient
import com.lcmm.sysbar.android.utils.ErrorHandler
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userService = RetrofitClient.userService

    // LiveData to hold the list of users
    val usersLiveData = MutableLiveData<List<User>>()

    // LiveData to hold a single user
    val userLiveData = MutableLiveData<User>()

    // LiveData to hold errors or loading state
    val errorLiveData = MutableLiveData<ErrorResponse>()

    /**
     * Fetch user by ID from the repository
     */
    fun requestUserAccess(code: String) {
        viewModelScope.launch {
            try {
                val user = userService.requestAccess(code)
                userLiveData.postValue(user)
            } catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }

        }
    }

    /**
     *  Fetch users from the API
      */
    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val users = userService.getAll()
                usersLiveData.postValue(users)
            }
            catch (e: Exception) {
                ErrorHandler.handleError(errorLiveData, e)
            }
        }
    }

}