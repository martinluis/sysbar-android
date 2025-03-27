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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.lcmm.sysbar.android.components.TableItemView
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

    private lateinit var tableItemAdapter: TableItemAdapter

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

        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP  // Allow wrapping
        binding.recyclerView.layoutManager = flexboxLayoutManager
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
        tableItemAdapter = TableItemAdapter(tables) { table ->
            handleSelectTable(table)
        }
        binding.recyclerView.adapter = tableItemAdapter
    }
}


// ItemAdapter.kt
class TableItemAdapter(private var  tables: List<Table>, private val onItemClickListener: (Table) -> Unit ) : RecyclerView.Adapter<TableItemAdapter.TabletItemViewHolder>() {


    // ViewHolder that holds the reference to the custom view
    class TabletItemViewHolder(val tableItemView: TableItemView) :
        RecyclerView.ViewHolder(tableItemView)

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabletItemViewHolder {
        // Inflate the custom view and return the ViewHolder
        val itemView = TableItemView(parent.context)

        // Set FlexboxLayoutParams instead of the default LayoutParams
        val layoutParams = FlexboxLayoutManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set margins
        val scale = 2
        val marginSize = 10.0f
        val dpAsPixels = (marginSize * scale + 0.5f).toInt()
        layoutParams.setMargins(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)

        itemView.layoutParams = layoutParams

        return TabletItemViewHolder(itemView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: TabletItemViewHolder, position: Int) {
        val item = tables[position]
        holder.tableItemView.bindData(item)
        holder.tableItemView.setOnClickListener {
            onItemClickListener(item)
        }
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return tables.size
    }
    
}
