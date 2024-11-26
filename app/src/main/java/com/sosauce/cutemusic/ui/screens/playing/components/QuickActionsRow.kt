package com.sosauce.cutemusic.ui.screens.playing.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sosauce.cutemusic.R
import com.sosauce.cutemusic.data.MusicState
import com.sosauce.cutemusic.ui.navigation.Screen
import com.sosauce.cutemusic.ui.shared_components.CuteText
import com.sosauce.cutemusic.ui.shared_components.MusicStateDetailsDialog

@Composable
fun QuickActionsRow(
    musicState: MusicState,
    onShowLyrics: () -> Unit,
    onShowSpeedCard: () -> Unit,
    onChargeAlbumSongs: (String) -> Unit,
    onNavigate: (Screen) -> Unit,
    onChargeArtistLists: (String) -> Unit
) {
    val context = LocalContext.current
    var isDropDownExpanded by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    val uri = remember { Uri.parse(musicState.currentMusicUri) }

    if (showDetailsDialog) {
        MusicStateDetailsDialog(
            musicState = musicState,
            onDismissRequest = { showDetailsDialog = false }
        )
    }
    val shareIntent = Intent()
        .apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "audio/*"
        }


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {

        IconButton(onClick = onShowLyrics) {
            Icon(
                painter = painterResource(R.drawable.lyrics_rounded),
                contentDescription = "show lyrics"
            )
        }

        IconButton(onClick = onShowSpeedCard) {
            Icon(
                imageVector = Icons.Rounded.Speed,
                contentDescription = "change speed"
            )
        }
        Row {
            IconButton(onClick = { isDropDownExpanded = true }) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "more"
                )
            }


            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
                shape = RoundedCornerShape(24.dp)
            ) {
                DropdownMenuItem(
                    onClick = { showDetailsDialog = true },
                    text = {
                        CuteText(stringResource(R.string.details))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.rotate(180f)
                        )
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        isDropDownExpanded = false
                        onChargeAlbumSongs(musicState.currentAlbum)
                        onNavigate(Screen.AlbumsDetails(musicState.currentAlbumId))
                    },
                    text = {
                        CuteText("Go to: ${musicState.currentAlbum}")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(androidx.media3.session.R.drawable.media3_icon_album),
                            contentDescription = null
                        )
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        isDropDownExpanded = false
                        onChargeArtistLists(musicState.currentArtist)
                        onNavigate(Screen.ArtistsDetails(musicState.currentArtistId))
                    },
                    text = {
                        CuteText("Go to: ${musicState.currentArtist}")
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.artist_rounded),
                            contentDescription = null
                        )
                    }
                )
                DropdownMenuItem(
                    onClick = { context.startActivity(Intent.createChooser(shareIntent, null)) },
                    text = {
                        CuteText(
                            text = stringResource(R.string.share)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(androidx.media3.session.R.drawable.media3_icon_share),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}