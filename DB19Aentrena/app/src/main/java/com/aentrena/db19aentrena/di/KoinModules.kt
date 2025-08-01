package com.aentrena.db19aentrena.di

import com.aentrena.db19aentrena.game.GameViewModel
import com.aentrena.db19aentrena.login.LoginViewModel
import com.aentrena.db19aentrena.repositories.UserRepository
import com.aentrena.db19aentrena.repositories.UserRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val respositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { GameViewModel() }
}