package com.lcmm.sysbar.android.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.components.PreparationQueueItemView
import com.lcmm.sysbar.android.components.PreparationQueueSummaryItemView
import com.lcmm.sysbar.android.databinding.FragmentProcessOrderBinding
import com.lcmm.sysbar.android.enums.OrderType
import com.lcmm.sysbar.android.fragments.PreparationQueueSummaryAdapter.PreparationQueueSummaryViewHolder
import com.lcmm.sysbar.android.models.PreparationQueue
import com.lcmm.sysbar.android.models.PreparationQueueSummary
import com.lcmm.sysbar.android.services.LocalStorageService
import com.lcmm.sysbar.android.viewModel.PreparationQueueViewModel

class OrderProcessFragment : Fragment() {
    private var _binding: FragmentProcessOrderBinding? = null
    private val binding get() = _binding!!

    private val preparationQueueViewModel: PreparationQueueViewModel by viewModels()
    private lateinit var localStorageService: LocalStorageService
    private lateinit var navController: NavController
    private lateinit var preparationQueueAdapter: PreparationQueueAdapter
    private lateinit var preparationQueueSummaryAdapter: PreparationQueueSummaryAdapter

    private var selectedIndex = 0

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProcessOrderBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        initView()
        initObservers()
        return this.binding.root
    }

    /**
     *
     */
    private fun initView() {
        navController = findNavController()
        localStorageService = LocalStorageService(requireContext())
    }


    /**
     * Group PreparationQueue items to generate a PreparationQueueSummary base on the order ID
     */
    private fun initView(preparationQueueList: MutableList<PreparationQueue>) {
        val preparationQueueSummaryList: MutableList<PreparationQueueSummary> = mutableListOf()
        val sortedList = preparationQueueList.sortedBy { it.createdAt }
        val groupedByOrderMap = sortedList .groupBy { it.orderId }

        for ((orderId, items) in groupedByOrderMap) {
            val item = items[0]
            val destination = when (item.orderType) {
                OrderType.LOCAL -> {
                    item.tableName
                }
                OrderType.DELIVERY -> {
                    requireContext().resources.getString(R.string.order_type_delivery)
                }
                OrderType.PERSONAL -> {
                    requireContext().resources.getString(R.string.order_type_delivery)
                }
               null -> {""}
            }
            val preparationQueueSummary = PreparationQueueSummary(orderId!!, destination, item.userName!!, item.orderType!!, item.createdAt, items)
            preparationQueueSummaryList.add(preparationQueueSummary)
        }

        // Adapter for the list of PreparationQueue by Order
        preparationQueueSummaryAdapter = PreparationQueueSummaryAdapter(preparationQueueSummaryList, selectedIndex) { itemView, index ->
            selectedIndex = index
            val preparationQueueSummary = preparationQueueSummaryList[index]
            initPreparationQueueDetailsView(preparationQueueSummary)
            //itemView.markAsSelected()
        }

        binding.preparationQueueSummaryList.layoutManager = LinearLayoutManager(context)
        binding.preparationQueueSummaryList.adapter = preparationQueueSummaryAdapter

        initPreparationQueueDetailsView( preparationQueueSummaryList[selectedIndex] )
    }

    /**
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers() {
        // List of active preparation order items
        preparationQueueViewModel.preparationQueueListLiveData.observe(requireActivity()) { preparationQueueList ->
            initView(preparationQueueList)
        }

        // Observer when click the Finish button for preparation order item
        preparationQueueViewModel.preparationQueueFinishStatus.observe(requireActivity()) { success ->
            if (success) {
                preparationQueueViewModel.fetchActives()
                preparationQueueAdapter.notifyDataSetChanged()
            }
        }

        preparationQueueViewModel.fetchActives()
    }

    /**
     * Init the container with the preparation items from Order (Table, User, List of Order Items)
     */
    private fun initPreparationQueueDetailsView(preparationQueueSummary: PreparationQueueSummary){
        binding.destinationText.text = preparationQueueSummary.destination
        binding.waiterText.text = preparationQueueSummary.userName
        if (preparationQueueSummary.orderType == OrderType.DELIVERY) {
            val preparationQueueDetails = preparationQueueSummary.preparationQueueList[0]
            if (preparationQueueDetails.customer != null) {
                binding.customerInfoContainer.visibility =  View.VISIBLE
                binding.customerNameText.text = buildString {
                    append(preparationQueueDetails.customer.firstName)
                    append(" ")
                    append(preparationQueueDetails.customer.lastName)
                }
                binding.customerPhoneText.text = preparationQueueDetails.customer.phone
            }
        }
        else {
            binding.customerInfoContainer.visibility =  View.GONE
        }

        // Adapter for the items list for the order selected
        preparationQueueAdapter = PreparationQueueAdapter( preparationQueueSummary.preparationQueueList.toMutableList() ) { item ->
            preparationQueueViewModel.finishPreparationQueue(item)
        }
        binding.preparationQueueList.layoutManager = LinearLayoutManager(context)
        binding.preparationQueueList.adapter = preparationQueueAdapter
    }
}


/**
 * Adapter for list of PreparationQueueSummary
 */
class PreparationQueueSummaryAdapter( private var items: MutableList<PreparationQueueSummary>,
                                      private val selectedIndex: Int,
                                      private val onItemClickListener: (itemView: PreparationQueueSummaryItemView, index: Int) -> Unit)
    : RecyclerView.Adapter<PreparationQueueSummaryViewHolder>() {

    // ViewHolder that holds the reference to the custom view
    class PreparationQueueSummaryViewHolder(val preparationQueueSummaryItemView: PreparationQueueSummaryItemView) : RecyclerView.ViewHolder(preparationQueueSummaryItemView)

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreparationQueueSummaryViewHolder {
        // Inflate the custom view and return the ViewHolder
        val itemView = PreparationQueueSummaryItemView(parent.context)

        // manually set the CustomView's size - this is what I was missing
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return PreparationQueueSummaryViewHolder(itemView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: PreparationQueueSummaryViewHolder, position: Int) {
        val item = items[position]
        holder.preparationQueueSummaryItemView.bindData(item)

        // Set the delete button listener
        holder.preparationQueueSummaryItemView.setOnClickListener {
            onItemClickListener(holder.preparationQueueSummaryItemView, position)
        }
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }
}

/**
 * Adapter for list of PreparationQueue , details of summary
 */
class PreparationQueueAdapter(private var items: MutableList<PreparationQueue>, private val onFinishClickListener: (item: PreparationQueue) -> Unit) : RecyclerView.Adapter<PreparationQueueAdapter.PreparationQueueViewHolder>() {

    // ViewHolder that holds the reference to the custom view
    class PreparationQueueViewHolder(val preparationQueueItemView: PreparationQueueItemView) : RecyclerView.ViewHolder(preparationQueueItemView)

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreparationQueueViewHolder {
        // Inflate the custom view and return the ViewHolder
        val itemView = PreparationQueueItemView(parent.context)

        // manually set the CustomView's size - this is what I was missing
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return PreparationQueueViewHolder(itemView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: PreparationQueueViewHolder, position: Int) {
        val item = items[position]
        holder.preparationQueueItemView.bindData(item)
        // Set the delete button listener
        holder.preparationQueueItemView.setFinishClickListener {
            onFinishClickListener(item)
        }
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }
}