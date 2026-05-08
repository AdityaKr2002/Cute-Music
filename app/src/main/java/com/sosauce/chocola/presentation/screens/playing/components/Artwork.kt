@file:OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class
)

package com.sosauce.chocola.presentation.screens.playing.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.rectangle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.sosauce.chocola.R
import com.sosauce.chocola.data.datastore.rememberArtworkShape
import com.sosauce.chocola.data.datastore.rememberCarousel
import com.sosauce.chocola.data.states.MusicState
import com.sosauce.chocola.domain.actions.PlayerActions
import com.sosauce.chocola.presentation.shared_components.animations.MorphPolygonShape
import com.sosauce.chocola.utils.ArtworkShape
import com.sosauce.chocola.utils.bouncySpec
import com.sosauce.chocola.utils.ignoreParentPadding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.math.absoluteValue

@Composable
fun Artwork(
    pagerModifier: Modifier = Modifier,
    musicState: MusicState,
    onHandlePlayerActions: (PlayerActions) -> Unit,
) {
    val useCarousel by rememberCarousel()
    var artworkShape by rememberArtworkShape()

    if (useCarousel) {
        val carouselState =
            rememberCarouselState(initialItem = musicState.mediaIndex) { musicState.loadedMedias.size }

        var isProgrammaticScroll by remember { mutableStateOf(false) }

        // Sync carousel position when playback changes track externally
        LaunchedEffect(musicState.mediaIndex) {
            if (!carouselState.isScrollInProgress &&
                carouselState.currentItem != musicState.mediaIndex
            ) {
                isProgrammaticScroll = true
                carouselState.animateScrollToItem(musicState.mediaIndex)
                isProgrammaticScroll = false
            }
        }

        // Use rememberUpdatedState to always read latest values inside the long-lived LaunchedEffect
        val currentMediaIndex by rememberUpdatedState(musicState.mediaIndex)
        val currentShuffle by rememberUpdatedState(musicState.shuffle)
        val currentTrackCount by rememberUpdatedState(musicState.loadedMedias.size)

        // Dispatch track change when user finishes swiping
        LaunchedEffect(carouselState) {
            snapshotFlow { carouselState.isScrollInProgress }
                .filter { !it }
                .map { carouselState.currentItem }
                .distinctUntilChanged()
                .collectLatest { settledItem ->
                    if (currentTrackCount == 0) return@collectLatest
                    val safeIndex = settledItem.coerceIn(0, currentTrackCount - 1)
                    if (isProgrammaticScroll) return@collectLatest
                    if (safeIndex != currentMediaIndex) {
                        if (currentShuffle) {
                            if (safeIndex > currentMediaIndex) {
                                onHandlePlayerActions(PlayerActions.SeekToNextMusic)
                            } else {
                                onHandlePlayerActions(PlayerActions.SeekToPreviousMusic)
                            }
                        } else {
                            onHandlePlayerActions(PlayerActions.SeekToMusicIndex(safeIndex))
                        }
                    }
                }
        }

        HorizontalCenteredHeroCarousel(
            state = carouselState,
            itemSpacing = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.music_note_rounded),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.4f),
                    tint = contentColorFor(MaterialTheme.colorScheme.surfaceContainer)

                )
                AsyncImage(
                    model = musicState.loadedMedias[page].artUri,
                    contentDescription = stringResource(R.string.artwork),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    } else {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = ArtworkShape.toShape(artworkShape)
                )
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.music_note_rounded),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.4f),
                tint = contentColorFor(MaterialTheme.colorScheme.surfaceContainer)

            )
            AsyncImage(
                model = musicState.track.artUri,
                contentDescription = stringResource(R.string.artwork),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


