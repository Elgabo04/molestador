package com.gordillo.bebapp.modelos

// Enum que representa los estados reales guardados para cada tarea.
enum class EstadoTarea(val textoVisible: String) {
    REALIZADO("realizado"),
    EN_PROCESO("en proceso"),
    POSTERGADO("postergado")
}
