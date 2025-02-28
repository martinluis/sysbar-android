package com.lcmm.sysbar.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.lcmm.sysbar.android.databinding.FragmentAccessBinding
import com.lcmm.sysbar.android.models.ErrorResponse
import com.lcmm.sysbar.android.models.User
import com.lcmm.sysbar.android.utils.ErrorHandler
import com.lcmm.sysbar.android.viewModel.UserViewModel


class AccessFragment : Fragment() {

    private var _binding: FragmentAccessBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()


    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccessBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        initView()
        initObservers()
        return this.binding.root
    }

    /**
     *
     */
    private fun initView() {
        binding.accessMsgText.visibility = View.GONE
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

        userViewModel.requestUserAccess("dev")
    }

    /**
     *
     */
    private fun handleResponseUserAccess(user: User) {
        binding.accessMsgText.visibility = View.GONE
        user.let {
            binding.accessMsgText.text = it.role
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