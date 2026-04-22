import os

readme_content = """# 🍦 GelatoSoft - API de Gestión para Heladerías

## 📋 Descripción del Proyecto
Este proyecto consiste en el desarrollo de una **API RESTful** robusta y escalable diseñada para optimizar la gestión integral de una heladería[cite: 4, 8]. Permite la administración de inventario, ventas, usuarios y reportes, asegurando la integridad de los datos y una experiencia de usuario fluida mediante una arquitectura moderna en **Spring Boot**[cite: 22].

---

## 👥 Integrantes del Equipo
* **Manuel Pelegrino**
* **Melody Aizpirarte**
* **Emanuel Bustos**
* **Laureano Martin**

**Docente:** Tec. Mango Eduardo [cite: 3]
**Institución:** Universidad Tecnológica Nacional - Mar del Plata [cite: 1]

---

## 🚀 Problemáticas que Resuelve
1.  **Control de Stock Ineficiente:** Automatización del seguimiento de sabores y suministros para evitar quiebres de stock[cite: 8].
2.  **Seguridad de la Información:** Implementación de acceso granular mediante roles para proteger operaciones sensibles[cite: 24].
3.  **Falta de Trazabilidad:** Registro detallado de cada transacción y auditoría de cambios en el sistema[cite: 17].

---

## 🛠️ Reglas de Negocio
* **Gestión de Ventas:** Cada venta debe registrar los sabores, cantidades y el empleado responsable.
* **Autenticación Obligatoria:** Solo usuarios autenticados mediante **JWT** pueden realizar pedidos o modificar el catálogo[cite: 24].
* **Validación de Datos:** No se permiten ingresos de productos con precios negativos o nombres vacíos[cite: 26].
* **Auditoría (AOP):** Todas las eliminaciones de productos críticos deben ser registradas automáticamente en los logs[cite: 17].

---

## 💻 Stack Tecnológico
* **Backend:** Java con Spring Boot[cite: 22].
* **Persistencia:** Spring Data JPA con MySQL/PostgreSQL[cite: 23].
* **Seguridad:** Spring Security + JSON Web Tokens (JWT)[cite: 24].
* **Documentación:** Swagger / OpenAPI[cite: 19].
* **Testing:** JUnit y Mockito para servicios críticos[cite: 61].
* **Contenerización:** Docker Compose para despliegue simplificado[cite: 59, 60].

---

## 🏗️ Arquitectura y Estándares
El proyecto sigue una estructura **Package by Feature** para máxima modularidad[cite: 67]:
* **Capas:** Separación estricta entre Controllers, Services (Interfaces), Repositories y DTOs[cite: 13, 16].
* **Patrones de Diseño:** Implementación de Builder, Factory y Singleton según necesidad[cite: 14].
* **Manejo de Errores:** Centralizado mediante `@ControllerAdvice`[cite: 25, 42].
* **Git Flow:** Uso estricto de ramas `main`, `develop` y `feature/US-XX`[cite: 83, 85, 91].

---

## ⚙️ Pasos para la Ejecución
1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/nombre-del-repo.git](https://github.com/tu-usuario/nombre-del-repo.git)
    ```
2.  **Configurar la Base de Datos:**
    Asegúrate de tener MySQL corriendo o utiliza el archivo `docker-compose.yml` provisto[cite: 60].
3.  **Configurar el `application.properties`:**
    Ajusta las credenciales de la base de datos y la clave secreta para el JWT.
4.  **Ejecutar la aplicación:**
    ```bash
    ./mvnw spring-boot:run
    ```
5.  **Acceder a la Documentación:**
    Visita `http://localhost:8080/swagger-ui.html` para interactuar con los endpoints[cite: 19].

---

### 📂 Colección de Postman
En la raíz del proyecto encontrarás el archivo `GelatoSoft_Postman_Collection.json` con todas las peticiones necesarias para probar el sistema[cite: 30].

---
*Este proyecto fue desarrollado bajo los estándares de calidad de la UTN MdP[cite: 113].*
"""

file_path = "README.md"
with open(file_path, "w", encoding="utf-8") as f:
    f.write(readme_content)

print(f"File created: {file_path}")
