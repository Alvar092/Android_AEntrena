package com.aentrena.db19aentrena.di

import com.aentrena.db19aentrena.domain.hero.HeroRepository
import com.aentrena.db19aentrena.domain.hero.HeroRepositoryImpl
import com.aentrena.db19aentrena.game.GameViewModel
import com.aentrena.db19aentrena.presentation.LoginViewModel
import com.aentrena.db19aentrena.domain.login.UserRepository
import com.aentrena.db19aentrena.domain.login.UserRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val respositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<HeroRepository> { HeroRepositoryImpl() }
}
val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { GameViewModel(get()) }
}