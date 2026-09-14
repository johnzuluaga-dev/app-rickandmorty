package com.danidev.apprickmorty.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CharacterUiState {
    data object Loading : CharacterUiState
    data class Success(val characters: List<RickCharacter>) : CharacterUiState
    data class Error(val message: String) : CharacterUiState
}

class CharacterViewModel @JvmOverloads constructor(
    private val repository: CharacterRepository = CharacterRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Loading)
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadCharacters()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        loadCharacters(query.ifBlank { null })
    }

    private fun loadCharacters(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = CharacterUiState.Loading
            repository.getCharacters(query)
                .onSuccess { _uiState.value = CharacterUiState.Success(it) }
                .onFailure { _uiState.value = CharacterUiState.Error(it.message ?: "Error desconocido") }
        }
    }
}
