plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")  // Spécifiez directement la version
    }
}

// Pas besoin de redéfinir les référentiels ici, ils sont gérés dans settings.gradle.kts
