package udg.cuvalles.scaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch // ESTE IMPORT FALTABA

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val productos = remember { mutableStateListOf<Producto>() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    productos = productos,
                    onNavigateToRegistro = { navController.navigate("registro") },
                    onEditProduct = { id -> navController.navigate("registro?id=$id") },
                    onDeleteProduct = { producto ->
                        productos.remove(producto)
                        scope.launch {
                            snackbarHostState.showSnackbar("Producto '${producto.nombre}' eliminado")
                        }
                    }
                )
            }

            composable(
                route = "registro?id={id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val productoAEditar = productos.find { it.id == id }

                RegistroScreen(
                    productoAEditar = productoAEditar,
                    onGuardarProducto = { producto ->
                        val existente = productos.indexOfFirst { it.id == producto.id }
                        val mensaje = if (existente >= 0) {
                            productos[existente] = producto
                            "Producto actualizado"
                        } else {
                            productos.add(producto)
                            "Producto guardado"
                        }
                        navController.popBackStack()
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}