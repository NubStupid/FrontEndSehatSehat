package com.example.sehatsehat.viewmodel

import androidx.compose.runtime.collection.MutableVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
//import com.example.sehatsehat.data.sources.remote.CustomerDetails
import com.example.sehatsehat.data.sources.remote.PaymentRequest
import com.example.sehatsehat.data.sources.remote.PaymentResponse
import com.example.sehatsehat.data.sources.remote.TransactionDetails
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivityViewModel (
    private val sehatRepository: SehatRepository
):ViewModel(){

    private val _paymentUrl = MutableLiveData<String>()
    val paymentUrl:LiveData<String>
        get() = _paymentUrl

    fun createPaymentTransaction(programId: String) {
        viewModelScope.launch {
            val transactionDetails = TransactionDetails(programId,1)
            val paymentRequest = PaymentRequest(transactionDetails)
            val url = sehatRepository.createPaymentTransaction(paymentRequest).payment_url
            if(url != null){
                _paymentUrl.value = url.toString()
                val u = sehatRepository.getUrl()
                if(u != null){
                    sehatRepository.deleteUrl()
                }
                sehatRepository.insertUrl(url)
            }
        }
    }

    fun fetchURL(b:Boolean,programId:String){
        viewModelScope.launch {
            if(b){
                val u = sehatRepository.getUrl()
                if(u == null){
                    createPaymentTransaction(programId)
                }else{
                    _paymentUrl.value = u!!
                }
            }else{
                _paymentUrl.value = ""
            }
        }
    }



}