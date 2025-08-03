package com.aentrena.db19aentrena

import com.aentrena.db19aentrena.domain.login.UserRepository
import com.aentrena.db19aentrena.login.LoginViewModel
import kotlinx.coroutines.test.runTest


class LoginViewModelTest {
    private lateinit var userRepositoryMocked: UserRepository = mock()
    private lateinit var viewModel: LoginViewModel

    private fun mock(): UserRepository {}

    val viewModel = LoginViewModel(userRepositoryMocked)

    fun `login success`() {
        runTest {
            
        }
    }

}