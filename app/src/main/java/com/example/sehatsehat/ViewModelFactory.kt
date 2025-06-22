package com.example.sehatsehat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sehatsehat.viewmodel.AdminHomepageViewModel
import com.example.sehatsehat.viewmodel.ChatbotViewModel
import com.example.sehatsehat.viewmodel.CustomerServiceViewModel
import com.example.sehatsehat.viewmodel.CustomerTopupViewModel
import com.example.sehatsehat.viewmodel.HomeViewModel
import com.example.sehatsehat.viewmodel.PaymentActivityViewModel
import com.example.sehatsehat.viewmodel.ProfileViewModel
import com.example.sehatsehat.viewmodel.ViewProgramViewModel

val SehatViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val application = checkNotNull(extras[APPLICATION_KEY]) as SehatApplication
            val sehatRepository = application.sehatRepository

            when {
                isAssignableFrom(ChatbotViewModel::class.java) ->
                    ChatbotViewModel(sehatRepository)
                isAssignableFrom(AdminHomepageViewModel::class.java) ->
                    AdminHomepageViewModel(sehatRepository)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(sehatRepository)
                isAssignableFrom(HomeViewModel::class.java)->
                    HomeViewModel(sehatRepository)
                isAssignableFrom(CustomerServiceViewModel::class.java)->
                    CustomerServiceViewModel(sehatRepository)
                isAssignableFrom(CustomerTopupViewModel::class.java)->
                    CustomerTopupViewModel(sehatRepository)
                isAssignableFrom(ViewProgramViewModel::class.java)->
                    ViewProgramViewModel(sehatRepository)
                isAssignableFrom(PaymentActivityViewModel::class.java)->
                    PaymentActivityViewModel(sehatRepository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}