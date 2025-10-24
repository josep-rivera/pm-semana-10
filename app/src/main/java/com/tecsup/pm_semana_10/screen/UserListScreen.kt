package com.tecsup.pm_semana_10.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.pm_semana_10.data.model.User
import com.tecsup.pm_semana_10.viewmodel.UserUiState
import com.tecsup.pm_semana_10.viewmodel.UserViewModel

/**
 * Pantalla principal que muestra la lista de usuarios
 * @param viewModel ViewModel que maneja el estado y lógica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserViewModel = viewModel()
) {
    // Observar el estado del ViewModel
    // collectAsState() convierte el StateFlow en State de Compose
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Usuarios - Tecsup ") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Renderizar contenido según el estado actual
            when (uiState) {
                is UserUiState.Loading -> {
                    // Estado: Cargando
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UserUiState.Success -> {
                    // Estado: Éxito - mostrar lista
                    val users = (uiState as UserUiState.Success).users
                    UserList(users = users)
                }

                is UserUiState.Error -> {
                    // Estado: Error - mostrar mensaje y botón reintentar
                    val message = (uiState as UserUiState.Error).message
                    ErrorMessage(
                        message = message,
                        onRetry = { viewModel.loadUsers() }
                    )
                }
            }
        }
    }
}

/**
 * Componente que muestra la lista de usuarios en un LazyColumn
 * @param users Lista de usuarios a mostrar
 */
@Composable
fun UserList(users: List<User>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(user = user)
        }
    }
}

/**
 * Card que muestra la información de un usuario
 * @param user Usuario a mostrar
 */
@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nombre del usuario
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Email del usuario
            Text(
                text = user.email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Teléfono del usuario
            Text(
                text = user.phone,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Componente que muestra un mensaje de error con opción de reintentar
 * @param message Mensaje de error a mostrar
 * @param onRetry Callback que se ejecuta al presionar el botón Reintentar
 */
@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "❌ Error",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}