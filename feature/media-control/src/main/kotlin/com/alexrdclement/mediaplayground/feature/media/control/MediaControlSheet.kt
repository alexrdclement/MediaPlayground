package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.model.audio.MediaItem
import com.alexrdclement.mediaplayground.media.model.audio.largeImageUrl
import com.alexrdclement.mediaplayground.media.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.constants.MediaControlSheetPartialExpandHeight
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.media.MediaControlSheetState
import com.alexrdclement.palette.components.media.model.Artist
import com.alexrdclement.palette.components.util.calculateHorizontalPaddingValues
import com.alexrdclement.palette.components.util.copy
import com.alexrdclement.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration
import com.alexrdclement.mediaplayground.ui.R as UiR
import com.alexrdclement.palette.components.media.MediaControlSheet as MediaControlSheetComponent
import com.alexrdclement.palette.components.media.model.MediaItem as UiMediaItem

@Composable
fun MediaControlSheet(
    mediaControlSheetState: MediaControlSheetState,
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToPlaybackControl: () -> Unit = {},
) {
    val viewModel = metroViewModel<MediaControlSheetViewModel>()
    val loadedMediaItem by viewModel.loadedMediaItem.collectAsStateWithLifecycle()
    val playlist by viewModel.playlist.collectAsStateWithLifecycle()
    val transportState by viewModel.transportState.collectAsStateWithLifecycle()
    val playheadState by viewModel.playheadState.collectAsStateWithLifecycle()
    val timelineState by viewModel.timelineState.collectAsStateWithLifecycle()
    val playbackRateState by viewModel.playbackRateState.collectAsStateWithLifecycle()

    MediaControlSheet(
        mediaControlSheetState = mediaControlSheetState,
        loadedMediaItem = loadedMediaItem,
        playlist = playlist,
        transportState = transportState,
        playheadState = playheadState,
        timelineState = timelineState,
        playbackRateState = playbackRateState,
        onPlayPauseClick = viewModel::onPlayPauseClick,
        onPlayPauseLongClick = onNavigateToPlaybackControl,
        onSkipClick = viewModel::onSkipClick,
        onSkipBackClick = viewModel::onSkipBackClick,
        onSeek = viewModel::onSeek,
        onItemClick = viewModel::onItemClick,
        onItemPlayPauseClick = viewModel::onItemPlayPauseClick,
        onNavigateToTrackMetadata = onNavigateToTrackMetadata,
        onNavigateToTrackDelete = onNavigateToTrackDelete,
        onNavigateToArtistMetadata = onNavigateToArtistMetadata,
        onNavigateToArtistDelete = onNavigateToArtistDelete,
    )
}

@Composable
fun MediaControlSheet(
    mediaControlSheetState: MediaControlSheetState,
    loadedMediaItem: MediaItem?,
    playlist: PersistentList<MediaItemUi>,
    transportState: TransportState,
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    playbackRateState: PlaybackRateState? = null,
    onPlayPauseClick: () -> Unit,
    onPlayPauseLongClick: (() -> Unit)? = null,
    onSkipClick: () -> Unit,
    onSkipBackClick: () -> Unit,
    onSeek: (Duration) -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToArtistMetadata: (artistId: String) -> Unit = {},
    onNavigateToArtistDelete: (artistId: String, displayName: String) -> Unit = { _, _ -> },
) {
    val coroutineScope = rememberCoroutineScope()
    val isPlaying = transportState == TransportState.Playing

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
                            transportState = transportState,
                            playheadState = playheadState,
                            timelineState = timelineState,
                            playbackRateState = playbackRateState,
                            onPlayPauseClick = onPlayPauseClick,
                            onPlayPauseLongClick = onPlayPauseLongClick,
                            onSkipClick = onSkipClick,
                            onSkipBackClick = onSkipBackClick,
                            onSeek = onSeek,
                            onItemClick = onItemClick,
                            onItemPlayPauseClick = onItemPlayPauseClick,
                            onNavigateToTrackMetadata = onNavigateToTrackMetadata,
                            onNavigateToTrackDelete = onNavigateToTrackDelete,
                            onNavigateToLoadedItemMetadata = { onNavigateToTrackMetadata(mediaItem.id.value) },
                            onNavigateToLoadedItemDelete = { onNavigateToTrackDelete(mediaItem.id.value, mediaItem.title) },
                            onNavigateToArtistMetadata = { mediaItem.artists.firstOrNull()?.let { onNavigateToArtistMetadata(it.id) } },
                            onNavigateToArtistDelete = { mediaItem.artists.firstOrNull()?.let { onNavigateToArtistDelete(it.id, it.name ?: "") } },
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
