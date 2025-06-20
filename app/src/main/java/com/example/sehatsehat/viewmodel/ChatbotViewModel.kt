package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.model.ChatLogEntity
import kotlinx.coroutines.launch

class ChatbotViewModel(
    private val sehatRepository: SehatRepository
):ViewModel(){
    private val _chatMessages = MutableLiveData<List<ChatLogEntity>>()
    val chatMessages: LiveData<List<ChatLogEntity>>
        get() = _chatMessages

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private val _currentMessage = MutableLiveData<String>()
    val currentMessage: LiveData<String>
        get() = _currentMessage

    private val _chatGroup = MutableLiveData<String>()
    val chatGroup: LiveData<String>
        get() = _chatGroup

    fun init(username:String, chatGroup:String){
        viewModelScope.launch {
            chatbotSync(chatGroup)
            _chatMessages.value = sehatRepository.getFromChatbotChatLog(username).sortedByDescending{
                it.createdAt
            }
            _username.value = username
            _chatGroup.value = chatGroup
            Log.d("ChatbotViewModel", "init: ${_username.value}")
            Log.d("ChatbotViewModel", "init: ${_chatGroup.value}")
            Log.d("ChatbotViewModel", "init: ${_chatMessages.value.size}")
        }
    }

    fun chatbotSync(chatGroup:String){
        viewModelScope.launch {
            sehatRepository.chatGroupSync(chatGroup)
            if(username.value != null){
                _chatMessages.value = sehatRepository.getFromChatbotChatLog(username.value).sortedByDescending{
                    it.createdAt
                }
            }
        }
    }

    fun updateMessage(newMessage:String){
        _currentMessage.value = newMessage
    }

    fun sendMessages(){
        viewModelScope.launch {
            sehatRepository.chatToChatbot(currentMessage.value!!,username.value!!, chatGroup.value!!)
            _chatMessages.value = sehatRepository.getFromChatbotChatLog(username.value!!).sortedByDescending{
                it.createdAt
            }
            Log.d("ChatbotViewModel", "sendMessages: ${_chatMessages.value.size}")
        }
    }
}