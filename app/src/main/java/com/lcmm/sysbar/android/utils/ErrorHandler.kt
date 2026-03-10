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
        if (ex is HttpException) {
            val errorResponse: ErrorResponse = try {
                val errorBody = ex.response()?.errorBody()?.string()
                val jsonResponse = if (!errorBody.isNullOrEmpty()) JSONObject(errorBody) else null

                val parsedError = if (jsonResponse != null) {
                    Gson().fromJson(jsonResponse.toString(), ErrorResponse::class.java)
                } else {
                    ErrorResponse(ex.code(), "HTTP ERROR: ${ex.code()}")
                }

                // Ensure message is not empty or null
                if (parsedError.message.isNullOrEmpty()) {
                    parsedError.message = "HTTP ERROR: ${ex.code()}"
                }

                parsedError
            } catch (jsonEx: JSONException) {
                ErrorResponse(ex.code(), ex.message ?: "HTTP ERROR: ${ex.code()}")
            }

            errorLiveData.postValue(errorResponse)
        } else {
            val message = ex.message ?: "An unexpected error occurred"
            val errorResponse = ErrorResponse(500, message)
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