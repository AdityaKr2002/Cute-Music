@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.sosauce.cutemusic.presentation.screens.album.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage
import com.sosauce.cutemusic.R
import com.sosauce.cutemusic.data.actions.PlayerActions
import com.sosauce.cutemusic.domain.model.Album
import com.sosauce.cutemusic.presentation.shared_components.CuteText
import com.sosauce.cutemusic.utils.ImageUtils

@Composable
fun SharedTransitionScope.AlbumHeaderLandscape(
    album: Album,
    musics: List<MediaItem>,
    onHandlePlayerActions: (PlayerActions) -> Unit
) {
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageUtils.imageRequester(ImageUtils.getAlbumArt(album.id), context),
            contentDescription = stringResource(R.string.artwork),
            modifier = Modifier
                .size(200.dp)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = album.id),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                )
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(15.dp))
        Column {
            CuteText(
                text = album.name,
                style = MaterialTheme.typography.headlineMediumEmphasized,
                modifier = Modifier
                    .basicMarquee()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = album.name + album.id),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )
            )
            CuteText(
                text = album.artist,
                style = MaterialTheme.typography.bodyLargeEmphasized,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .basicMarquee()
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = album.artist + album.id),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )

            )
            Spacer(Modifier.height(15.dp))
            IconButton(
                onClick = {
                    onHandlePlayerActions(
                        PlayerActions.StartAlbumPlayback(
                            albumName = album.name,
                            mediaId = musics.first().mediaId
                        )
                    )
                },
                shapes = IconButtonDefaults.shapes(),
                colors = IconButtonDefaults.filledIconButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide))
            ) {
                Icon(
                    painter = painterResource(R.drawable.widget_play),
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(5.dp))
            IconButton(
                onClick = {
                    onHandlePlayerActions(
                        PlayerActions.StartAlbumPlayback(
                            albumName = album.name,
                            mediaId = null
                        )
                    )
                },
                shapes = IconButtonDefaults.shapes(),
                colors = IconButtonDefaults.filledIconButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(IconButtonDefaults.mediumContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Shuffle,
                    contentDescription = null
                )
            }
        }
    }
}