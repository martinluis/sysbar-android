package com.lcmm.sysbar.android.adapters

import android.content.Context
import android.widget.RelativeLayout
import com.google.android.flexbox.FlexboxLayout
import com.lcmm.sysbar.android.components.TableItem
import com.lcmm.sysbar.android.models.Table

class TableListAdapter (
    private val context: Context,
    private val flexboxLayout: FlexboxLayout,
    private val onItemClickListener: (Table) -> Unit // Callback to handle click
) {

    fun setItems(tables: List<Table>) {
        // Clear existing views
        flexboxLayout.removeAllViews()

        // Dynamically create views for each item and add to FlexboxLayout
        tables.forEach { table ->
            val tableItem = TableItem(context).apply {
                setOnClickListener {
                    onItemClickListener(table)  // Trigger the click listener with the clicked item
                }
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                val scale = resources.displayMetrics.density
                val dpAsPixels = (10.0f * scale + 0.5f).toInt()
                params.setMargins(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
                layoutParams = params
                setTable(table)
            }
            flexboxLayout.addView(tableItem)
        }
    }
}