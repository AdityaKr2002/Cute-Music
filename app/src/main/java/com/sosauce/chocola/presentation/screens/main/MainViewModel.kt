@file:OptIn(FlowPreview::class)

package com.sosauce.chocola.presentation.screens.main

import android.app.Application
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosauce.chocola.data.AbstractTracksScanner
import com.sosauce.chocola.data.datastore.UserPreferences
import com.sosauce.chocola.data.models.CuteTrack
import com.sosauce.chocola.utils.TrackSort
import com.sosauce.chocola.utils.copyMutate
import com.sosauce.chocola.utils.ordered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MainViewModel(
    private val application: Application,
    private val abstractTracksScanner: AbstractTracksScanner,
    private val userPreferences: UserPreferences
) : AndroidViewModel(application) {

    val textFieldState = TextFieldState()
    private val userQuery = snapshotFlow { textFieldState.text }.debounce(250)

    private val _state = MutableStateFlow(MainState(isLoading = true, textFieldState = textFieldState))
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            combine(
                abstractTracksScanner.fetchLatestTracks(null, null),
                userQuery,
                userPreferences.getTrackSort,
                userPreferences.sortTracksAscending
            ) { tracks, query, trackSort, ascending ->
                tracks.ordered(trackSort, ascending, query) to query.isNotEmpty()
            }
                .flowOn(Dispatchers.Default)
                .collectLatest { (tracks, isSearching) ->
                    _state.update {
                        it.copy(
                            tracks = tracks,
                            isLoading = false,
                            isSearching = isSearching
                        )
                    }
                }
        }
    }


}


data class MainState(
    val isLoading: Boolean = false,
    val tracks: List<CuteTrack> = emptyList(),
    val isSearching: Boolean = false,
    val textFieldState: TextFieldState = TextFieldState()
)