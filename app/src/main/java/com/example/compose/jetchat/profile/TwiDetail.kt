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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.compose.jetchat.conversation.*
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
    commentsState: List<SingleTwiData>,
    modifier: Modifier = Modifier,
    onSendComment: (String) -> Unit,

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
        Surface{
            Column {
                Row {
                    Image(
                        modifier = Modifier
                            .clickable(onClick = { navigateToProfile(twidata.author.username) })
                            .padding(horizontal = 16.dp)
                            .size(42.dp)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .clip(CircleShape)
                            .align(Alignment.Top),
                        painter = painterResource(id = R.drawable.irori_avatar),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                    AuthorAndTextMessage(
                        msg = twidata,
                        isUserMe = false,
                        isFirstMessageByAuthor = true,
                        isLastMessageByAuthor = true,
                        authorClicked = { navigateToProfile(twidata.author.username) },
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .weight(1f)
                    )
                }
                DayHeader(dayString = "Comments")
            }
        }
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    Messages(
                        messages = commentsState,
                        navigateToProfile = navigateToProfile,
                        scrollState = scrollStateLazy,
                        onCommentClick = onCommentClick,
                        onLikeClick = onLikeClick
                    )

                }
            }
        }
        UserInput(
            onMessageSent = { content -> onSendComment(content)},
            resetScroll = {
            },
            modifier = Modifier.navigationBarsWithImePadding(),
        )
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
        commentsState = listOf(),
        onSendComment = {}
    )

}

@Composable
fun TwiDetailError() {
    Text(stringResource(R.string.profile_error))
}
