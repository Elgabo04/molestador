plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Plugin para serializar data classes hacia JSON para comunicacion con Supabase.
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.gordillo.bebapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gordillo.bebapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        // Habilita ViewBinding para acceso tipado a vistas XML sin findViewById.
        viewBinding = true
    }
}

dependencies {
    // Extensiones de Kotlin para componentes core de Android.
    implementation(libs.androidx.core.ktx)
    // Compatibilidad hacia atras con versiones antiguas de Android.
    implementation(libs.androidx.appcompat)
    // Componentes de Material Design (botones, cards, toolbar, etc).
    implementation(libs.material)
    // Libreria para escaneo de codigos QR con camara incrustada.
    implementation(libs.zxing.embedded)
    // Autenticacion con Google Sign-In para inicio de sesion.
    implementation(libs.play.services.auth)
    // Cliente PostgREST de Supabase para operaciones CRUD en la base de datos.
    implementation(libs.supabase.postgrest)
    // Modulo GoTrue de Supabase para autenticacion y manejo de sesiones.
    implementation(libs.supabase.gotrue)
    // Cliente HTTP Ktor requerido como transporte por Supabase-kt en Android.
    implementation(libs.ktor.client.android)
    // Serializacion JSON de Kotlin para convertir objetos a/desde JSON.
    implementation(libs.kotlinx.serialization.json)
    // WorkManager para tareas periodicas en segundo plano (verificacion de vencimientos).
    implementation(libs.androidx.work.runtime)
    // ConstraintLayout para layouts complejos y responsivos.
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
