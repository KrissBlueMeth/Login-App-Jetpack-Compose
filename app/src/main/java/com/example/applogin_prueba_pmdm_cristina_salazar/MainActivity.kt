package com.example.applogin_prueba_pmdm_cristina_salazar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

//Esto es para el color personalizado moradito del botón
private val CustomPurple = Color(0xFF673AB7)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AppNavigation()
            }
        }
    }
}

//Función principal de navegación
@Composable
fun AppNavigation() {
    val navegar = rememberNavController()
    // Rutas "login" y "bienvenido".
    NavHost(
        navController = navegar,
        startDestination = "login"
    ) {
        //Pantalla de lgin
        composable("login") { PantallaLogin(navegar) }

        //Pantalla de bienvenida (recibe el argumento "user" y el NavController)
        composable(
            route = "bienvenido/{usuario}",
            arguments = listOf(navArgument("usuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val usuario = backStackEntry.arguments?.getString("usuario") ?: "USUARIO DESCONOCIDO"
            // Se pasa el NavController (navegar) a la PantallaBienvenida
            PantallaBienvenida(usuario = usuario, navegar = navegar)
        }
    }
}

/**
 * A) Pantalla de petición de datos (Login)
 * Captura usuario y contraseña. Navega a 'bienvenido' pasando el usuario.
 */
@Composable
fun PantallaLogin(navegar: NavHostController) {
    var usuarioText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Esto es para el logo del gatito-yoga
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la aplicación", // Descripción para accesibilidad
            modifier = Modifier.size(100.dp) // Tamaño fijo para el logo
        )
        Spacer(Modifier.height(16.dp))


        Text(
            text = "INICIO DE SESIÓN",
            style = MaterialTheme.typography.headlineLarge,
            color = CustomPurple // Color Morado
        )
        Spacer(Modifier.height(32.dp))

        //Campo "Usuario o Email" (OutlinedTextField)
        OutlinedTextField(
            value = usuarioText,
            onValueChange = { usuarioText = it },
            label = { Text("USUARIO O EMAIL") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CustomPurple,
                focusedLabelColor = CustomPurple,
                cursorColor = CustomPurple
            )
        )
        Spacer(Modifier.height(16.dp))

        //Campo "Contraseña" (OutlinedTextField con PasswordVisualTransformation)
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("CONTRASEÑA") },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CustomPurple,
                focusedLabelColor = CustomPurple,
                cursorColor = CustomPurple
            )
        )
        Spacer(Modifier.height(32.dp))

        //Botón "Login"
        Button(
            onClick = {
                // Navegación a "bienvenido" pasando el usuario.
                val safeUsuario = java.net.URLEncoder.encode(usuarioText, "utf-8")
                navegar.navigate("bienvenido/$safeUsuario")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomPurple,
                contentColor = Color.White
            ),
            enabled = usuarioText.isNotBlank() && passwordText.isNotBlank()
        ) {
            Text("INICIAR SESIÓN")
        }
    }
}

/**
 * B) Pantalla de resultados (Bienvenida)
 * Muestra un mensaje personalizado con el usuario y un botón para cerrar sesión.
 *
 * @param usuario El nombre de usuario que ha iniciado sesión.
 * @param navegar El NavHostController para gestionar la navegación.
 */
@Composable
fun PantallaBienvenida(usuario: String, navegar: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Título de bienvenida en moradoo
        Text(
            text = "¡BIENVENIDO/A,",
            style = MaterialTheme.typography.headlineLarge,
            color = CustomPurple
        )
        Spacer(Modifier.height(8.dp))
        //Usuario en moradito y mayusculas
        Text(
            text = usuario.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = CustomPurple
        )
        Spacer(Modifier.height(32.dp))
        //Texto general usa el color por defecto
        Text(
            text = "ACCESO REALIZADO CON ÉXITO",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(32.dp)) // Espaciador antes del nuevo botón

        //botón para cerrar sesión/volver al login
        Button(
            onClick = {
                navegar.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("CERRAR SESIÓN")
        }
    }
}