package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.Role


data class User (
    var id: Long? = null,
    var name: String,
    var code: String? = null,
    var roles: Set<Role>,
    var active: Boolean? = null
)