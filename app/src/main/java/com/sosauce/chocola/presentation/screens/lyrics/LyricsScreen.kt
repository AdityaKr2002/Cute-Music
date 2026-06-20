@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.chocola.presentation.screens.lyrics

import android.app.SearchManager
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.vibrantFloatingToolbarColors
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sosauce.chocola.R
import com.sosauce.chocola.data.datastore.rememberLyricsAlignment
import com.sosauce.chocola.data.datastore.rememberLyricsFontSize
import com.sosauce.chocola.data.states.MusicState
import com.sosauce.chocola.domain.actions.PlayerActions
import com.sosauce.chocola.domain.model.Lyrics
import com.sosauce.chocola.presentation.navigation.Screen
import com.sosauce.chocola.presentation.screens.playing.components.PlayPauseButton
import com.sosauce.chocola.presentation.shared_components.animations.AnimatedFab
import com.sosauce.chocola.presentation.shared_components.animations.AnimatedIconButton
import com.sosauce.chocola.utils.ICON_TEXT_SPACING
import com.sosauce.chocola.utils.rememberInteractionSource
import com.sosauce.chocola.utils.selfAlignHorizontally
import com.sosauce.chocola.utils.toLyricsAlignment
import kotlinx.coroutines.launch

@Composable
fun LyricsScreen(
    onNavigateBack: () -> Unit,
    onNavigate: (Screen) -> Unit,
    musicState: MusicState,
    onHandlePlayerActions: (PlayerActions) -> Unit
) {
    Scaffold(
        bottomBar = {
            HorizontalFloatingToolbar(
                expanded = true,
                colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
                modifier = Modifier
                    .selfAlignHorizontally()
                    .navigationBarsPadding(),
                floatingActionButton = {
                    AnimatedFab(
                        onClick = onNavigateBack,
                        icon = R.drawable.close,
                        containerColor = vibrantFloatingToolbarColors().fabContainerColor
                    )
                }
            ) {
                AnimatedIconButton(
                    onClick = { onHandlePlayerActions(PlayerActions.SeekToPreviousMusic) },
                    icon = R.drawable.skip_previous,
                    contentDescription = stringResource(androidx.media3.session.R.string.media3_controls_seek_back_description)
                )
                PlayPauseButton(
                    isPlaying = musicState.isPlaying,
                    onHandlePlayerActions = onHandlePlayerActions
                )
                AnimatedIconButton(
                    onClick = { onHandlePlayerActions(PlayerActions.SeekToNextMusic) },
                    icon = R.drawable.skip_next,
                    contentDescription = stringResource(androidx.media3.session.R.string.media3_controls_seek_to_next_description)
                )
            }
        }
    ) { paddingValues ->
        LyricsList(
            contentPadding = paddingValues,
            musicState = musicState,
            onHandlePlayerActions = onHandlePlayerActions,
            emptyLyrics = {
                DefaultEmptyLyricsScreen(
                    musicState = musicState,
                    onNavigate = onNavigate,
                    onHandlePlayerActions = onHandlePlayerActions
                )
            }
        )
    }

}

