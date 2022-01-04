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

package com.example.compose.jetchat.profile

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compose.jetchat.SingleTwiData
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.*
import com.example.compose.jetchat.data.exampleUiState
import com.example.compose.jetchat.data.useryaya
import io.ktor.client.request.*

class TwiDetailViewModel : ViewModel() {

    var tid: String = ""
    fun updtid(newtid: String){
        if (newtid == tid) {
            return
        }
        tid = newtid
//        _twidata.value = sampleTwi
    }

    suspend fun upd() {
        val r = ktorClient.get<Resp<SingleTwiData>>("$api_host/twi/detail?tid=$tid")
        _twidata.value = r.data!!
        _comments.clear()
        if (r.data.comments != null) {
            for (i in r.data.comments.reversed()){
                _comments.add(i)
            }
        }
    }

    private val _twidata = MutableLiveData<SingleTwiData>(sampleTwi)

    private val _comments: MutableList<SingleTwiData> = mutableStateListOf(*exampleUiState.messages.toTypedArray())
    val twidata: LiveData<SingleTwiData> = _twidata
    val comments: List<SingleTwiData> = _comments
}

val sampleTwi = SingleTwiData(
    "32143141",
    "dhcvakugsdhnf",
    useryaya,
    false,
    1641295619
)

//@Immutable
//data class TwiDetailState(
//    val id: String,
//    val content: String,
//    val author: UserData,
//    val is_top: Boolean,
//    val post_time: Long,
//    val comments: List<SingleTwiData>? = null
//) {
//}
