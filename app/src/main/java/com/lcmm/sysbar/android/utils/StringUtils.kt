package com.lcmm.sysbar.android.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

class StringUtils { companion object {

    /**
     *
     */
    fun decimalToCurrencyFormat(value: BigDecimal): String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(2)
        format.setMinimumFractionDigits(2)
        format.currency = Currency.getInstance("USD")
        return format.format(value)
    }

}}