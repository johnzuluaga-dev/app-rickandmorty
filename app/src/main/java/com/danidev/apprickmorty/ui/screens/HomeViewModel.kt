package com.danidev.apprickmorty.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.data.repository.CharacterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val characters: List<RickCharacter>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel @JvmOverloads constructor(
    private val repository: CharacterRepository = CharacterRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Estado para la URL de la foto de perfil en el Home
    private val _userPhotoUrl = MutableStateFlow<String?>(null)
    val userPhotoUrl: StateFlow<String?> = _userPhotoUrl.asStateFlow()

    init {
        loadCharacters()
        loadUserProfilePhoto()
    }

    /**
     * Consulta el documento del usuario en Firestore y emite la URL de la foto.
     */
    fun loadUserProfilePhoto() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || !snapshot.exists()) {
                    return@addSnapshotListener
                }
                val photoUrl = snapshot.getString("photoUrl")
                _userPhotoUrl.value = photoUrl
            }
    }

    /**
     * Carga la lista de personajes. Limpia el query para que si viene vacío o con espacios
     * envíe null a la API y devuelva la lista completa de personajes.
     */
    fun loadCharacters(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            // Si el query está vacío o solo contiene espacios, enviamos null
            val cleanQuery = if (query.isNullOrBlank()) null else query.trim()

            repository.getCharacters(cleanQuery)
                .onSuccess { characters ->
                    _uiState.value = HomeUiState.Success(characters)
                }
                .onFailure { throwable ->
                    _uiState.value = HomeUiState.Error(throwable.message ?: "Error al cargar los personajes")
                }
        }
    }
}