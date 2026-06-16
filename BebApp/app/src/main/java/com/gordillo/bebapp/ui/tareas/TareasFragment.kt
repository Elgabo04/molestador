package com.gordillo.bebapp.ui.tareas

// Permiso de camara requerido para escanear QR.
import android.Manifest
// Dialogo nativo usado para crear modales simples.
import android.app.AlertDialog
// Selector nativo de fecha.
import android.app.DatePickerDialog
// Selector nativo de hora.
import android.app.TimePickerDialog
// Libreria para revisar permisos en tiempo de ejecucion.
import android.content.pm.PackageManager
// Libreria base para construir vistas.
import android.os.Bundle
// Libreria para inflar XML a vistas.
import android.view.LayoutInflater
// Libreria base de vistas Android.
import android.view.View
// Contenedor usado por fragments.
import android.view.ViewGroup
// Boton de imagen usado para editar tareas.
import android.widget.ImageButton
// Contenedor usado para insertar la camara QR solo en tiempo de ejecucion.
import android.widget.FrameLayout
// Contenedor lineal para listas dinamicas.
import android.widget.LinearLayout
// Texto nativo para contenido.
import android.widget.TextView
// Mensajes cortos al usuario.
import android.widget.Toast
// Compatibilidad para permisos y colores.
import androidx.core.content.ContextCompat
// Fragment base de AndroidX.
import androidx.fragment.app.Fragment
// Referencias a recursos generados.
import com.gordillo.bebapp.R
// Utilidad local para mostrar timestamps.
import com.gordillo.bebapp.datos.FormateadorFecha
// Repositorio temporal reemplazable por Supabase/backend.
import com.gordillo.bebapp.datos.RepositorioLocalTemporal
// Modelo de proximidad calculada.
import com.gordillo.bebapp.modelos.ProximidadTarea
// Modelo de tarea academica.
import com.gordillo.bebapp.modelos.TareaAcademica
// Callback del lector ZXing Embedded.
import com.journeyapps.barcodescanner.BarcodeCallback
// Resultado del lector ZXing Embedded.
import com.journeyapps.barcodescanner.BarcodeResult
// Vista incrustada de ZXing Embedded para escanear QR.
import com.journeyapps.barcodescanner.DecoratedBarcodeView
// Punto detectado por ZXing durante el escaneo.
import com.google.zxing.ResultPoint
// Boton Material para acciones principales y secundarias.
import com.google.android.material.button.MaterialButton
// Boton flotante Material para agregar tareas.
import com.google.android.material.floatingactionbutton.FloatingActionButton
// Campo de texto Material para formularios.
import com.google.android.material.textfield.TextInputEditText
// Libreria usada para construir y comparar timestamps.
import java.util.Calendar
// Libreria usada para manejar fechas con hora.
import java.util.Date
// Libreria matematica usada para calcular diferencias de tiempo.
import kotlin.math.abs

// Fragment que muestra tareas activas, creacion, edicion y vinculacion de dispositivo.
class TareasFragment : Fragment() {

    // Contenedor visible cuando el usuario ya tiene dispositivo.
    private lateinit var contenidoConTareas: LinearLayout

    // Contenedor visible cuando falta vincular dispositivo.
    private lateinit var contenidoSinDispositivo: LinearLayout

    // Lista vertical donde se agregan cards de tareas.
    private lateinit var listaTareas: LinearLayout

    // Banner rojo que resume tareas vencidas.
    private lateinit var txtBannerVencidas: TextView

    // Boton flotante para abrir el modal de nueva tarea.
    private lateinit var btnAgregarTarea: FloatingActionButton

    // Boton para abrir el modal de vinculacion QR.
    private lateinit var btnVincularDispositivo: MaterialButton

    // Dialogo actual de vinculacion para cerrarlo al leer QR.
    private var dialogoVinculacion: AlertDialog? = null

    // Vista de camara incrustada para ZXing.
    private var vistaEscanerQr: DecoratedBarcodeView? = null

    // Crea la vista del fragmento.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tareas, container, false)
    }

    // Inicializa componentes y pinta informacion cuando la vista ya existe.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarComponentes(view)
        inicializarEventos()
        procesarEstadoDispositivo()
    }

    // Pausa la camara si el fragmento deja de estar activo.
    override fun onPause() {
        vistaEscanerQr?.pause()
        super.onPause()
    }

    // Recibe el resultado del permiso de camara.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODIGO_PERMISO_CAMARA && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            iniciarEscaneoQr()
        } else if (requestCode == CODIGO_PERMISO_CAMARA) {
            Toast.makeText(requireContext(), "La camara es necesaria para vincular el dispositivo.", Toast.LENGTH_SHORT).show()
        }
    }

    // Vincula vistas XML del fragmento.
    private fun inicializarComponentes(view: View) {
        contenidoConTareas = view.findViewById(R.id.contenidoConTareas)
        contenidoSinDispositivo = view.findViewById(R.id.contenidoSinDispositivo)
        listaTareas = view.findViewById(R.id.listaTareas)
        txtBannerVencidas = view.findViewById(R.id.txtBannerVencidas)
        btnAgregarTarea = view.findViewById(R.id.btnAgregarTarea)
        btnVincularDispositivo = view.findViewById(R.id.btnVincularDispositivo)
    }

    // Registra eventos de agregar y vincular.
    private fun inicializarEventos() {
        btnAgregarTarea.setOnClickListener {
            mostrarModalTarea(null)
        }
        btnVincularDispositivo.setOnClickListener {
            mostrarModalVinculacion()
        }
    }

    // Decide si se muestra el onboarding de dispositivo o la lista de tareas.
    private fun procesarEstadoDispositivo() {
        val dispositivoVinculado = RepositorioLocalTemporal.dispositivoVinculado
        contenidoConTareas.visibility = if (dispositivoVinculado) View.VISIBLE else View.GONE
        btnAgregarTarea.visibility = if (dispositivoVinculado) View.VISIBLE else View.GONE
        contenidoSinDispositivo.visibility = if (dispositivoVinculado) View.GONE else View.VISIBLE
        if (dispositivoVinculado) {
            procesarDatosTareas()
        }
    }

    // Pinta banner y cards ordenadas por fecha de vencimiento.
    private fun procesarDatosTareas() {
        listaTareas.removeAllViews()
        val tareas = RepositorioLocalTemporal.obtenerTareasActivas()
        txtBannerVencidas.text = "${RepositorioLocalTemporal.contarTareasVencidas()} tareas vencidas"
        tareas.forEach { tarea ->
            listaTareas.addView(crearVistaTarea(tarea))
        }
    }

    // Crea la card visual para una tarea activa.
    private fun crearVistaTarea(tarea: TareaAcademica): View {
        val vista = layoutInflater.inflate(R.layout.item_tarea, listaTareas, false)
        val indicador = vista.findViewById<View>(R.id.vistaIndicador)
        val txtTitulo = vista.findViewById<TextView>(R.id.txtTituloTarea)
        val txtDescripcion = vista.findViewById<TextView>(R.id.txtDescripcionTarea)
        val txtFecha = vista.findViewById<TextView>(R.id.txtFechaTarea)
        val txtProximidad = vista.findViewById<TextView>(R.id.txtProximidadTarea)
        val btnEditar = vista.findViewById<ImageButton>(R.id.btnEditarTarea)
        val proximidad = calcularProximidad(tarea.fechaVencimiento)

        txtTitulo.text = tarea.nombreTarea
        txtDescripcion.text = tarea.descripcionTarea
        txtFecha.text = "Vence: ${FormateadorFecha.mostrar(tarea.fechaVencimiento)}"
        txtProximidad.text = proximidad.textoVisible
        aplicarEstiloProximidad(indicador, txtProximidad, txtFecha, proximidad)
        btnEditar.setOnClickListener {
            mostrarModalTarea(tarea)
        }
        return vista
    }

    // Calcula la cercania de la tarea segun su fecha de vencimiento.
    private fun calcularProximidad(fechaVencimiento: Date): ProximidadTarea {
        val ahora = Date()
        val minutosDiferencia = (fechaVencimiento.time - ahora.time) / 60000
        return when {
            fechaVencimiento.before(ahora) -> ProximidadTarea.VENCIDO
            abs(minutosDiferencia) <= MINUTOS_PROXIMO -> ProximidadTarea.PROXIMO
            else -> ProximidadTarea.AL_DIA
        }
    }

    // Aplica colores y badges segun vencido, proximo o al dia.
    private fun aplicarEstiloProximidad(indicador: View, badge: TextView, fecha: TextView, proximidad: ProximidadTarea) {
        when (proximidad) {
            ProximidadTarea.VENCIDO -> {
                indicador.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_rojo))
                badge.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_rojo_pastel))
                badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_rojo_oscuro))
                fecha.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_rojo))
            }
            ProximidadTarea.PROXIMO -> {
                indicador.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_amarillo))
                badge.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_amarillo_pastel))
                badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_marron))
                fecha.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_texto_secundario))
            }
            ProximidadTarea.AL_DIA -> {
                indicador.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_verde))
                badge.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.beb_verde_pastel))
                badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_verde_oscuro))
                fecha.setTextColor(ContextCompat.getColor(requireContext(), R.color.beb_texto_secundario))
            }
        }
    }

    // Abre el modal para crear o editar una tarea.
    private fun mostrarModalTarea(tarea: TareaAcademica?) {
        val vistaModal = layoutInflater.inflate(R.layout.dialog_tarea, null)
        val txtTituloModal = vistaModal.findViewById<TextView>(R.id.txtTituloModalTarea)
        val inputTitulo = vistaModal.findViewById<TextInputEditText>(R.id.inputTituloTarea)
        val inputDescripcion = vistaModal.findViewById<TextInputEditText>(R.id.inputDescripcionTarea)
        val btnSeleccionarFecha = vistaModal.findViewById<MaterialButton>(R.id.btnSeleccionarFecha)
        val btnCancelar = vistaModal.findViewById<MaterialButton>(R.id.btnCancelarTarea)
        val btnGuardar = vistaModal.findViewById<MaterialButton>(R.id.btnGuardarTarea)
        var fechaSeleccionada = tarea?.fechaVencimiento ?: Date()

        txtTituloModal.text = if (tarea == null) "Agregar tarea" else "Editar tarea"
        inputTitulo.setText(tarea?.nombreTarea.orEmpty())
        inputDescripcion.setText(tarea?.descripcionTarea.orEmpty())
        btnSeleccionarFecha.text = FormateadorFecha.mostrar(fechaSeleccionada)

        val dialogo = AlertDialog.Builder(requireContext())
            .setView(vistaModal)
            .create()

        btnSeleccionarFecha.setOnClickListener {
            mostrarSelectorFecha(fechaSeleccionada) { nuevaFecha ->
                fechaSeleccionada = nuevaFecha
                btnSeleccionarFecha.text = FormateadorFecha.mostrar(nuevaFecha)
            }
        }
        btnCancelar.setOnClickListener {
            dialogo.dismiss()
        }
        btnGuardar.setOnClickListener {
            val titulo = inputTitulo.text?.toString()?.trim().orEmpty()
            val descripcion = inputDescripcion.text?.toString()?.trim().orEmpty()
            if (titulo.isBlank() || descripcion.isBlank()) {
                Toast.makeText(requireContext(), "Completa titulo y descripcion.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            procesarGuardadoTarea(tarea, titulo, descripcion, fechaSeleccionada)
            dialogo.dismiss()
        }
        dialogo.show()
    }

    // Abre selector de fecha y luego selector de hora para un timestamp completo.
    private fun mostrarSelectorFecha(fechaInicial: Date, alSeleccionar: (Date) -> Unit) {
        val calendario = Calendar.getInstance()
        calendario.time = fechaInicial
        DatePickerDialog(
            requireContext(),
            { _, anio, mes, dia ->
                TimePickerDialog(
                    requireContext(),
                    { _, hora, minuto ->
                        val seleccion = Calendar.getInstance()
                        seleccion.set(anio, mes, dia, hora, minuto, 0)
                        seleccion.set(Calendar.MILLISECOND, 0)
                        alSeleccionar(seleccion.time)
                    },
                    calendario.get(Calendar.HOUR_OF_DAY),
                    calendario.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Guarda cambios en el repositorio temporal y refresca la lista.
    private fun procesarGuardadoTarea(tarea: TareaAcademica?, titulo: String, descripcion: String, fechaVencimiento: Date) {
        if (tarea == null) {
            RepositorioLocalTemporal.agregarTarea(titulo, descripcion, fechaVencimiento)
        } else {
            RepositorioLocalTemporal.actualizarTarea(tarea.idTarea, titulo, descripcion, fechaVencimiento)
        }
        procesarDatosTareas()
    }

    // Abre el modal que solicita permiso y muestra la camara incrustada.
    private fun mostrarModalVinculacion() {
        val vistaModal = layoutInflater.inflate(R.layout.dialog_vincular_dispositivo, null)
        val btnCancelar = vistaModal.findViewById<MaterialButton>(R.id.btnCancelarVinculacion)
        val contenedorEscaner = vistaModal.findViewById<FrameLayout>(R.id.contenedorEscanerQr)
        vistaEscanerQr = DecoratedBarcodeView(requireContext())
        contenedorEscaner.removeAllViews()
        contenedorEscaner.addView(
            vistaEscanerQr,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        dialogoVinculacion = AlertDialog.Builder(requireContext())
            .setView(vistaModal)
            .create()
        btnCancelar.setOnClickListener {
            vistaEscanerQr?.pause()
            dialogoVinculacion?.dismiss()
        }
        dialogoVinculacion?.setOnDismissListener {
            vistaEscanerQr?.pause()
            vistaEscanerQr = null
        }
        dialogoVinculacion?.show()
        solicitarPermisoCamara()
    }

    // Solicita permiso de camara o inicia escaneo si ya existe.
    private fun solicitarPermisoCamara() {
        val permisoConcedido = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (permisoConcedido) {
            iniciarEscaneoQr()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CODIGO_PERMISO_CAMARA)
        }
    }

    // Inicia ZXing Embedded en modo QR continuo hasta obtener un codigo.
    private fun iniciarEscaneoQr() {
        val escaner = vistaEscanerQr ?: return
        escaner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                val codigo = result?.text.orEmpty()
                if (codigo.isBlank()) return
                escaner.pause()
                procesarCodigoDispositivo(codigo)
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                // No se muestran puntos en pantalla; la camara centrada basta para esta version.
            }
        })
        escaner.resume()
    }

    // Punto de inyeccion para guardar el id_dispositivo en Supabase.
    private fun procesarCodigoDispositivo(codigoDispositivo: String) {
        RepositorioLocalTemporal.vincularDispositivo(codigoDispositivo)
        dialogoVinculacion?.dismiss()
        Toast.makeText(requireContext(), "Dispositivo vinculado correctamente.", Toast.LENGTH_SHORT).show()
        procesarEstadoDispositivo()
    }

    companion object {
        // Codigo interno para identificar la respuesta del permiso de camara.
        private const val CODIGO_PERMISO_CAMARA = 201

        // Minutos antes de vencer que se consideran proximos.
        private const val MINUTOS_PROXIMO = 60
    }
}
