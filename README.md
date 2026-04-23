Tu README ya está muy bien estructurado, pero para cumplir **estrictamente** con la consigna de la imagen, vamos a darle un enfoque más narrativo a la presentación del proyecto y asegurarnos de que las secciones solicitadas (Presentación, Problemáticas, Reglas de Negocio y Pasos de Ejecución) sean las protagonistas.

Aquí tienes la versión ajustada:

---

# 🍦 GelatoSoft - API de Gestión para Heladerías

## 📖 Presentación del Proyecto
**GelatoSoft** es una solución integral desarrollada en Java con Spring Boot, diseñada para digitalizar y optimizar la operación diaria de heladerías artesanales y comerciales. El sistema nace como respuesta a la necesidad de centralizar la gestión de ventas, el control de stock por sabores y la administración de usuarios en una única plataforma robusta. 

A través de una arquitectura basada en microservicios y una interfaz de programación de aplicaciones (API) RESTful, GelatoSoft permite a los propietarios de negocios tener una visión clara y en tiempo real de su inventario y transacciones, garantizando la escalabilidad y el mantenimiento a largo plazo mediante el uso de estándares modernos de desarrollo.

---

## 🚀 Problemáticas que Resuelve
El desarrollo de este software aborda directamente los siguientes desafíos del sector:

* **Descontrol en el Inventario de Sabores:** Automatiza el seguimiento de suministros y productos finales, eliminando la incertidumbre sobre el stock disponible y evitando quiebres de inventario en horas pico.
* **Vulnerabilidad de Datos:** Resuelve la falta de seguridad mediante un sistema de autenticación basado en roles, asegurando que solo el personal autorizado pueda modificar precios o acceder a reportes financieros.
* **Ausencia de Trazabilidad:** Registra cada movimiento y transacción, permitiendo auditar quién, cuándo y qué acción realizó, lo que reduce drásticamente los errores operativos y las pérdidas no justificadas.

---

## 💼 Reglas de Negocio
Para garantizar el correcto funcionamiento del ecosistema de la heladería, se han implementado las siguientes reglas:

1.  **Integridad de Ventas:** No se puede procesar una venta si no se especifica el sabor, la cantidad y el ID del empleado responsable.
2.  **Seguridad JWT:** El acceso a cualquier endpoint de gestión (stock o catálogo) requiere un token **JSON Web Token** válido y activo.
3.  **Validación de Dominios:** El sistema rechaza automáticamente cualquier intento de cargar productos con valores negativos en precio o stock, o campos obligatorios vacíos.
4.  **Política de Auditoría (AOP):** Cualquier eliminación de registros críticos (como un sabor del catálogo o un usuario) genera un log automático para fines de auditoría técnica.

---

## ⚙️ Pasos Requeridos para la Ejecución
Siga estas instrucciones para poner en marcha el entorno de desarrollo:

### 1. Requisitos Previos
* **Java 17+** instalado.
* **MySQL** o **PostgreSQL** (según configuración).
* **Maven** (opcional, se incluye el wrapper `./mvnw`).

### 2. Instalación y Configuración
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/gelato-soft.git
    cd gelato-soft
    ```
2.  **Configurar Base de Datos:**
    Cree una base de datos local y actualice las credenciales en el archivo:
    `src/main/resources/application.properties`
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/gelatosoft_db
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    ```

### 3. Ejecución
Ejecute el siguiente comando en la terminal desde la raíz del proyecto:
```bash
./mvnw spring-boot:run
```

### 4. Verificación
Una vez que el servicio esté corriendo en el puerto **8080**, puede acceder a la documentación interactiva de la API para realizar pruebas:
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## 👥 Equipo de Desarrollo
* **Manuel Pelegrino**
* **Melody Aizpirarte**
* **Emanuel Bustos**
* **Laureano Martin**
* **Laureano Martin**

**Institución:** Universidad Tecnológica Nacional (UTN) - Facultad Regional Mar del Plata.
