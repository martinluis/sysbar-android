package com.lcmm.sysbar.android.models


data class ErrorResponse (
    var status: Int,
    var message: String,
    var errorCodes: List<String>? = null

)