package udg.cuvalles.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    productoAEditar: Producto?,
    onGuardarProducto: (Producto) -> Unit,
    onNavigateBack: () -> Unit
) {
    var id by remember { mutableStateOf(productoAEditar?.id ?: "") }
    var nombre by remember { mutableStateOf(productoAEditar?.nombre ?: "") }
    var precio by remember { mutableStateOf(productoAEditar?.precio ?: "") }
    var mostrarError by remember { mutableStateOf(false) }

    val esEdicion = productoAEditar != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (esEdicion) "Editar producto" else "Registro de producto",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text("ID", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !esEdicion,
                isError = mostrarError && id.isBlank(),
                supportingText = if (mostrarError && id.isBlank()) {
                    { Text("El ID es obligatorio") }
                } else null
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nombre", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = mostrarError && nombre.isBlank(),
                supportingText = if (mostrarError && nombre.isBlank()) {
                    { Text("El nombre es obligatorio") }
                } else null
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Precio", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = mostrarError && precio.isBlank(),
                supportingText = if (mostrarError && precio.isBlank()) {
                    { Text("El precio es obligatorio") }
                } else null
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (id.isNotBlank() && nombre.isNotBlank() && precio.isNotBlank()) {
                        onGuardarProducto(Producto(id.trim(), nombre.trim(), precio.trim()))
                    } else {
                        mostrarError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(
                    if (esEdicion) "Actualizar" else "Guardar",
                    fontWeight = FontWeight.Bold
                )
            }

            if (esEdicion) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}