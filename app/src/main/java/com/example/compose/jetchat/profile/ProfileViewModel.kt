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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compose.jetchat.UserData
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.errorUserData
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.getUser

class ProfileViewModel : ViewModel() {

    private var userId: String = ""



    fun setUserId(newUserId: String?) {
        if (newUserId != userId) {
            userId = newUserId ?: meProfile.userId
        }
        // Workaround for simplicity
        _userData.value = if (userId == meProfile.userId || userId == meProfile.displayName) {
            meProfile
        } else {
            colleagueProfile
        }
    }

    suspend fun upd() {
        val othersdata = getUser(userId)
        _userDetailedInfo.value = othersdata
        val mydata = getUser(meProfile.userId)
        _userData.value!!.name = mydata.username
        _userData.value!!.userId = mydata.username
        _is_followed.value = false
        if(mydata.follows!=null)
        {
            for(i in mydata.follows){
                if (i.username==userId){
                    _is_followed.value = true
                    return
                }
            }
        }
    }
    private var _is_followed = MutableLiveData<Boolean>(false)
    val is_followed: LiveData<Boolean> = _is_followed

    private val _userDetailedInfo = MutableLiveData<UserData>(errorUserData)
    val userDetailedInfo: LiveData<UserData> = _userDetailedInfo

    private val _userData = MutableLiveData<ProfileScreenState>(meProfile)
    val userData: LiveData<ProfileScreenState> = _userData
}

data class ProfileScreenState(
    var userId: String,
    @DrawableRes val photo: Int?,
    var name: String,
    val status: String,
    val displayName: String,
    val position: String,
    val twitter: String = "",
    val timeZone: String?, // Null if me
    val commonChannels: String? // Null if me
) {
    fun isMe() = userId == meProfile.userId
}
