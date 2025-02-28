package com.lcmm.sysbar.android.models


data class User (
    var id: Long? = null,
    var name: String,
    var code: String? = null,
    var role: String? = null,
    var active: Boolean? = null
)