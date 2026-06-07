# 🐾 Formulario de Adopción — Appdopt

Aplicación móvil Android para gestionar la adopción de mascotas disponible para adopción.

---

## ¿Qué hace?

El usuario completa un formulario con sus datos personales y al enviarlo recibe una confirmación de que su solicitud fue recibida.

---

## Campos del formulario

| Campo | Tipo |
|---|---|
| Nombre completo | Texto |
| Correo electrónico | Email |
| Teléfono | Numérico |
| Dirección | Texto |
| Tipo de vivienda | Dropdown (Casa, Apartamento, Finca, Otro) |
| Ubicación | Dropdown (Quito, Guayaquil, Cuenca, Otra) |

---

## Tecnologías usadas

- **Kotlin** + **Jetpack Compose**
- **ViewModel** (MVVM) para manejo de estado
- **Navigation Compose** para navegación
- **Material 3** para componentes UI

---



## Validación

El botón **Enviar solicitud** solo procede si todos los campos están completos. Si la validación pasa, se muestra un diálogo de confirmación.