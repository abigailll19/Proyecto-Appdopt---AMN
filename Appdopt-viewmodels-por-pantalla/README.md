# 📱 Documentación de Implementación: Capa de UI - Appdopt

Este documento detalla la refactorización realizada en la **capa de interfaz de usuario (UI)** para cumplir con los estándares de **arquitectura reactiva** y las restricciones de **State Hoisting** solicitadas.

---

# 🛠️ Cambios Realizados

## 1. Descentralización de la lógica (ViewModels por pantalla)

Se eliminó la dependencia de un único **ViewModel** global. Ahora, cada pantalla principal posee su propio **ViewModel**, permitiendo una gestión de estado más limpia, desacoplada y específica.

### ViewModels implementados

* **LoginViewModel:** Maneja el flujo de autenticación y validación.
* **HomeViewModel:** Gestiona el catálogo de mascotas y los filtros.
* **AddPetViewModel:** Controla el estado y el envío del formulario de registro de mascotas.
* **EditShelterViewModel:** Gestiona la actualización de los datos del refugio.

---

## 2. Implementación del patrón **Route / Screen** (State Hoisting)

Para cumplir con la restricción de **no pasar el ViewModel a los subcomponentes**, cada funcionalidad se dividió en dos niveles claramente diferenciados.

### Nivel **Route** (ej. `LoginRoute`)

* Es el **único componente** que recibe la instancia del **ViewModel**.
* Obtiene el estado de la interfaz mediante el ViewModel.
* Define las acciones que serán enviadas como **lambdas** a la pantalla.

### Nivel **Screen** (ej. `LoginScreen`)

* Cumple con el principio de **State Hoisting**.
* Recibe únicamente:

  * Estados inmutables (por ejemplo, `LoginUiState`).
  * Funciones de orden superior (`() -> Unit`) para propagar eventos.
* No tiene conocimiento del **ViewModel** ni de la capa de datos, manteniendo una UI desacoplada y reutilizable.

---

## 3. Consumo seguro del estado

El manejo del estado sigue las buenas prácticas recomendadas para el ciclo de vida en Android.

### Implementaciones realizadas

* **`collectAsStateWithLifecycle()`**

  * Garantiza que la UI observe cambios únicamente cuando el componente se encuentra activo.
  * Optimiza el consumo de batería y recursos.

* **`StateFlow`**

  * Los ViewModels exponen un estado **inmutable** mediante `StateFlow`.
  * La interfaz representa siempre el estado actual de los datos, siguiendo un flujo reactivo.

---

## 4. Comunicación directa con repositorios (sin capa de dominio)

Con el objetivo de mantener una arquitectura liviana y acorde a los requerimientos del proyecto:

* Los **ViewModels** se comunican directamente con los repositorios (`PetRepository`, `AdoptionRepository`).
* La lógica de negocio permanece en el ViewModel.
* No se implementó una capa de **Use Cases**, ya que actualmente no existe lógica compleja que deba reutilizarse entre múltiples pantallas.

---

# 📂 Archivos refactorizados

### Autenticación

* `LoginViewModel.kt`
* `LoginScreen.kt` (estructura Route/Screen)

### Administración

* `AddPetViewModel.kt`
* `AddPetScreen.kt`
* `EditShelterViewModel.kt`

### Mascotas

* `HomeViewModel.kt`
* `HomeScreen.kt`

### Navegación

* `AppNavigation.kt`, actualizado para orquestar los nuevos componentes **Route**.

