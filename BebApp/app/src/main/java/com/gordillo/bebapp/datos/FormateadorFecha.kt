package com.gordillo.bebapp.datos

// Libreria usada para formatear timestamps de forma legible.
import java.text.SimpleDateFormat
// Libreria usada para representar fechas con hora.
import java.util.Date
// Libreria usada para fijar el idioma de salida.
import java.util.Locale

// Utilidad central para mostrar y convertir fechas de vencimiento.
object FormateadorFecha {

    // Formato visible que mantiene fecha y hora para el usuario.
    private val formatoVisible = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    // Convierte un timestamp a texto visible.
    fun mostrar(fecha: Date): String {
        return formatoVisible.format(fecha)
    }
}
