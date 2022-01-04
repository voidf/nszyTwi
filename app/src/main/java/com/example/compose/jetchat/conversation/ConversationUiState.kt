/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.jetchat.conversation

import android.util.Log
import android.util.MutableBoolean
import android.widget.Toast
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.compose.jetchat.*
import io.ktor.client.request.*

class ConversationUiState(
    val channelName: String,
    val channelMembers: Int,
    initialMessages: List<SingleTwiData>
) {
    private val _messages: MutableList<SingleTwiData> =
        mutableStateListOf(*initialMessages.toTypedArray())
    val messages: List<SingleTwiData> = _messages

    private val _onlyFollow: MutableList<Boolean> = mutableStateListOf(false)
    val onlyFollow: List<Boolean> = _onlyFollow

    fun addMessage(msg: SingleTwiData) {
        _messages.add(0, msg) // Add to the beginning of the list
    }

    fun switchOnlyFollow(){
        _onlyFollow[0] = !_onlyFollow[0]
    }

    suspend fun <T> tryUpdateData(typ: String, bdy: T){
        try {
            val r = ktorClient.post<Resp<List<SingleTwiData>>>("$api_host/twi/$typ")
            {
                body=bdy!!
            }
            _messages.clear()
            for (i in r.data)_messages.add(i)
        } catch (e: Exception) {
            Log.d("【UiState】Error!", e.toString())
        }
    }

    suspend fun tryUpdateData(){
        try {
            val typ = if (_onlyFollow.first()) "follows" else "all"
            val r = ktorClient.get<Resp<List<SingleTwiData>>>("$api_host/twi/$typ")
            _messages.clear()
            for (i in r.data)_messages.add(i)
        } catch (e: Exception) {
            Log.d("【UiState】Error!", e.toString())
        }
    }

    fun pullMessages(msgs: MutableList<SingleTwiData>){
        _messages.clear()
        for (i in msgs)_messages.add(i)
    }
}

//@Immutable
//data class Message(
//    val author: String,
//    val content: String,
//    val timestamp: String,
//    val image: Int? = null,
//    val authorImage: Int = if (author == "me") R.drawable.ali else R.drawable.someone_else,
//    val likecount: Int = 0,
//    val commentcount: Int = 0,
//)
