package com.lcmm.sysbar.android.fragments

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.databinding.FragmentAccessBinding
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.User
import com.lcmm.sysbar.android.services.LocalStorageService
import com.lcmm.sysbar.android.utils.ErrorHandler
import com.lcmm.sysbar.android.viewModel.UserViewModel


class AccessFragment : Fragment() {

    private var _binding: FragmentAccessBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var localStorageService: LocalStorageService
    private lateinit var navController: NavController

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccessBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
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
        binding.accessMsgText.visibility = View.GONE
    }

    /**
     *
     */
    private fun initListeners() {
        binding.accessButton.setOnClickListener {
            val code = binding.codeInput.editText?.text.toString()
            if (code.isNotEmpty()) {
                userViewModel.requestUserAccess(code)
            }
        }

        binding.codeInput.editText?.setOnEditorActionListener { _, actionId, _ ->
            val code = binding.codeInput.editText?.text.toString()
            if (code.isNotEmpty()) {
                userViewModel.requestUserAccess(code)
            }
            true
        }

        binding.codeInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val code = binding.codeInput.editText?.text.toString()
                userViewModel.requestUserAccess(code)
                true // Return true to indicate that the key event is handled
            } else {
                false
            }
        }
    }

    /**
     *
     */
    private fun initObservers() {
        userViewModel.userLiveData.observe(requireActivity()) { user ->
            handleResponseUserAccess(user)
        }
        userViewModel.errorLiveData.observe(requireActivity()) { error ->
            handleResponseError(error)
        }

    }

    /**
     *
     */
    private fun handleResponseUserAccess(user: User) {
        binding.accessMsgText.visibility = View.GONE
        user.let {
            localStorageService.setActiveUser(user)
            navController.navigate(R.id.action_accessFragment_to_homeFragment)
        }
    }

    /**
     *
     */
    private fun handleResponseError(errorResponse: ErrorResponse) {
        errorResponse.let {
            binding.accessMsgText.visibility = View.VISIBLE
            if (errorResponse.errorCodes.orEmpty().isNotEmpty()) {
                errorResponse.errorCodes?.forEach { code ->
                    binding.accessMsgText.text = ErrorHandler.getErrorMessage(requireContext(), code)
                }
            } else {
                binding.accessMsgText.text = errorResponse.message
            }
        }
    }


}