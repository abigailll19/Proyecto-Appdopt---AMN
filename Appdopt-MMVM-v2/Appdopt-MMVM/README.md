# Appdopt - Proyecto de Adopción de Mascotas

Paso A: Configuración de Dependencias

Se incorporaron las dependencias necesarias para implementar persistencia de datos, configuración de la aplicación e inyección de dependencias.

### Dependencias agregadas

- **Room**
  - Persistencia local de datos.
  - Almacenamiento de mascotas y solicitudes de adopción.

- **DataStore**
  - Almacenamiento de preferencias de la aplicación.
  - Configuración de tema, contraste y otras preferencias del usuario.

- **Hilt**
  - Inyección de dependencias.
  - Gestión del ciclo de vida de repositorios y demás componentes.

- **KSP (Kotlin Symbol Processing)**
  - Procesador moderno de anotaciones.
  - Utilizado por Room y Hilt para la generación automática de código.

---

## Paso B: Migración del Modelo de Datos (SSOT)

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