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

package com.example.compose.jetchat.data

import com.example.compose.jetchat.R
import com.example.compose.jetchat.SingleTwiData
import com.example.compose.jetchat.UserData
import com.example.compose.jetchat.conversation.ConversationUiState
import com.example.compose.jetchat.conversation.Message
import com.example.compose.jetchat.profile.ProfileScreenState
/*
private val initialMessages = listOf(
    Message(
        "me",
        "Check it out!",
        "8:07 PM"
    ),
    Message(
        "me",
        "Thank you!",
        "8:06 PM",
        R.drawable.sticker
    ),
    Message(
        "Taylor Brooks",
        "You can use all the same stuff",
        "8:05 PM"
    ),
    Message(
        "Taylor Brooks",
        "@aliconors Take a look at the `Flow.collectAsState()` APIs",
        "8:05 PM"
    ),
    Message(
        "John Glenn",
        "Compose newbie as well, have you looked at the JetNews sample? Most blog posts end up " +
            "out of date pretty fast but this sample is always up to date and deals with async " +
            "data loading (it's faked but the same idea applies) \uD83D\uDC49" +
            "https://github.com/android/compose-samples/tree/master/JetNews",
        "8:04 PM"
    ),
    Message(
        "me",
        "Compose newbie: I’ve scourged the internet for tutorials about async data loading " +
            "but haven’t found any good ones. What’s the recommended way to load async " +
            "data and emit composable widgets?",
        "8:03 PM"
    )
)
*/
val useryaya = UserData("yaya", "yaya_avatar", "Y")
val userirori = UserData("irori", "irori_avatar", "I")
val userkomurasaki = UserData("komurasaki", "komurasaki_avatar", "K")

private val initialMessages = listOf(
    SingleTwiData(
        "1",
        "Check it out!",
        useryaya,
        true,
        1641295619
    ),
    SingleTwiData(
        "2",
        "Thank you!",
        useryaya,
        true,
        1641295639
    ),

    SingleTwiData(
        "3",
        "You can use all the same stuff",
        userirori,
        true,
        1641295659
    ),
    SingleTwiData(
        "4",
        "@aliconors Take a look at the `Flow.collectAsState()` APIs",
        userirori,
        true,
        1641295759
    ),
    SingleTwiData(
        "5",
        "Compose newbie as well, have you looked at the JetNews sample? Most blog posts end up " +
                "out of date pretty fast but this sample is always up to date and deals with async " +
                "data loading (it's faked but the same idea applies) \uD83D\uDC49" +
                "https://github.com/android/compose-samples/tree/master/JetNews",
        userkomurasaki,
        true,
        1641298859
    ),
    SingleTwiData(
        "6",
        "Compose newbie: I’ve scourged the internet for tutorials about async data loading " +
                "but haven’t found any good ones. What’s the recommended way to load async " +
                "data and emit composable widgets?",
        useryaya,
        true,
        1641299859
    ),

)

val exampleUiState = ConversationUiState(
    initialMessages = initialMessages,
    channelName = "#composers",
    channelMembers = 42
)

/**
 * Example colleague profile
 */
val colleagueProfile = ProfileScreenState(
    userId = "12345",
    photo = R.drawable.irori_avatar,
    name = "Irori",
    status = "Away",
    displayName = "irori",
    position = "Senior Android Dev at Openlane",
    twitter = "twitter.com/taylorbrookscodes",
    timeZone = "12:25 AM local time (Eastern Daylight Time)",
    commonChannels = "2"
)

/**
 * Example "me" profile.
 */
var meProfile = ProfileScreenState(
    userId = "me",
    photo = R.drawable.yaya_avatar,
    name = "Yaya",
    status = "Online",
    displayName = "yaya",
    position = "Senior Android Dev at Yearin\nGoogle Developer Expert",
    twitter = "twitter.com/aliconors",
    timeZone = "In your timezone",
    commonChannels = null
)

var errorUserData = UserData(
    "ERROR!",
    "ERROR!",
    "ERROR!",
)