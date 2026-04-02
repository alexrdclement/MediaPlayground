package com.alexrdclement.mediaplayground.feature.artist.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.artist.ArtistRepository
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class DeleteState {
    data object Confirming : DeleteState()
    data object Deleting : DeleteState()
    data object Deleted : DeleteState()
}

@AssistedInject
class ArtistDeleteViewModel(
    @Assisted val idValue: String,
    private val artistRepository: ArtistRepository,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(idValue: String): ArtistDeleteViewModel
    }

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Confirming)
    val deleteState: StateFlow<DeleteState> = _deleteState.asStateFlow()

    fun onDeleteConfirmed() {
        viewModelScope.launch {
            _deleteState.update { DeleteState.Deleting }
            artistRepository.deleteArtist(idValue)
            _deleteState.update { DeleteState.Deleted }
        }
    }
}
