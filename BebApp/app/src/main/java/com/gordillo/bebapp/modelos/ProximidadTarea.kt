package com.gordillo.bebapp.modelos

// Enum que representa el estado calculado segun la fecha de vencimiento.
enum class ProximidadTarea(val textoVisible: String) {
    VENCIDO("vencido"),
    PROXIMO("proximo"),
    AL_DIA("al dia")
}
