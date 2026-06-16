package com.gordillo.bebapp.ui.principal

// Libreria base para cerrar o navegar activities.
import android.content.Intent
// Libreria base para activities compatibles.
import androidx.appcompat.app.AppCompatActivity
// Libreria usada para recibir estado de creacion.
import android.os.Bundle
// Libreria base para trabajar con fragments.
import androidx.fragment.app.Fragment
// Referencia a recursos generados.
import com.gordillo.bebapp.R
// Repositorio temporal con el usuario actual.
import com.gordillo.bebapp.datos.RepositorioLocalTemporal
// Pantalla de acceso para volver al cerrar sesion.
import com.gordillo.bebapp.ui.login.MainActivity
// Fragmento de historial.
import com.gordillo.bebapp.ui.historial.HistorialFragment
// Fragmento de tareas.
import com.gordillo.bebapp.ui.tareas.TareasFragment
// Navegacion inferior de Material.
import com.google.android.material.bottomnavigation.BottomNavigationView
// Toolbar Material usada como barra superior.
import com.google.android.material.appbar.MaterialToolbar
// Vista de texto nativa para nombre de usuario.
import android.widget.TextView
// Boton de imagen nativo para logout.
import android.widget.ImageButton

// Activity contenedora del dashboard de BebApp.
class DashboardActivity : AppCompatActivity() {

    // Barra superior con identidad de la app y usuario.
    private lateinit var barraSuperior: MaterialToolbar

    // Texto donde se muestra el nombre del usuario autenticado.
    private lateinit var txtNombreUsuario: TextView

    // Boton que cierra la sesion actual.
    private lateinit var btnCerrarSesion: ImageButton

    // Navegacion inferior entre tareas e historial.
    private lateinit var navegacionInferior: BottomNavigationView

    // Inicializa el dashboard y carga la seccion de tareas.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        inicializarComponentes()
        inicializarEventos()
        procesarDatosIniciales()
    }

    // Vincula vistas principales del dashboard.
    private fun inicializarComponentes() {
        barraSuperior = findViewById(R.id.barraSuperior)
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        navegacionInferior = findViewById(R.id.navegacionInferior)
    }

    // Declara eventos de navegacion y cierre de sesion.
    private fun inicializarEventos() {
        btnCerrarSesion.setOnClickListener {
            procesarCierreSesion()
        }
        navegacionInferior.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_tareas -> {
                    mostrarFragmento(TareasFragment())
                    true
                }
                R.id.menu_historial -> {
                    mostrarFragmento(HistorialFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Carga informacion inicial del usuario y la primera seccion.
    private fun procesarDatosIniciales() {
        setSupportActionBar(barraSuperior)
        txtNombreUsuario.text = RepositorioLocalTemporal.nombreUsuario
        navegacionInferior.selectedItemId = R.id.menu_tareas
    }

    // Reemplaza el contenido central por el fragmento elegido.
    private fun mostrarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorPrincipal, fragmento)
            .commit()
    }

    // Punto de inyeccion para cerrar sesion con Google y limpiar tokens locales.
    private fun procesarCierreSesion() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
