package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.PreparationQueue


class PreparationQueueItemView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CustomView(context, attrs, defStyleAttr) {

    private val quantityText: TextView
    private val productText: TextView
    private val commentText: TextView
    private val finishButton: MaterialButton

    private lateinit var preparationQueue: PreparationQueue


    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.preparation_queue_item_view, this, true)
        quantityText = findViewById(R.id.quantityText)
        productText = findViewById(R.id.productText)
        commentText = findViewById(R.id.commentText)
        finishButton = findViewById(R.id.finishButton)
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
    fun bindData(preparationQueue: PreparationQueue){
        this.preparationQueue = preparationQueue
        quantityText.text = preparationQueue.quantity.toString()
        productText.text = preparationQueue.productName
        commentText.visibility = if (preparationQueue.comment.isNullOrEmpty()) View.GONE else View.VISIBLE
        commentText.text = preparationQueue.comment
    }

    /**
     *
     */
    fun setFinishClickListener(listener: OnClickListener) {
        finishButton.setOnClickListener(listener)
    }
}