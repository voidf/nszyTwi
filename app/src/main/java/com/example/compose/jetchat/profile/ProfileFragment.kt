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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.compose.jetchat.MainViewModel
import com.example.compose.jetchat.NavActivity
import com.example.compose.jetchat.sendFo
import com.example.compose.jetchat.sendUnfo
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ViewWindowInsetObserver
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Consider using safe args plugin
        val userId = arguments?.getString("userId")
        viewModel.setUserId(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


        // Create a ViewWindowInsetObserver using this view, and call start() to
        // start listening now. The WindowInsets instance is returned, allowing us to
        // provide it to AmbientWindowInsets in our content below.
        val windowInsets = ViewWindowInsetObserver(this).start()


        setContent {
            val userData by viewModel.userData.observeAsState()
            val isFollowed by viewModel.is_followed.observeAsState()
            val myud by viewModel.userDetailedInfo.observeAsState()

            CompositionLocalProvider(LocalWindowInsets provides windowInsets) {
                JetchatTheme {
                    val scope = rememberCoroutineScope()
                    val ctx = LocalContext.current
                    if (userData == null) {
                        ProfileError()
                    } else {
                        LaunchedEffect(Unit) {
                            viewModel.upd()
                        }
                        ProfileScreen(
                            userData = userData!!,
                            onNavIconPressed = {
                                activityViewModel.openDrawer()
                            },
                            isFollowed = isFollowed!!,
                            onFollowClick = {
                                scope.launch {
                                    if(isFollowed!!){
                                        var ret = sendUnfo(myud!!.username)
                                        NavActivity.singleton!!.viewModel.updfolist()
                                        Toast.makeText(ctx, if(ret.length==0)"????????????" else ret, Toast.LENGTH_LONG).show()
                                    }else{
                                        var ret = sendFo(myud!!.username)
                                        NavActivity.singleton!!.viewModel.updfolist()
                                        Toast.makeText(ctx, if(ret.length==0)"????????????" else ret, Toast.LENGTH_LONG).show()
                                    }
                                    viewModel.upd()
                                }
                            }
                        )

                    }
                }
            }
        }
    }
}
