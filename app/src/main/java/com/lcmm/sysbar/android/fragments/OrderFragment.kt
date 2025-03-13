package com.lcmm.sysbar.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lcmm.sysbar.android.components.OrderSummaryActionsListener
import com.lcmm.sysbar.android.databinding.FragmentOrderBinding
import com.lcmm.sysbar.android.enums.OrderStatus
import com.lcmm.sysbar.android.enums.OrderType
import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.models.OrderItem
import com.lcmm.sysbar.android.models.Table
import com.lcmm.sysbar.android.services.LocalStorageService
import com.lcmm.sysbar.android.viewModel.OrderViewModel
import com.lcmm.sysbar.android.viewModel.ProductViewModel
import com.lcmm.sysbar.android.viewModel.TableViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderViewModel by viewModels()
    private val tableViewModel: TableViewModel by viewModels()
    private lateinit var productViewModel: ProductViewModel

    private lateinit var localStorageService: LocalStorageService
    private lateinit var navController: NavController

    private var table: Table? = null
    private var order: Order? = null

    // This will retrieve the userId passed via Safe Args
    private val args: OrderFragmentArgs by navArgs()


    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        initView()
        initListeners()
        initObservers()
        return binding.root
    }

    /**
     *
     */
    private fun initView() {
        navController = findNavController()
        localStorageService = LocalStorageService(requireContext())
        productViewModel = ViewModelProvider(requireActivity())[ProductViewModel::class.java]
    }

    /**
     *
     */
    private fun initView(order: Order) {
        this.order = order
        binding.orderSummaryView.setOrder(order)
    }

    /**
     *
     */
    private fun initListeners() {
        val summaryOrderViewListener = object: OrderSummaryActionsListener {
            override fun onConfirmButtonClick() {
                val orderItems = binding.orderSummaryView.getNewItems()
                val orderItemsFiltered = orderItems.filter { it.quantity > 0 }
                if (orderItemsFiltered.isNotEmpty()) {
                    orderViewModel.addItemsToOrder(order?.id!!, orderItems)
                }
            }

            override fun onCancelButtonClick() {
               binding.orderSummaryView.removeNewItems()
            }
        }
        binding.orderSummaryView.setOrderSummaryActionsListener(summaryOrderViewListener)
    }

    /**
     *
     */
    private fun initObservers() {
        orderViewModel.orderLiveData.observe(requireActivity()) { order ->
            initView(order)
        }

        orderViewModel.errorLiveData.observe(requireActivity()) { error ->
            if (error.errorCodes != null && error.errorCodes!!.isNotEmpty()) {
                if (error.errorCodes!!.contains("table.without.active.order")){
                    val order = createEmptyOrder()
                    initView(order)
                }
            }
        }

        tableViewModel.tableLiveData.observe(requireActivity()) { table ->
            this.table = table
            orderViewModel.getByTable( table.id!! )
        }

        lifecycleScope.launch {
            orderViewModel.orderState.collect { result ->
                result?.onSuccess { order ->
                    initView(order)
                }
                result?.onFailure { exception ->
                    // Handle failure (e.g., show an error message)
                    Toast.makeText(context, "Error creating order: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        productViewModel.productLiveData.observe(requireActivity()) { product ->
            val orderItem = OrderItem(
                null, product.id, product.name, product.price, 1
            )
            binding.orderSummaryView.addItem(orderItem)
        }

        // Call to services after init the observers
        tableViewModel.getTable(args.tableId)
    }

    /**
     *
     */
    private fun createEmptyOrder(): Order{
        val user = localStorageService.getActiveUser()
        val order = Order(null, user!!,this.table!!, mutableListOf() ,OrderType.LOCAL, OrderStatus.ACTIVE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        return order
    }

}