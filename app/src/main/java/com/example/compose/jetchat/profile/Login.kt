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

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.FunctionalityNotAvailablePopup
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.AnimatingFabContent
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.components.baselineHeight
import com.example.compose.jetchat.conversation.InputSelector
import com.example.compose.jetchat.conversation.UserInput
import com.example.compose.jetchat.conversation.UserInputText
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {

    var username by remember { mutableStateOf(TextFieldValue("用户名")) }
    var password by remember { mutableStateOf(TextFieldValue("密码")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column() {
                    editableTextT2(
                        onTextChanged = { username = it},
                        textFieldValue = username
                    )
                    editableTextT2(
                        onTextChanged = { password = it},
                        textFieldValue = password
                    )
                    Box(

                    )
                }
            }

        }
    }
}

@ExperimentalFoundationApi
@Composable
fun editableText(modifier: Modifier = Modifier)
{
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
)
{
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
            LoginScreen()
        }
    }
}

