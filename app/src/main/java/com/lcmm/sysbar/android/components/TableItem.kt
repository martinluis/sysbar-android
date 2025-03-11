package com.lcmm.sysbar.android.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.Table

class TableItem@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val titleText: TextView

    init {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.table_item, this, true)
        titleText  = findViewById(R.id.tableNameText)
    }

    /**
     *
     */
    private fun setupTable(tableName: String?, isActive: Boolean){
        titleText.text = tableName
        background = ContextCompat.getDrawable(context, R.drawable.container_round)
        backgroundTintList = if (isActive) {
            ContextCompat.getColorStateList(context, R.color.success)
        } else {
            ContextCompat.getColorStateList(context, R.color.disable)
        }
        requestLayout()
    }

    /**
     *
     */
    fun setTable(table: Table) {
        setupTable(table.name, table.isBusy == true)
    }

}