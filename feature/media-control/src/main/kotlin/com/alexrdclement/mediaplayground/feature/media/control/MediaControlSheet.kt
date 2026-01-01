package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.constants.MediaControlSheetPartialExpandHeight
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.palette.components.util.calculateHorizontalPaddingValues
import com.alexrdclement.palette.components.util.minus
import com.alexrdclement.palette.components.util.plus
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.media.MediaControlSheetState
import com.alexrdclement.palette.components.media.model.Artist
import com.alexrdclement.palette.components.util.calculateVerticalPadding
import com.alexrdclement.palette.components.util.calculateVerticalPaddingValues
import com.alexrdclement.palette.components.util.copy
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.alexrdclement.mediaplayground.ui.R as UiR
import com.alexrdclement.palette.components.media.MediaControlSheet as MediaControlSheetComponent
import com.alexrdclement.palette.components.media.model.MediaItem as UiMediaItem

@Composable
fun MediaControlSheet(
    mediaControlSheetState: MediaControlSheetState
) {
    val viewModel = hiltViewModel<MediaControlSheetViewModel>()
    val loadedMediaItem by viewModel.loadedMediaItem.collectAsStateWithLifecycle()
    val playlist by viewModel.playlist.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    MediaControlSheet(
        mediaControlSheetState = mediaControlSheetState,
        loadedMediaItem = loadedMediaItem,
        playlist = playlist,
        isPlaying = isPlaying,
        onPlayPauseClick = viewModel::onPlayPauseClick,
        onItemClick = viewModel::onItemClick,
        onItemPlayPauseClick = viewModel::onItemPlayPauseClick,
    )
}

@Composable
fun MediaControlSheet(
    mediaControlSheetState: MediaControlSheetState,
    loadedMediaItem: MediaItem?,
    playlist: PersistentList<MediaItemUi>,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = loadedMediaItem != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        loadedMediaItem?.let { mediaItem ->
            val fallbackArtistName = stringResource(id = UiR.string.artist_name_fallback)
            val artists by derivedStateOf {
                mediaItem.artists.map {
                    Artist(
                        name = it.name ?: fallbackArtistName
                    )
                }
            }
            val uiMediaItem by derivedStateOf {
                UiMediaItem(
                    artworkLargeUrl = mediaItem.largeImageUrl,
                    artworkThumbnailUrl = mediaItem.thumbnailImageUrl,
                    title = mediaItem.title,
                    artists = artists,
                )
            }

            val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()
            val navBarBottomPadding = navigationBarsPadding.calculateBottomPadding()
            val yOffset by derivedStateOf {
                navBarBottomPadding * (1f - mediaControlSheetState.partialToFullProgress)
            }

            BoxWithConstraints {
                val maxHeight = this@BoxWithConstraints.maxHeight
                val maxWidth = this@BoxWithConstraints.maxWidth

                val contentPadding = WindowInsets.safeDrawing.asPaddingValues()
                MediaControlSheetComponent(
                    mediaItem = uiMediaItem,
                    isPlaying = isPlaying,
                    onPlayPauseClick = onPlayPauseClick,
                    state = mediaControlSheetState,
                    contentPadding = contentPadding.calculateHorizontalPaddingValues(),
                    onControlBarClick = {
                        coroutineScope.launch {
                            if (mediaControlSheetState.isExpanded) {
                                mediaControlSheetState.partialExpand()
                            } else {
                                mediaControlSheetState.expand()
                            }
                        }
                    },
                    minContentSize = DpSize(
                        width = MediaControlSheetPartialExpandHeight,
                        height = MediaControlSheetPartialExpandHeight,
                    ),
                    maxContentSize = when {
                        maxHeight < maxWidth -> DpSize(
                            width = maxWidth,
                            height = maxHeight / 2f,
                        )
                        else -> DpSize(
                            width = maxWidth,
                            height = maxWidth, // Square for now
                        )
                    },
                    aboveControlBar = {
                        val statusBarsPadding = WindowInsets.statusBars.asPaddingValues()
                        val statusBarTopPadding = statusBarsPadding.calculateTopPadding()
                        Box(
                            modifier = Modifier
                                .background(PaletteTheme.colorScheme.surface)
                                .fillMaxWidth()
                                .layout { measureables, constraints ->
                                    val placeables = measureables.measure(constraints)
                                    val progress = mediaControlSheetState.partialToFullProgress
                                    val topPadding = statusBarTopPadding * progress
                                    layout(constraints.maxWidth, topPadding.toPx().roundToInt()) {
                                        placeables.place(0, 0)
                                    }
                                }
                        )
                    },
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = -yOffset.toPx()
                        }
                ) {
                    Surface(
                        modifier = Modifier.graphicsLayer {
                            alpha = mediaControlSheetState.partialToFullProgress
                        }
                    ) {
                        MediaControlSheetContent(
                            loadedMediaItem = uiMediaItem,
                            playlist = playlist,
                            isPlaying = isPlaying,
                            onPlayPauseClick = onPlayPauseClick,
                            onItemClick = onItemClick,
                            onItemPlayPauseClick = onItemPlayPauseClick,
                            contentPadding = contentPadding.copy(
                                top = PaletteTheme.spacing.medium,
                            )
                        )
                    }
                }
            }
        }
    }
}
