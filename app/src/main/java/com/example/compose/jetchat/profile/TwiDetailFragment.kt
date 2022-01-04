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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.compose.jetchat.MainViewModel
import com.example.compose.jetchat.R
import com.example.compose.jetchat.data.exampleUiState
import com.example.compose.jetchat.theme.JetchatTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ViewWindowInsetObserver
import com.google.accompanist.insets.navigationBarsPadding

class TwiDetailFragment : Fragment() {

    private val viewModel: TwiDetailViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Consider using safe args plugin

        val tid = arguments?.getString("tid")
        if (tid != null) {
            viewModel.updtid(tid)
        }
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

        val windowInsets = ViewWindowInsetObserver(this).start()

        setContent {
            val twiData by viewModel.twidata.observeAsState()


            CompositionLocalProvider(LocalWindowInsets provides windowInsets) {
                JetchatTheme {
                    if (twiData == null) {
                        ProfileError()
                    } else {
                        LaunchedEffect(Unit) {
                            viewModel.upd()
                        }
                        TwiDetailScreen(
                            twidata = twiData!!,
                            navigateToProfile = { user ->
                                // Click callback
                                val bundle = bundleOf("userId" to user)
                                findNavController().navigate(
                                    R.id.nav_profile,
                                    bundle
                                )
                            },
                            onNavIconPressed = {
                                activityViewModel.openDrawer()
                            },
                            commentsState = viewModel.comments,
                            modifier = Modifier.navigationBarsPadding(bottom = false),
                            onCommentClick = { tid ->
                                val bundle = bundleOf("tid" to tid)
                                findNavController().navigate(
                                    R.id.nav_detail,
                                    bundle
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
