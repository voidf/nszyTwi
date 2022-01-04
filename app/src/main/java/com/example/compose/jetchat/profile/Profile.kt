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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.FunctionalityNotAvailablePopup
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.AnimatingFabContent
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.components.baselineHeight
import com.example.compose.jetchat.data.colleagueProfile
import com.example.compose.jetchat.data.meProfile
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userData: ProfileScreenState,
    isFollowed: Boolean,
    onFollowClick: () -> Unit,
    onNavIconPressed: () -> Unit = { }
) {

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    val scrollState = rememberScrollState()
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
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    ProfileHeader(
                        scrollState,
                        userData,
                        this@BoxWithConstraints.maxHeight
                    )
                    UserInfoFields(userData, this@BoxWithConstraints.maxHeight)
                }
            }
            ProfileFab(
                extended = scrollState.value == 0,
                userIsMe = userData.isMe(),
                modifier = Modifier.align(Alignment.BottomEnd),
                onFabClicked = onFollowClick,
                isFollowed = isFollowed
            )
        }
    }
}

@Composable
private fun UserInfoFields(userData: ProfileScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        NameAndPosition(userData)

        ProfileProperty(stringResource(R.string.display_name), userData.displayName)

        ProfileProperty(stringResource(R.string.status), userData.status)

        ProfileProperty(stringResource(R.string.twitter), userData.twitter, isLink = true)

        userData.timeZone?.let {
            ProfileProperty(stringResource(R.string.timezone), userData.timeZone)
        }

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPosition(
    userData: ProfileScreenState
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(
            userData,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Position(
            userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        text = userData.name,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun Position(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        text = userData.position,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ProfileHeader(
    scrollState: ScrollState,
    data: ProfileScreenState,
    containerHeight: Dp
) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }

    data.photo?.let {
        Image(
            modifier = Modifier
                .heightIn(max = containerHeight / 2)
                .fillMaxWidth()
                // TODO: Update to use offset to avoid recomposition
                .padding(
                    start = 16.dp,
                    top = offsetDp,
                    end = 16.dp
                )
                .clip(CircleShape),
            painter = painterResource(id = it),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        // TODO (M3): No Divider, replace when available
        Divider()
        Text(
            text = label,
            modifier = Modifier.baselineHeight(24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val style = if (isLink) {
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        } else {
            MaterialTheme.typography.bodyLarge
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
fun ProfileError() {
    Text(stringResource(R.string.profile_error))
}

@Composable
fun ProfileFab(
    extended: Boolean,
    userIsMe: Boolean,
    isFollowed: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = { }
) {

    key(isFollowed) { // Prevent multiple invocations to execute during composition
        FloatingActionButton(
            onClick = onFabClicked,
            modifier = modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            AnimatingFabContent(
                icon = {
                    Icon(
                        imageVector = if (isFollowed) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(
                            if (isFollowed) R.string.unfollow else R.string.follow
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            id = if (isFollowed) R.string.unfollow else R.string.follow
                        ),
                    )
                },
                extended = extended

            )
        }
    }
}

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun ConvPreviewLandscapeMeDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileScreen(meProfile,false, {})
        }
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun ConvPreviewPortraitMeDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileScreen(meProfile, isFollowed = false, {})

        }
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun ConvPreviewPortraitOtherDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileScreen(colleagueProfile, isFollowed = true, {})
        }
    }
}

@Preview
@Composable
fun ProfileFabPreview() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileFab(extended = true, userIsMe = false, isFollowed = false)
        }
    }
}
@Preview
@Composable
fun ProfileFabPreview2() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        JetchatTheme {
            ProfileFab(extended = true, userIsMe = false, isFollowed = true)
        }
    }
}
