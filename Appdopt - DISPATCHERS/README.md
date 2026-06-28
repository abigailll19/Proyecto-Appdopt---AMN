# Appdopt - Proyecto de Adopción de Mascotas


---

## Paso A: Migración del Modelo de Datos (SSOT)

Los modelos del paquete `core.model` fueron adaptados para trabajar con Room siguiendo el principio de **Single Source of Truth (SSOT)**.

### Entidades implementadas

#### `Pet`

- Convertida en una entidad de Room.
- Tabla: `pets`.
- Clave primaria autogenerada.

#### `AdoptionRequest`

- Convertida en una entidad de Room.
- Tabla: `adoption_requests`.

### Type Converters

Se implementó la clase `Converters` para permitir que Room almacene tipos de datos complejos, tales como:

- Enumeraciones (`Enum`)
- Listas de cadenas (`List<String>`)

---

## Paso C: Infraestructura de Datos y Repositorios

Se implementó la arquitectura de persistencia para que el repositorio funcione como la **única fuente de verdad (Single Source of Truth)**.

### DAOs

Se crearon las interfaces encargadas del acceso a la base de datos:

- `PetDao`
- `AdoptionRequestDao`

Estas interfaces definen las operaciones CRUD utilizando **Flow**, permitiendo la actualización automática de los datos.

### Base de Datos

#### `AppDatabase`

Base de datos principal de Room que integra:

- Entidades
- DAOs
- Configuración general de persistencia

### Repositorios

#### `PetRepository`

Interfaz que define las operaciones de negocio relacionadas con las mascotas.

#### `OfflinePetRepository`

Implementación de `PetRepository` utilizando Room como mecanismo de persistencia local.

#### `AdoptionRepository`

Interfaz que define las operaciones relacionadas con las solicitudes de adopción.

#### Implementación Offline

Implementación basada en Room para almacenar y consultar las solicitudes de adopción de forma local.

---

## Paso D: Integración con Supabase y Sincronización Remota

Se ha completado la integración con el backend remoto y la implementación final de la arquitectura MAD.

### Fuente Única de Verdad (SSOT) con Supabase
- **Repositorios Híbridos:** Se implementaron `SupabasePetRepository` y `SupabaseAdoptionRepository` que actúan como mediadores entre el servidor y Room.
- **Sincronización:** Los datos se descargan de Supabase y se persisten en Room automáticamente al iniciar la aplicación (`refresh()`).
- **Reactive State:** El ViewModel observa exclusivamente la base de datos local (Room) mediante `Flow`, garantizando una UI funcional incluso sin conexión.

### Manejo de Preferencias y Eventos
- **DataStore:** Persistencia de `AppSettings` (Tema oscuro, Idioma, Accesibilidad) integrada en el flujo reactivo.
- **SharedFlow:** Implementación de eventos de un solo uso para notificaciones de red y errores mediante Snackbars en la UI.
- **Hilt DI:** Configuración completa de módulos para inyectar el `SupabaseClient` y vincular las interfaces de repositorio a sus implementaciones remotas.
