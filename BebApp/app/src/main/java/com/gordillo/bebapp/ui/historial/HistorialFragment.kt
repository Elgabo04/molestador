package com.gordillo.bebapp.ui.historial

// Libreria base para inflar vistas XML.
import android.view.LayoutInflater
// Libreria base para representar vistas.
import android.view.View
// Contenedor base para fragments.
import android.view.ViewGroup
// Libreria para recibir estado inicial.
import android.os.Bundle
// Contenedor lineal usado para lista dinamica.
import android.widget.LinearLayout
// Vista de texto nativa.
import android.widget.TextView
// Utilidad para aplicar colores desde recursos.
import androidx.core.content.ContextCompat
// Fragment base de AndroidX.
import androidx.fragment.app.Fragment
// Referencias a recursos generados.
import com.gordillo.bebapp.R
// Utilidad local de fechas.
import com.gordillo.bebapp.datos.FormateadorFecha
// Repositorio temporal de tareas e historial.
import com.gordillo.bebapp.datos.RepositorioLocalTemporal
// Enum real de estado de tarea.
import com.gordillo.bebapp.modelos.EstadoTarea
// Modelo principal de tarea.
import com.gordillo.bebapp.modelos.TareaAcademica

// Fragment que muestra el historial y resumen de reprogramaciones.
class HistorialFragment : Fragment() {

    // Banner azul que muestra cuantas tareas se reprogramaron.
    private lateinit var txtBannerReprogramadas: TextView

    // Lista vertical donde se agregan cards de historial.
    private lateinit var listaHistorial: LinearLayout

    // Crea la vista del fragmento de historial.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    // Inicializa componentes y pinta datos al crear la vista.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarComponentes(view)
        procesarDatosHistorial()
    }

    // Vincula las vistas XML del historial.
    private fun inicializarComponentes(view: View) {
        txtBannerReprogramadas = view.findViewById(R.id.txtBannerReprogramadas)
        listaHistorial = view.findViewById(R.id.listaHistorial)
    }

    // Pinta el banner y las cards de historial.
    private fun procesarDatosHistorial() {
        listaHistorial.removeAllViews()
        val totalReprogramadas = RepositorioLocalTemporal.contarTareasReprogramadas()
        txtBannerReprogramadas.text = "Reprogramaste $totalReprogramadas tareas"
        RepositorioLocalTemporal.obtenerHistorial().forEach { tarea ->
            listaHistorial.addView(crearVistaHistorial(tarea))
        }
    }

    // Crea una card de historial con estado real y fecha anterior si aplica.
    private fun crearVistaHistorial(tarea: TareaAcademica): View {
        val vista = layoutInflater.inflate(R.layout.item_historial, listaHistorial, false)
        val txtTitulo = vista.findViewById<TextView>(R.id.txtTituloHistorial)
        val txtDescripcion = vista.findViewById<TextView>(R.id.txtDescripcionHistorial)
        val txtFecha = vista.findViewById<TextView>(R.id.txtFechaHistorial)
        val txtFechaAnterior = vista.findViewById<TextView>(R.id.txtFechaAnteriorHistorial)
        val txtEstado = vista.findViewById<TextView>(R.id.txtEstadoHistorial)

        txtTitulo.text = tarea.nombreTarea
        txtDescripcion.text = tarea.descripcionTarea
        txtFecha.text = "Vencimiento: ${FormateadorFecha.mostrar(tarea.fechaVencimiento)}"
        txtEstado.text = tarea.estado.textoVisible
        aplicarEstiloEstado(txtEstado, tarea.estado)

        tarea.fechaVencimientoAnterior?.let { fechaAnterior ->
            txtFechaAnterior.visibility = View.VISIBLE
            txtFechaAnterior.text = "Fecha anterior: ${FormateadorFecha.mostrar(fechaAnterior)}"
        }
        return vista
    }

    // Aplica el color del badge segun el estado real guardado.
    private fun aplicarEstiloEstado(badge: TextView, estado: EstadoTarea) {
        when (estado) {
            EstadoTarea.REALIZADO -> {
                badge.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_verde_pastel))
                badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_verde_oscuro))
            }
            EstadoTarea.EN_PROCESO -> {
                badge.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_amarillo_pastel))
                badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_marron))
            }
            EstadoTarea.POSTERGADO -> {
                badge.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_rojo_pastel))
                badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_rojo_oscuro))
            }
        }
    }
}
