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

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.*
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.AnimatingFabContent
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.components.baselineHeight
import com.example.compose.jetchat.conversation.ConversationUiState
import com.example.compose.jetchat.conversation.Messages
import com.example.compose.jetchat.conversation.UserInput
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.exampleUiState
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.data.useryaya
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwiDetailScreen(
    twidata: SingleTwiData,
    navigateToProfile: (String) -> Unit,
    uiState: ConversationUiState,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { },
    onCommentClick: (String) -> Unit = { },
    onLikeClick: (String) -> Unit = { }
) {

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val scrollStateLazy = rememberLazyListState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        JetchatAppBar(
            // Use statusBarsPadding() to move the app bar content below the status bar
            modifier = Modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            onNavIconPressed = onNavIconPressed,
            title = { },
            actions = {
                // More icon
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp),
                    contentDescription = stringResource(id = R.string.more_options)
                )
            }
        )
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {


                Column(
                    Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    Messages(
                        messages = uiState.messages,
                        navigateToProfile = navigateToProfile,
                        scrollState = scrollStateLazy,
                        onCommentClick = onCommentClick,
                        onLikeClick = onLikeClick
                    )
                    UserInput(
                        onMessageSent = { content ->
                        },
                        resetScroll = {
                        },
                        modifier = Modifier.navigationBarsWithImePadding(),
                    )
                }


            }

        }
    }
}

@Preview
@Composable
fun tPreview(){
    TwiDetailScreen(
        twidata = SingleTwiData(
            "id",
            "content",
            useryaya,
            true,
            1145141919
        ),
        navigateToProfile = {},
        uiState = exampleUiState
    )

}

@Composable
fun TwiDetailError() {
    Text(stringResource(R.string.profile_error))
}
