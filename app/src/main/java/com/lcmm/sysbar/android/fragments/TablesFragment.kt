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
import com.lcmm.sysbar.android.adapters.TableListAdapter
import com.lcmm.sysbar.android.databinding.FragmentTablesBinding
import com.lcmm.sysbar.android.models.Table
import com.lcmm.sysbar.android.navigateForward
import com.lcmm.sysbar.android.services.LocalStorageService
import com.lcmm.sysbar.android.viewModel.TableViewModel


class TablesFragment : Fragment() {
    private var _binding: FragmentTablesBinding? = null
    private val binding get() = _binding!!

    private val tableViewModel: TableViewModel by viewModels()
    private lateinit var localStorageService: LocalStorageService
    private lateinit var navController: NavController

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTablesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        initView()
        initListeners()
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
     *
     */
    private fun initListeners() {

    }

    /**
     *
     */
    private fun initObservers() {
        tableViewModel.tablesLiveData.observe(requireActivity()) { tables ->
            handleTablesResponse(tables)
        }

        tableViewModel.fetchTables()
    }

    /**
     *
     */
    private fun handleSelectTable(table: Table){
        table.id?.let { id ->
            val action = TablesFragmentDirections.actionTablesFragmentToOrderFragment(id)
            navController.navigateForward(action)
        }
    }

    /**
     *
     */
    private fun handleTablesResponse(tables: List<Table>) {
        // Create an instance of TableListAdapter and define the click behavior
        val flexboxHelper = TableListAdapter( requireContext(), binding.tablesFlexbox) { item ->
            handleSelectTable(item)
        }
        flexboxHelper.setItems( tables )
    }
}