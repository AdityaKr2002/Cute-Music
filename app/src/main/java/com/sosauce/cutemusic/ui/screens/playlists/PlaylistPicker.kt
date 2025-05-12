@file:OptIn(ExperimentalMaterial3Api::class)

package com.sosauce.cutemusic.ui.screens.playlists

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import com.sosauce.cutemusic.R
import com.sosauce.cutemusic.data.actions.PlaylistActions
import com.sosauce.cutemusic.domain.model.Playlist
import com.sosauce.cutemusic.ui.shared_components.CuteText
import com.sosauce.cutemusic.ui.shared_components.PlaylistViewModel
import com.sosauce.cutemusic.utils.ICON_TEXT_SPACING
import com.sosauce.cutemusic.utils.copyMutate
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistPicker(
    mediaId: List<String>,
    onDismissRequest: () -> Unit,
    onAddingFinished: () -> Unit = {}
) {
    val context = LocalContext.current
    val playlistViewModel = koinViewModel<PlaylistViewModel>()
    val playlists by playlistViewModel.allPlaylists.collectAsStateWithLifecycle()
    var showPlaylistCreatorDialog by remember { mutableStateOf(false) }



    if (showPlaylistCreatorDialog) {
        CreatePlaylistDialog { showPlaylistCreatorDialog = false }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn {
            item {
                OutlinedButton(
                    onClick = { showPlaylistCreatorDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(ICON_TEXT_SPACING.dp))
                        CuteText(stringResource(R.string.create_playlist))
                    }
                }
            }

            items(
                items = playlists,
                key = { it.id }
            ) { playlist ->
                PlaylistItem(
                    playlist = playlist,
                    allowEditAction = false,
                    onClickPlaylist = {

                        val newPlaylist = Playlist(
                            id = playlist.id,
                            name = playlist.name,
                            emoji = playlist.emoji,
                            musics = playlist.musics.copyMutate {
                                mediaId.forEach { id ->
                                    if (!contains(id)) {
                                        add(id)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.alrdy_in_playlist),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                        playlistViewModel.handlePlaylistActions(
                            PlaylistActions.UpsertPlaylist(newPlaylist)
                        )
                        onAddingFinished()
                    },
                    onHandlePlaylistActions = playlistViewModel::handlePlaylistActions
                )
            }
        }
    }
}