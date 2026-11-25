# Sistema de Gestión Inmobiliaria — API REST (`InsomnioyAnsiedad`)

Proyecto integrador desarrollado para la **Tecnicatura Universitaria en Tecnologías de la Información (TUTI)** — **Universidad Tecnológica Nacional (UTN) Facultad Regional Santa Fe (FRSF)**.

La aplicación implementa una arquitectura orientada a servicios (RESTful API con soporte HATEOAS) para la gestión integral de alquileres: emisión de facturas, registro de pagos, seguimiento de incidentes en propiedades e informes técnicos de resolución.

---

## Integrantes y Módulos Desarrollados

| Integrante | Módulo | Recurso Base | Funcionalidades Principales |
| :--- | :--- | :--- | :--- |
| **Cristian Nozralah** | **S01 — Facturas** | `/facturas` | Emisión automática mensual desde contrato, cargos adicionales, filtros por estado/período y anulación. |
| **Nicolás Mendez** | **S02 — Pagos** | `/pagos` | Registro de pagos totales con validación de importe e intereses, consulta por ID/factura y control de conflictos. |
| **Lucas Rojas** | **S03 — Incidentes** | `/incidentes` | Alta de reclamos asociados a contratos activos, filtros por categoría/estado y transiciones de ciclo de vida. |
| **Claudio Laborda** | **S04 — Informes de Resolución** | `/informes-resolucion` | Registro técnico de resolución, cálculo de costos, cierre automático del incidente (`RESUELTO`) y enlaces HATEOAS. |

---

## Stack Tecnológico

* **Lenguaje y Framework:** Java 17+ / Spring Boot (Web, Data JPA, Validation, HATEOAS)
* **Base de Datos:** MySQL / MariaDB (XAMPP)
* **Documentación y Pruebas Interactivas:** Springdoc OpenAPI / Swagger UI
* **Gestión de Dependencias y Entorno:** Maven / Eclipse STS

---

## Consideraciones Importantes Antes de Levantar el Proyecto

1. **Puerto del Servidor (`8081`):**
   La aplicación está configurada en `src/main/resources/application.properties` con `server.port=8081` para evitar conflictos con otros servicios locales en el puerto `8080`. Tanto los endpoints como Swagger UI y los enlaces HATEOAS responden sobre `http://localhost:8081`.
2. **Creación Automática de Base de Datos y Tablas:**
   No es necesario crear manualmente el esquema en MySQL. La URL de conexión (`jdbc:mysql://localhost:3306/inmobiliaria_db?createDatabaseIfNotExist=true&serverTimezone=UTC`) junto con `spring.jpa.hibernate.ddl-auto=update` crea automáticamente la base de datos **`inmobiliaria_db`** y todas sus tablas al iniciar Spring Boot por primera vez (credenciales por defecto de XAMPP: usuario `root`, sin contraseña).
3. **Orden Obligatorio en la Primera Ejecución:**
   Primero se debe iniciar **MySQL** en XAMPP y ejecutar **Spring Boot** en Eclipse STS para que Hibernate genere las tablas. Recién después se deben ejecutar los scripts SQL de datos de prueba en phpMyAdmin.
4. **Limpieza Segura de Claves Foráneas:**
   Cada script SQL de prueba limpia las tablas en orden jerárquico (`DELETE FROM` desde tablas hijas hacia tablas padre) y reinicia los contadores `AUTO_INCREMENT = 1` para evitar bloqueos por restricciones de integridad referencial en InnoDB/MariaDB.

---

## Guía Paso a Paso para Arrancar y Testear

### 1. Encender servicios en XAMPP

1. Abre el **XAMPP Control Panel**.
2. Haz clic en **Start** en **Apache** (habilita phpMyAdmin).
3. Haz clic en **Start** en **MySQL** (puerto `3306`) y verifica que ambos módulos figuren en verde.

### 2. Arrancar el proyecto en Eclipse STS

1. Abre **Eclipse STS** sobre el proyecto **`InsomnioyAnsiedad`** (rama `master`).
2. Si es la primera vez que se importa el proyecto, haz clic derecho sobre **`InsomnioyAnsiedad`** → **Maven → Update Project...** (`Alt + F5`) → **OK**.
3. Haz clic derecho sobre el proyecto → **Run As → Spring Boot App**.
4. Espera en la consola el mensaje de confirmación:
   ```text
   Started InsomnioyAnsiedadApplication ... Tomcat started on port 8081 (http)
   ```

### 3. Cargar Scripts SQL en phpMyAdmin y Probar en Swagger UI

1. Abre phpMyAdmin en **`http://localhost:8888/phpmyadmin`**, selecciona **`inmobiliaria_db`** y entra a la pestaña **SQL**.
2. Abre Swagger UI en **`http://localhost:8081/swagger-ui/index.html`**.
3. Ejecuta el script SQL del módulo que deseas evaluar y realiza las peticiones en Swagger (no es necesario reiniciar Spring Boot al cambiar de módulo).

---

## Scripts de Datos de Prueba por Módulo

### Módulo S01 — Facturas (`/facturas`)

Deja precargadas las personas, una propiedad y dos contratos (`id = 1` en estado `ACTIVO` a nombre de **Pepito Santos** e `id = 2` en estado `FINALIZADO` a nombre de **Pinola Dias**).

```sql
DELETE FROM informe_resolucion;
DELETE FROM historial_estado_incidente;
DELETE FROM incidente;
DELETE FROM pago;
DELETE FROM facturas;
DELETE FROM contrato;
DELETE FROM propiedades;
DELETE FROM persona;

ALTER TABLE informe_resolucion AUTO_INCREMENT = 1;
ALTER TABLE historial_estado_incidente AUTO_INCREMENT = 1;
ALTER TABLE incidente AUTO_INCREMENT = 1;
ALTER TABLE pago AUTO_INCREMENT = 1;
ALTER TABLE facturas AUTO_INCREMENT = 1;
ALTER TABLE contrato AUTO_INCREMENT = 1;
ALTER TABLE propiedades AUTO_INCREMENT = 1;
ALTER TABLE persona AUTO_INCREMENT = 1;

INSERT INTO persona (id, nombre, apellido, dni, datos_contacto) VALUES
(1, 'Carlos', 'Gómez', '25111222', 'carlos.gomez@inmobiliaria.com'),
(2, 'Pepito', 'Santos', '32444555', 'pepito.santos@email.com'),
(3, 'Pinola', 'Dias', '35777888', 'pinola.dias@email.com');

INSERT INTO propiedades (id, direccion, tipo, precio_referencia, disponible)
VALUES (1, 'San Martín 2450, Piso 3 Dpto B, Santa Fe', 'Departamento', 250000.00, 0);

INSERT INTO contrato (
    id, propiedad_id, propietario_id, inquilino_id, inquilino_nombre,
    fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento_mensual,
    activo, eliminado_logicamente, estado
) VALUES
(1, 1, 1, 2, 'Pepito Santos', '2026-01-01', 12, 250000.00, 10, 1, 0, 'ACTIVO'),
(2, 1, 1, 3, 'Pinola Dias', '2024-01-01', 12, 180000.00, 10, 0, 0, 'FINALIZADO');
```

**Casos de prueba en Swagger (`Facturas`):**
* **`POST /facturas`** (`{"contratoId": 1, "periodo": "2026-12"}`) → **`201 Created`** (emite alquiler mensual tomando `$250.000` del contrato).
* **`POST /facturas`** (`{"contratoId": 1, "periodo": "2026-12", "concepto": "Expensas extraordinarias", "importe": 35000.00}`) → **`201 Created`** (cargo adicional).
* **`GET /facturas`** (filtros `contratoId=1`, `periodo=2026-12`, `estado=PENDIENTE`) → **`200 OK`**.
* **`PATCH /facturas/{id}/estado`** (`id=2`) → **`200 OK`** (cambia estado a `ANULADA`).
* **`POST /facturas`** (`{"contratoId": 2, "periodo": "2026-12"}`) → Rechazo de negocio por contrato `FINALIZADO`.

---

### Módulo S02 — Pagos (`/pagos`)

Deja precargado el contrato activo y 3 facturas (`id = 1` pendiente por `$250.000`, `id = 2` pendiente por `$35.000` e `id = 3` en estado `ANULADA`).

```sql
DELETE FROM informe_resolucion;
DELETE FROM historial_estado_incidente;
DELETE FROM incidente;
DELETE FROM pago;
DELETE FROM facturas;
DELETE FROM contrato;
DELETE FROM propiedades;
DELETE FROM persona;

ALTER TABLE informe_resolucion AUTO_INCREMENT = 1;
ALTER TABLE historial_estado_incidente AUTO_INCREMENT = 1;
ALTER TABLE incidente AUTO_INCREMENT = 1;
ALTER TABLE pago AUTO_INCREMENT = 1;
ALTER TABLE facturas AUTO_INCREMENT = 1;
ALTER TABLE contrato AUTO_INCREMENT = 1;
ALTER TABLE propiedades AUTO_INCREMENT = 1;
ALTER TABLE persona AUTO_INCREMENT = 1;

INSERT INTO persona (id, nombre, apellido, dni, datos_contacto) VALUES
(1, 'Carlos', 'Gómez', '25111222', 'carlos.gomez@inmobiliaria.com'),
(2, 'Pepito', 'Santos', '32444555', 'pepito.santos@email.com');

INSERT INTO propiedades (id, direccion, tipo, precio_referencia, disponible)
VALUES (1, 'San Martín 2450, Piso 3 Dpto B, Santa Fe', 'Departamento', 250000.00, 0);

INSERT INTO contrato (
    id, propiedad_id, propietario_id, inquilino_id, inquilino_nombre,
    fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento_mensual,
    activo, eliminado_logicamente, estado
) VALUES
(1, 1, 1, 2, 'Pepito Santos', '2026-01-01', 12, 250000.00, 10, 1, 0, 'ACTIVO');

INSERT INTO facturas (
    id, contrato_id, periodo_facturado, concepto, fecha_emision, fecha_vencimiento, importe, estado
) VALUES
(1, 1, '2026-12', 'Alquiler correspondiente al período 2026-12', '2026-11-25', '2026-12-10', 250000.00, 'PENDIENTE'),
(2, 1, '2026-12', 'Expensas extraordinarias diciembre', '2026-11-25', '2026-12-10', 35000.00, 'PENDIENTE'),
(3, 1, '2026-12', 'Cargo administrativo erróneo', '2026-11-25', '2026-12-10', 10000.00, 'ANULADA');
```

**Casos de prueba en Swagger (`pago-controller`):**
* **`POST /pagos`** (`{"facturaId": 1, "medioPago": "TRANSFERENCIA", "importePagado": 250000.00, "interes": 0}`) → **`201 Created`**.
* **`GET /pagos/{id}`** (`id=1`) y **`GET /pagos`** (`facturaId=1`) → **`200 OK`**.
* **`POST /pagos`** sobre `facturaId=1` (ya pagada), `facturaId=2` con importe distinto (`20000.00`) o `facturaId=3` (`ANULADA`) → **`409 Conflict`**.
* **`POST /pagos`** sobre `facturaId=999` → **`404 Not Found`**.

---

### Módulo S03 — Incidentes (`/incidentes`)

Deja precargados la propiedad y el contrato `id = 1` en estado `ACTIVO`.

```sql
DELETE FROM informe_resolucion;
DELETE FROM historial_estado_incidente;
DELETE FROM incidente;
DELETE FROM pago;
DELETE FROM facturas;
DELETE FROM contrato;
DELETE FROM propiedades;
DELETE FROM persona;

ALTER TABLE informe_resolucion AUTO_INCREMENT = 1;
ALTER TABLE historial_estado_incidente AUTO_INCREMENT = 1;
ALTER TABLE incidente AUTO_INCREMENT = 1;
ALTER TABLE pago AUTO_INCREMENT = 1;
ALTER TABLE facturas AUTO_INCREMENT = 1;
ALTER TABLE contrato AUTO_INCREMENT = 1;
ALTER TABLE propiedades AUTO_INCREMENT = 1;
ALTER TABLE persona AUTO_INCREMENT = 1;

INSERT INTO persona (id, nombre, apellido, dni, datos_contacto) VALUES
(1, 'Carlos', 'Gómez', '25111222', 'carlos.gomez@inmobiliaria.com'),
(2, 'Pepito', 'Santos', '32444555', 'pepito.santos@email.com');

INSERT INTO propiedades (id, direccion, tipo, precio_referencia, disponible)
VALUES (1, 'San Martín 2450, Piso 3 Dpto B, Santa Fe', 'Departamento', 250000.00, 0);

INSERT INTO contrato (
    id, propiedad_id, propietario_id, inquilino_id, inquilino_nombre,
    fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento_mensual,
    activo, eliminado_logicamente, estado
) VALUES
(1, 1, 1, 2, 'Pepito Santos', '2026-01-01', 12, 250000.00, 10, 1, 0, 'ACTIVO');
```

**Casos de prueba en Swagger (`incidente-controller`):**
* **`POST /incidentes`** (`{"contratoId": 1, "titulo": "Pérdida de agua en cocina", "descripcion": "Filtración bajo mesada.", "categoria": "PLOMERIA", "prioridad": "ALTA"}`) → **`201 Created`** (estado inicial `ABIERTO`).
* **`PATCH /incidentes/{id}/estado`** (`id=1`, `{"estado": "EN_PROCESO"}`) → **`200 OK`**.
* **`GET /incidentes/{id}`** (`id=1`) → **`200 OK`** | (`id=99`) → **`404 Not Found`**.

---

### Módulo S04 — Informes de Resolución (`/informes-resolucion`)

Deja precargados los incidentes `id = 1` en estado `EN_PROCESO` (habilitado para resolver) e `id = 2` en estado `ABIERTO` (para validar el rechazo por estado inválido).

```sql
DELETE FROM informe_resolucion;
DELETE FROM historial_estado_incidente;
DELETE FROM incidente;

ALTER TABLE informe_resolucion AUTO_INCREMENT = 1;
ALTER TABLE historial_estado_incidente AUTO_INCREMENT = 1;
ALTER TABLE incidente AUTO_INCREMENT = 1;

INSERT INTO incidente (id, estado) VALUES (1, 'EN_PROCESO');
INSERT INTO incidente (id, estado) VALUES (2, 'ABIERTO');
```

**Casos de prueba en Swagger (`informe-resolucion-controller`):**
* **`POST /informes-resolucion`** (`{"incidenteId": 1, "descripcionTrabajo": "Reemplazo de flexible de agua fría.", "costo": 18500.50, "proveedor": "Sanitarios Santa Fe", "responsableTecnico": "Carlos Gómez", "observaciones": "Presión verificada."}`) → **`201 Created`** con enlaces HATEOAS y actualización del incidente `1` a `RESUELTO`.
* **`GET /informes-resolucion/{id}`** (`id=1`) y **`GET /informes-resolucion`** (`incidenteId=1`) → **`200 OK`**.
* **`POST /informes-resolucion`** con campos vacíos o costo negativo (`costo: -500`) → **`400 Bad Request`**.
* **`GET /informes-resolucion/{id}`** (`id=999`) → **`404 Not Found`**.
* **`POST /informes-resolucion`** sobre `incidenteId=2` (`ABIERTO`) → **`409 Conflict`**.