# 🌿 DIETETIC - Sistema de Consultorio Nutricional Premium

DIETETIC es una solución móvil integral diseñada para modernizar la gestión de consultas nutricionales y planes de alimentación. Con una interfaz de "Lujo Orgánico", la aplicación conecta a Pacientes, Nutricionistas y Administradores en un ecosistema de salud de alta gama.

## 🚀 Requisitos de Instalación
*   **Android Studio:** Jellyfish o superior.
*   **Lenguaje:** Kotlin 1.9.24+
*   **UI:** Jetpack Compose (Material 3).
*   **Arquitectura:** MVVM (Model-View-ViewModel).
*   **Conectividad:** Requiere acceso a internet para consumir la API REST.

## 🌐 Configuración del Backend
La aplicación consume una API REST desarrollada en **Django REST Framework** con **PostgreSQL**.
*   **URL Base:** `https://paz-dietetica.uaeftt-ute.site/api/`
*   **Configuración:** La URL se define en el archivo `local.properties` como `API_BASE_URL`.

## 👤 Usuarios de Prueba
| Rol | Usuario                | Contraseña    |
| :--- |:-----------------------|:--------------|
| **Administrador** | `admin`                | `Admin1234`   |
| **Nutricionista** | `nutrimaria@gmail.com` | `nutrimaria`  |
| **Paciente** | `prueba12`             | `prueba12345` |

---

## 🛠️ Entidades Implementadas (CRUD Completo)

La aplicación gestiona 5 entidades principales para un control total:

1.  **Usuarios:** Gestión de perfiles, autenticación JWT y roles (Admin, Nutricionista, Paciente).
2.  **Planes Nutricionales:** Catálogo de dietas (Keto, Vegano, etc.) con metas calóricas y duración.
3.  **Nutricionistas:** Registro profesional, especialidades y gestión de cuentas de acceso.
4.  **Pacientes:** Seguimiento de medidas (peso, altura), IMC y vinculación con planes.
5.  **Consultas Dietéticas:** Agenda de citas, gestión de estados (Programada, En Curso, Cancelada).

---

## 📱 Listado de Pantallas

1.  **Login/Registro:** Acceso seguro con validación de credenciales.
2.  **Home Dashboard:** Métricas en tiempo real, banner de salud y recomendaciones diarias fotorrealistas.
3.  **Catálogo:** Búsqueda dinámica (`?search=`), paginación (scroll infinito) y filtros de planes.
4.  **Detalle del Plan:** Información técnica, recomendaciones nutricionales y contratación directa.
5.  **Agenda de Consultas:**
    *   *Vista Paciente:* Agendar con especialistas y ver historial.
    *   *Vista Nutri:* Dashboard de citas hoy, gestión de estados y validación de pacientes.
6.  **Perfil VIP:** Gestión de identidad, tarjeta de membresía Premium y configuración.
7.  **Panel Administrativo:** Control total de las entidades con capacidades de creación, edición y borrado.

---

## 📡 Ejemplo de Consumo de API (JWT)
Para peticiones protegidas, se debe incluir el token en el header:
```bash
GET /api/consultas/mine/
Authorization: Bearer <tu_token_access>
```

---

## 🔒 Seguridad y Manejo de Errores

*   **Autenticación JWT:** Los tokens se almacenan de forma cifrada mediante `DataStore`.
*   **Protección de Rutas:** Navegación restringida según el rol del usuario (Role-Based Access Control).
*   **Manejo de Errores:** Mensajes claros para errores 400 (datos), 401 (sesión caducada), 404 (no encontrado) y 500 (servidor).
*   **Cierre de Sesión:** Reinicio forzado de la navegación para garantizar la limpieza de datos en memoria.

---

## ⚙️ Instrucciones de Ejecución
1.  Clonar el repositorio.
2.  Abrir en Android Studio.
3.  Sincronizar Gradle (`Sync Project with Gradle Files`).
4.  Ejecutar en un Emulador (mínimo API 26) o dispositivo físico.

---
**Desarrollado por:** Alexis Paz
**Proyecto:** Final de Aplicaciones Móviles
