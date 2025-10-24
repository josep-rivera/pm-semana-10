package com.tecsup.pm_semana_10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pm_semana_10.data.model.User
import com.tecsup.pm_semana_10.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles de la UI
 * Sealed class asegura que solo existen estos 3 estados
 */
sealed class UserUiState {
    object Loading : UserUiState()                          // Cargando datos
    data class Success(val users: List<User>) : UserUiState() // Datos cargados exitosamente
    data class Error(val message: String) : UserUiState()     // Error al cargar
}

/**
 * ViewModel que maneja la lógica de negocio y el estado de la UI
 * Sobrevive a cambios de configuración (rotación de pantalla)
 */
class UserViewModel : ViewModel() {

    // Repositorio para obtener los datos
    private val repository = UserRepository()

    // StateFlow privado y mutable (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)

    // StateFlow público e inmutable (la UI solo puede observarlo)
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        // Cargar usuarios automáticamente al crear el ViewModel
        loadUsers()
    }

    /**
     * Función pública para cargar o recargar usuarios
     * Maneja los diferentes estados: Loading, Success, Error
     */
    fun loadUsers() {
        // viewModelScope se cancela automáticamente cuando el ViewModel se destruye
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            try {
                // Intentar obtener usuarios del repositorio
                val users = repository.getUsers()
                _uiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                // Capturar cualquier error (red, parseo, etc.)
                _uiState.value = UserUiState.Error(
                    e.message ?: "Error desconocido al cargar usuarios"
                )
            }
        }
    }
}