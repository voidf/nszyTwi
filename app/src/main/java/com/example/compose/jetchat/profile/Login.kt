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

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.R
import com.example.compose.jetchat.api_host
import com.example.compose.jetchat.conversation.UserInputText
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.ktorClient
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.ProvideWindowInsets
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

var AccessToken: String = ""
var UserName: String = ""

@Serializable
data class tokenResponse(
    val access_token: String,
)

@Serializable
data class trueReturn(
    val msg: String,
)

@Serializable
data class loginForm(
    val username: String,
    val password: String
)

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(afterLogin: () -> Unit) {
    val ctx = LocalContext.current

    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf(TextFieldValue("?????????")) }
    var password by remember { mutableStateOf(TextFieldValue("??????")) }


    Column(
        modifier = Modifier
            .fillMaxHeight()

    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    editableTextT2(
                        onTextChanged = { username = it },
                        textFieldValue = username
                    )
                    editableTextT2(
                        onTextChanged = { password = it },
                        textFieldValue = password
                    )
                    Row() {
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val r = ktorClient.post<String>("$api_host/user/login") {
                                            body = loginForm(
                                                username = username.text,
                                                password = password.text
                                            )
                                        }
                                        val res = Json {
                                            ignoreUnknownKeys = true
                                        }.decodeFromString<tokenResponse>(r)
                                        AccessToken = res.access_token
                                        meProfile = ProfileScreenState(
                                            userId = username.text,
                                            photo = R.drawable.yaya_avatar,
                                            name = username.text,
                                            displayName = username.text,
                                            position="",
                                            status="",
                                            twitter = "twitter.com/aliconors",
                                            timeZone = "",
                                            commonChannels = null
                                        )

                                        UserName = username.text
                                        Toast.makeText(ctx, "????????????", Toast.LENGTH_LONG)
                                            .show()
                                        afterLogin()

                                    } catch (e: Exception) {
                                        Toast.makeText(ctx, "$e\n????????????", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 12.dp,
                                end = 20.dp,
                                bottom = 12.dp
                            )
                        ) {
                            Icon(
                                Icons.Filled.Login,
                                contentDescription = "??????",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("??????")
                        }
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing * 5))
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val r = ktorClient.post<String>("$api_host/user/register") {
                                            body = loginForm(
                                                username = username.text,
                                                password = password.text
                                            )
                                        }
                                        val res = Json {
                                            ignoreUnknownKeys = true
                                        }.decodeFromString<trueReturn>(r)
                                        if (res.msg.length == 0)
                                            Toast.makeText(ctx, "????????????", Toast.LENGTH_LONG).show()

                                    } catch (e: Exception) {
                                        Toast.makeText(ctx, "$e\n????????????", Toast.LENGTH_LONG).show()
                                    }


                                }
                            },
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 12.dp,
                                end = 20.dp,
                                bottom = 12.dp
                            )
                        ) {
                            // Inner content including an icon and a text label
                            Icon(
                                Icons.Filled.AppRegistration,
                                contentDescription = "??????",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("??????")
                        }
                    }
                }
            }

        }
    }
}

@ExperimentalFoundationApi
@Composable
fun editableText(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf(TextFieldValue("fudgasuky")) }
    var textFieldFocusState by remember { mutableStateOf(false) }
    UserInputText(
        textFieldValue = textState,
        onTextChanged = { textState = it },
        // Only show the keyboard if there's no input selector and text field has focus
        keyboardShown = textFieldFocusState,
        // Close extended selector if text field receives focus
        onTextFieldFocused = { focused ->
            textFieldFocusState = focused
        },
        focusState = textFieldFocusState
    )
}

@ExperimentalFoundationApi
@Composable
fun editableTextT2(
    modifier: Modifier = Modifier,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
) {
    var textFieldFocusState by remember { mutableStateOf(false) }
    UserInputText(
        textFieldValue = textFieldValue,
        onTextChanged = onTextChanged,
        // Only show the keyboard if there's no input selector and text field has focus
        keyboardShown = textFieldFocusState,
        // Close extended selector if text field receives focus
        onTextFieldFocused = { focused ->
            textFieldFocusState = focused
        },
        focusState = textFieldFocusState
    )
}


@ExperimentalFoundationApi
@Preview(widthDp = 360, heightDp = 480)
@Composable
fun loginPreview() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
//            editableText()
            LoginScreen(afterLogin = {})
        }
    }
}

