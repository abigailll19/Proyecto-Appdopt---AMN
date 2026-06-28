package com.example.appdopt.core.model

enum class ColorBlindMode { NONE, PROTANOPIA, DEUTERANOPIA, TRITANOPIA }
enum class AppLanguage { SPANISH, ENGLISH }

data class AppSettings(
    val isDarkMode: Boolean = false,
    val isHighContrast: Boolean = false,
    val colorBlindMode: ColorBlindMode = ColorBlindMode.NONE,
    val fontSizeMultiplier: Float = 1.0f,
    val language: AppLanguage = AppLanguage.SPANISH
)
