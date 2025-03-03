package com.lcmm.sysbar.android.utils

import com.lcmm.sysbar.android.enums.Role

class SecurityUtils { companion object {

    /**
     *
     */
    public fun hasPermissions(roles: Set<Role>, role: Role ): Boolean {
        return roles.contains(role) || roles.contains(Role.ADMIN)
    }
}}