package com.gordillo.bebapp.modelos

// Libreria usada para manejar fechas con hora en memoria antes de conectar Supabase.
import java.util.Date

// Modelo temporal alineado con la tabla tarea y el historial de reprogramacion.
data class TareaAcademica(
    // Identificador equivalente a id_tarea.
    val idTarea: Int,
    // Identificador equivalente a id_usuario.
    val idUsuario: Int,
    // Nombre visible de la tarea.
    var nombreTarea: String,
    // Detalle del trabajo que debe realizarse.
    var descripcionTarea: String,
    // Fecha exacta en que se creo la tarea.
    val fechaCreacion: Date,
    // Fecha exacta en que vence la tarea.
    var fechaVencimiento: Date,
    // Estado real que luego se guardara en Supabase.
    var estado: EstadoTarea,
    // Fecha anterior mas reciente cuando la tarea fue reprogramada.
    var fechaVencimientoAnterior: Date? = null
)
