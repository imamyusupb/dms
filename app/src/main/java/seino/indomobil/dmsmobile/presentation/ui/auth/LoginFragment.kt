/*
 * *
 *    Created by Seino Developer on 10/13/22, 8:44 AM                           *
 *    Copyright (c) 2022 DevTeam - New DMS Mobile. All rights reserved.           *
 *                                                                                       *
 *    This project and associated documentation files are limited to be used,            *
 *    reproduced, distributed, copied, modified, merged, published, sublicensed,         *
 *    and/or sold to only authorized staff. Should you find yourself is unauthorized,    *
 *    please do not use this project and its associated documentation files in any       *
 *    kind of intentions and conditions.                                                 *
 *                                                                                       *
 *    In order to obtain access to use and involve in this project, you may proceed      *
 *    to inform the authorized staff. By using and involving in this project, you agree  *
 *    to follow our regulations, terms and conditions.                                   *
 *                                                                                       *
 *    This project and source code may use libraries or frameworks that are released     *
 *    under various Open-Source license. Use of those libraries and frameworks are       *
 *    governed by their own individual licenses.                                         *
 *                                                                                       *
 *    The use of this project and source code follows the guideline as described and     *
 *    explained on confluence seinoindomobil.co.id under DMS Mobile New Platform project.*
 *    Please always refer to the project space to follow the guideline.                  *
 *                                                                                       *
 *    If you have any question, please inform our staff or development leader.           *
 *
 */

package seino.indomobil.dmsmobile.presentation.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import seino.indomobil.dmsmobile.data.common.ResponseWrapper
import seino.indomobil.dmsmobile.presentation.utils.SharedPrefs
import seino.indomobil.dmsmobile.data.source.auth.dto.Login
import seino.indomobil.dmsmobile.databinding.FragmentLoginBinding
import seino.indomobil.dmsmobile.domain.core.auth.LoginEntity
import seino.indomobil.dmsmobile.presentation.utils.BaseFragment
import seino.indomobil.dmsmobile.presentation.utils.showGenericAlertDialog
import seino.indomobil.dmsmobile.presentation.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()
        observe()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            val id = binding.tvUserId.text.toString().trim()
            val password = binding.tvUserPassword.text.toString().trim()
            if (validate(id, password)) {
                val loginRequest = Login.LoginRequest(id, password)
                viewModel.login(loginRequest)
            }
        }
    }

    private fun observe() {
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun validate(id: String, password: String): Boolean {
        resetAllInputError()
        if (id.length < 2) {
            setIdError("Id terlalu pendek")
            return false
        }
        if (password.length < 5) {
            setPasswordError("Password terlalu pendek")
            return false
        }
        return true
    }

    private fun resetAllInputError() {
        setIdError(null)
        setPasswordError(null)
    }

    private fun setIdError(e: String?) {
        binding.tvUserId.error = e
    }

    private fun setPasswordError(e: String?) {
        binding.tvUserPassword.error = e
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.btnLogin.isEnabled = !isLoading
        binding.progressBar.isIndeterminate = isLoading
        if (!isLoading) {
            binding.progressBar.progress = 0
        }
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity) {
        sharedPrefs.saveToken(loginEntity.accessToken)
        requireActivity().showToast("Welcome ${loginEntity.name}")
        goToDashboardActivity()
    }

    private fun goToDashboardActivity() {
        Toast.makeText(requireContext(), "Berhasil Masuk", Toast.LENGTH_LONG).show()
        requireActivity().finish()
    }

    private fun handleStateChange(state: LoginActivityState) {
        when (state) {
            LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
            is LoginActivityState.ShowToast -> requireActivity().showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleErrorLogin(response: ResponseWrapper<Login.LoginResponse>) {
        requireActivity().showGenericAlertDialog(response.status)
    }
}


