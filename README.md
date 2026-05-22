# ☕ DataLatte

DataLatte es una aplicación nativa de Android que permite visualizar, buscar y guardar como favoritos diferentes tipos de café, consumiendo la información de una API REST. La aplicación está construida utilizando las recomendaciones y tecnologías más modernas del ecosistema Android (2026).

## ✨ Características

- **Catálogo de Cafés:** Lista interactiva y dinámica de cafés calientes.
- **Detalle de Producto:** Vista completa con imagen de alta calidad, descripción y lista de ingredientes.
- **Sistema de Favoritos:** Guarda tus cafés preferidos con un solo toque. Estos persisten localmente.
- **Buscador en Tiempo Real:** Filtra la lista de cafés o la lista de favoritos de manera instantánea.
- **Ajustes y Personalización:**
  - Alternar entre Tema Claro y Tema Oscuro.
  - Cambiar entre vista de Lista (LazyColumn) o Cuadrícula (LazyVerticalGrid).
  - Configurar la aplicación para mostrar solo los favoritos al iniciar.
- **Caché Local Intangible:** Implementación de "Single Source of Truth" (Única fuente de la verdad) que asegura que la aplicación funcione eficientemente minimizando llamadas redundantes a la red. Opción para forzar vaciado de caché desde los ajustes.

## 🛠️ Arquitectura y Stack Tecnológico

La aplicación está desarrollada con **100% Kotlin** y **0% XML** (a excepción del Manifest) basándose fuertemente en arquitectura reactiva:

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) con Material Design 3.
- **Arquitectura:** MVVM (Model-View-ViewModel) + Single Source of Truth.
- **Navegación:** Navigation Compose (Type-safe).
- **Red:** [Retrofit](https://square.github.io/retrofit/) 3.0.0 + Gson Converter.
- **Base de Datos Local:** [Room](https://developer.android.com/training/data-storage/room) (KSP) para el almacenamiento de la información de la API y el estado de favoritos.
- **Asincronía & Reactividad:** Kotlin Coroutines & `Flow` / `StateFlow`.
- **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/) 3.2.0.
- **Preferencias:** `SharedPreferences` reactivas expuestas mediante `StateFlow`.

## 📦 Flujo de Datos (Single Source of Truth)

1. La UI (Compose) **únicamente** observa (mediante `collectAsState`) los flujos de datos expuestos por la base de datos local (Room) a través del `ViewModel`.
2. Al iniciar la aplicación, si la base de datos está vacía, el Repositorio solicita los datos a la API (`https://api.sampleapis.com/coffee/hot`).
3. Los datos de la API se insertan en Room.
4. Room emite automáticamente la nueva información hacia el `ViewModel`, y la UI se actualiza reactivamente.

## 🚀 Requisitos y Configuración

- **Android Studio** Ladybug o más reciente.
- **AGP (Android Gradle Plugin)** 9.2.1
- **Kotlin:** 2.3.21
- **KSP:** 2.3.7

### Instrucciones de Ejecución

1. Clona el repositorio.
2. Abre el proyecto en Android Studio.
3. Ejecuta un "Sync Project with Gradle Files".
4. Compila y lanza la aplicación en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.

---
*Desarrollado con Jetpack Compose y Arquitectura Moderna.*
