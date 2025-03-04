package com.lcmm.sysbar.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lcmm.sysbar.android.MainActivity
import com.lcmm.sysbar.android.databinding.FragmentHomeBinding
import com.lcmm.sysbar.android.enums.Role
import com.lcmm.sysbar.android.services.LocalStorageService
import com.lcmm.sysbar.android.utils.SecurityUtils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var localStorageService: LocalStorageService
    private lateinit var navController: NavController

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        initView()
        initListeners()
        return this.binding.root
    }

    /**
     *
     */
    private fun initView() {
        navController = findNavController()
        localStorageService = LocalStorageService(requireContext())
        (activity as MainActivity).updateUserInfo(localStorageService.getActiveUser()!!)
        val user = localStorageService.getActiveUser()
        binding.tablesItem.visibility = if (SecurityUtils.hasPermissions(user?.roles!!, Role.CASHIER)) View.VISIBLE else View.GONE
    }

    /**
     *
     */
    private fun initListeners() {
        binding.tablesItem.setOnClickListener {
            var v = 1;
            v = 3;
        }
    }

}