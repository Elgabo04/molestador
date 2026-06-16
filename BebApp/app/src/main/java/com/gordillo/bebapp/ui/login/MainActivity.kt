package com.gordillo.bebapp.ui.login

// Libreria de Android para navegar entre pantallas.
import android.content.Intent
// Libreria base para activities compatibles con Material/AppCompat.
import androidx.appcompat.app.AppCompatActivity
// Libreria de Android para recibir el ciclo de vida inicial.
import android.os.Bundle
// Libreria que contiene la referencia a recursos generados.
import com.gordillo.bebapp.R
// Pantalla principal que se abre despues de iniciar sesion.
import com.gordillo.bebapp.ui.principal.DashboardActivity
// Componente Material usado para el boton de Google.
import com.google.android.material.button.MaterialButton

// Activity inicial donde luego se conectara Google Sign-In.
class MainActivity : AppCompatActivity() {

    // Boton que inicia el flujo de autenticacion con Google.
    private lateinit var btnAccederGoogle: MaterialButton

    // Inicializa la pantalla de acceso.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializarComponentes()
        inicializarEventos()
    }

    // Vincula las vistas XML con variables de Kotlin.
    private fun inicializarComponentes() {
        btnAccederGoogle = findViewById(R.id.btnAccederGoogle)
    }

    // Registra eventos de usuario para esta pantalla.
    private fun inicializarEventos() {
        btnAccederGoogle.setOnClickListener {
            procesarAccesoGoogle()
        }
    }

    // Punto de inyeccion para Google Sign-In; por ahora avanza al dashboard.
    private fun procesarAccesoGoogle() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
