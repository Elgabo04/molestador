package com.gordillo.bebapp.datos

// Libreria usada para modificar fechas de ejemplo.
import java.util.Calendar
// Libreria usada para representar timestamps locales antes de conectar Supabase.
import java.util.Date
// Modelo de estados reales de una tarea.
import com.gordillo.bebapp.modelos.EstadoTarea
// Modelo principal usado por la interfaz de tareas e historial.
import com.gordillo.bebapp.modelos.TareaAcademica

// Repositorio temporal que deja la app lista para reemplazar datos mock por Supabase.
object RepositorioLocalTemporal {

    // Bandera temporal que simula si el usuario ya vinculo su dispositivo fisico.
    var dispositivoVinculado: Boolean = false

    // Nombre temporal del usuario autenticado por Google.
    var nombreUsuario: String = "Estudiante"

    // Contador local para generar ids mientras no exista backend conectado.
    private var siguienteIdTarea: Int = 4

    // Lista temporal que alimenta la pantalla de tareas y el historial.
    private val tareas: MutableList<TareaAcademica> = mutableListOf(
        crearTareaEjemplo(1, "Practica de Algebra", "Resolver ejercicios 1 al 12.", -1, EstadoTarea.EN_PROCESO),
        crearTareaEjemplo(2, "Resumen de Historia", "Preparar media pagina sobre independencia.", 0, EstadoTarea.POSTERGADO),
        crearTareaEjemplo(3, "Lectura de Biologia", "Leer el capitulo de celulas.", 3, EstadoTarea.REALIZADO)
    )

    // Crea datos de muestra con fechas cercanas para visualizar todos los estados.
    private fun crearTareaEjemplo(idTarea: Int, titulo: String, descripcion: String, horasDesdeAhora: Int, estado: EstadoTarea): TareaAcademica {
        val calendario = Calendar.getInstance()
        calendario.add(Calendar.HOUR_OF_DAY, horasDesdeAhora)
        val vencimiento = calendario.time
        val fechaAnterior = if (estado == EstadoTarea.POSTERGADO) {
            val anterior = Calendar.getInstance()
            anterior.add(Calendar.HOUR_OF_DAY, horasDesdeAhora - 2)
            anterior.time
        } else {
            null
        }
        return TareaAcademica(idTarea, 1, titulo, descripcion, Date(), vencimiento, estado, fechaAnterior)
    }

    // Obtiene tareas visibles en el dashboard, excluyendo las realizadas.
    fun obtenerTareasActivas(): List<TareaAcademica> {
        return tareas
            .filter { tarea -> tarea.estado != EstadoTarea.REALIZADO }
            .sortedBy { tarea -> tarea.fechaVencimiento }
    }

    // Obtiene todas las tareas para mostrar el historial de estados.
    fun obtenerHistorial(): List<TareaAcademica> {
        return tareas.sortedByDescending { tarea -> tarea.fechaVencimiento }
    }

    // Agrega una tarea nueva con estado inicial "en proceso".
    fun agregarTarea(nombre: String, descripcion: String, fechaVencimiento: Date) {
        tareas.add(
            TareaAcademica(
                idTarea = siguienteIdTarea++,
                idUsuario = 1,
                nombreTarea = nombre,
                descripcionTarea = descripcion,
                fechaCreacion = Date(),
                fechaVencimiento = fechaVencimiento,
                estado = EstadoTarea.EN_PROCESO
            )
        )
    }

    // Actualiza solo los campos editables desde la app movil.
    fun actualizarTarea(idTarea: Int, nombre: String, descripcion: String, fechaVencimiento: Date) {
        val tarea = tareas.firstOrNull { item -> item.idTarea == idTarea } ?: return
        if (tarea.fechaVencimiento != fechaVencimiento) {
            tarea.fechaVencimientoAnterior = tarea.fechaVencimiento
            tarea.estado = EstadoTarea.POSTERGADO
        }
        tarea.nombreTarea = nombre
        tarea.descripcionTarea = descripcion
        tarea.fechaVencimiento = fechaVencimiento
    }

    // Guarda el codigo QR recibido y simula la asociacion usuario-dispositivo.
    fun vincularDispositivo(codigoDispositivo: String) {
        dispositivoVinculado = codigoDispositivo.isNotBlank()
    }

    // Calcula cuantas tareas activas ya vencieron.
    fun contarTareasVencidas(): Int {
        val ahora = Date()
        return obtenerTareasActivas().count { tarea -> tarea.fechaVencimiento.before(ahora) }
    }

    // Calcula cuantas tareas tienen historial de reprogramacion.
    fun contarTareasReprogramadas(): Int {
        return tareas.count { tarea -> tarea.fechaVencimientoAnterior != null || tarea.estado == EstadoTarea.POSTERGADO }
    }
}
