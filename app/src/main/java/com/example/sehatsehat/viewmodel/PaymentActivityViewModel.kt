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
    // LiveData untuk paymentUrl & newBalance
    private val _paymentUrl = MutableLiveData<String?>()
    val paymentUrl: LiveData<String?> = _paymentUrl

    private val _newBalance = MutableLiveData<Int>()
    val newBalance: LiveData<Int> = _newBalance

    suspend fun pay(programId: String, userId: String, price: Int, viaMidtrans: Boolean) {
        val req = PaymentRequest(
            transactionDetails = TransactionDetails(order_id = programId.toString(), gross_amount = price),
            acquirer = if (viaMidtrans) "gopay" else "Balance",
            userId = userId,
            programPrice = price
        )
        val resp = sehatRepository.createPaymentTransaction(req)
        if (!viaMidtrans) {
            // langsung Balance
            _newBalance.postValue(resp.newBalance ?: 0)
            _paymentUrl.postValue(null)
        } else {
            // Midtrans QR
            _paymentUrl.postValue(resp.payment_url)
        }
    }
}