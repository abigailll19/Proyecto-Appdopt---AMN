# Appdopt - Proyecto de Adopción de Mascotas

Este repositorio contiene la implementación de la aplicación **Appdopt**, enfocada en la gestión de adopciones de mascotas, perfiles de usuario y paneles administrativos.

## 🚀 Arquitectura y Tecnologías

El proyecto sigue el patrón de diseño **MVVM (Model-View-ViewModel)**, asegurando una separación clara entre la interfaz de usuario y la lógica de negocio.

### 📂 Organización por Características (Feature-based)
A diferencia de versiones anteriores que utilizaban una estructura basada en roles, ahora el código está organizado por **Features**. Esto facilita la escalabilidad, el desarrollo bajo el patrón MVVM y el trabajo colaborativo en Git:
- `auth`: Autenticación y registro.
- `pets`: Visualización y detalles de mascotas.
- `admin`: Gestión de refugios y agregado de mascotas.
- `adoption`: Formularios y seguimiento de solicitudes.
- `settings`: Configuración de la aplicación (Modo oscuro, accesibilidad, etc.).

### 🧠 Gestión de Estado con ViewModel y StateFlow
Se utiliza una arquitectura reactiva para asegurar que la interfaz de usuario sea robusta y eficiente:
- **ViewModel**: Centraliza la lógica de negocio y mantiene los datos durante cambios de configuración.
- **MutableStateFlow**: Se emplea para manejar el estado de la UI de forma reactiva, garantizando una "Single Source of Truth" (Fuente Única de Verdad).
- **collectAsStateWithLifecycle**: Permite que la UI observe los cambios de estado de manera segura respecto al ciclo de vida de Android.

### 🛠️ Tecnologías Clave
- **Jetpack Compose**: Para la construcción de UI declarativa.
- **Material 3**: Diseño moderno con soporte para temas dinámicos.
- **Compose Navigation**: Navegación fluida entre pantallas.
- **Kotlin Coroutines**: Gestión de tareas asíncronas.

## 👥 Colaboración en Git
Para mantener el flujo de trabajo ordenado:
1. Asegúrate de trabajar en tu rama de feature correspondiente (ej: `feature/persistence-prep`).
2. La separación por carpetas de `features` minimiza los conflictos al hacer merge.
3. Esta versión establece la base arquitectónica necesaria para la futura implementación de persistencia local con **Room**.

---
*Desarrollado con ❤️ para facilitar la adopción responsable.*
