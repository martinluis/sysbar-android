package com.lcmm.sysbar.android.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.ErrorResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class ErrorHandler { companion object {

    /**
     *
     */
    fun handleError(errorLiveData: MutableLiveData<ErrorResponse>, ex: Exception) {
        if ( ex is HttpException) {
            var errorResponse: ErrorResponse?
            try {
                val jsonResponse= JSONObject(ex.response()?.errorBody()!!.string())
                errorResponse = Gson().fromJson(jsonResponse.toString(), ErrorResponse::class.java)
                if (errorResponse.message.isEmpty()) errorResponse.message = "HTTP ERROR: " + ex.code()

            }
            catch (jsonEx: JSONException){
                errorResponse = ErrorResponse(ex.code(), ex.message() )
            }
            errorLiveData.postValue(errorResponse)
        }
        else {
            val errorResponse = ErrorResponse(500, ex.message!!)
            errorLiveData.postValue(errorResponse)
        }
    }

    /**
     *
     */
    fun getErrorMessage(context: Context, errorCode: String): String {
        return when (errorCode) {
            "user.code.invalid" -> context.getString(R.string.error_user_code_invalid)
            else -> context.getString(R.string.error_generic)  // A generic error message
        }
    }

} }