package com.lcmm.sysbar.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lcmm.sysbar.android.databinding.FragmentOrderBinding
import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.services.LocalStorageService
import com.lcmm.sysbar.android.viewModel.OrderViewModel

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var localStorageService: LocalStorageService
    private lateinit var navController: NavController

    // This will retrieve the userId passed via Safe Args
    private val args: OrderFragmentArgs by navArgs()


    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        initView()
        initObservers()
        return binding.root
    }

    /**
     *
     */
    private fun initView() {
        navController = findNavController()
        localStorageService = LocalStorageService(requireContext())
    }

    /**
     *
     */
    private fun initObservers() {
        orderViewModel.orderLiveData.observe(requireActivity()) { order ->
            initViewWithOrder(order)
        }
        orderViewModel.getByTable( args.tableId )
    }

    /**
     *
     */
    private fun initViewWithOrder(order: Order){

    }

}