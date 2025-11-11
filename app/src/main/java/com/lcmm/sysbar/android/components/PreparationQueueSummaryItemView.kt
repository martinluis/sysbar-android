package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.PreparationQueueSummary
import java.time.format.DateTimeFormatter


class PreparationQueueSummaryItemView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CustomView(context, attrs, defStyleAttr) {

    private val titleText: TextView
    private val subTitleText: TextView

    private lateinit var preparationQueueSummary: PreparationQueueSummary


    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.preparation_queue_summary_item_view, this, true)
        titleText = findViewById(R.id.titleText)
        subTitleText = findViewById(R.id.subTitleText)
        initView()
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {

    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    fun bindData(preparationQueueSummary: PreparationQueueSummary){
        this.preparationQueueSummary = preparationQueueSummary
        titleText.text = preparationQueueSummary.destination
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        subTitleText.text = preparationQueueSummary.createdAt.format(formatter)
    }

    /**
     *
     */
    fun markAsSelected() {
        titleText.setTextColor(ContextCompat.getColor(context, R.color.success))
    }


    /**
     *
     */
    fun markAsDeselected() {
        titleText.setTextColor(ContextCompat.getColor(context, R.color.black))
    }

}