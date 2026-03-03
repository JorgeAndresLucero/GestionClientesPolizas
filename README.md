# Sistema de Gestión de Seguros (Insurance API)

API REST para la gestión de clientes y pólizas de seguros (vida, salud y vehículos), desarrollada con **Spring Boot 3.5.11** y **Java 21**.

---

## Tabla de Contenido

- [Solución Planteada](#-solución-planteada)
- [Modelo de Datos](#-modelo-de-datos)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Cómo Correr Localmente](#-cómo-correr-localmente)
- [Arquitectura AWS Propuesta](#-arquitectura-aws-propuesta)
- [Documentación API](#-documentación-api)
- [Tecnologías](#-tecnologías)

---

## Solución Planteada

Este sistema proporciona una solución integral para la gestión de seguros, permitiendo:

### Funcionalidades Principales

| Módulo | Funcionalidades |
|--------|----------------|
| **Clientes** | CRUD completo de clientes con validación de pólizas activas |
| **Pólizas de Vida** | Creación de pólizas con monto asegurado y beneficiarios (máx 2) |
| **Pólizas de Salud** | Gestión de pólizas con miembros familiares (padres, cónyuge, hijos) |
| **Pólizas de Vehículos** | Administración de pólizas con múltiples vehículos |
| **Beneficiarios** | Gestión de beneficiarios por póliza de vida |

### Reglas de Negocio Implementadas

-  Cada cliente solo puede tener **una póliza de vida** activa
-  Las pólizas de vida permiten **máximo 2 beneficiarios**
-  Las pólizas de salud pueden ser **solo para el titular** o **familiar**
-  Las pólizas de salud familiar permiten: **máximo 2 padres** y **1 cónyuge**

---

## Modelo de Datos

### Diagrama Entidad-Relación

```
┌─────────────────┐
│     CLIENT      │
├─────────────────┤
│ id (PK)         │
│ documentType    │
│ documentNumber  │
│ firstName       │
│ lastName        │
│ email           │
│ phone           │
│ birthDate       │
└────────┬────────┘
         │
         │ 1:N
         ▼
┌─────────────────┐
│     POLICY      │ (Abstracta)
├─────────────────┤
│ id (PK)         │
│ type            │◄─── VIDA, SALUD, VEHICULO
│ client_id (FK)  │
└────────┬────────┘
         │
         │ Herencia JOINED
         ├──────────────────┬──────────────────┐
         ▼                  ▼                  ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│   LIFE_POLICY   │ │  HEALTH_POLICY  │ │ VEHICLE_POLICY  │
├─────────────────┤ ├─────────────────┤ ├─────────────────┤
│ insuredAmount   │ │ coversClientOnly│ │                 │
└────────┬────────┘ └────────┬────────┘ └────────┬────────┘
         │                   │                   │
         │ 1:N               │ 1:N               │ 1:N
         ▼                   ▼                   ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│  BENEFICIARY    │ │  HEALTH_MEMBER  │ │     VEHICLE     │
├─────────────────┤ ├─────────────────┤ ├─────────────────┤
│ id (PK)         │ │ id (PK)         │ │ id (PK)         │
│ name            │ │ name            │ │ plate           │
│ relationship    │ │ relationship    │ │ brand           │
│ lifePolicy_id   │ │ healthPolicy_id │ │ model           │
└─────────────────┘ │ extraCost       │ │ vehicleYear     │
                    │ healthPolicy_id │ │ vehiclePolicy_id│
                    └─────────────────┘ └─────────────────┘
```

### Entidades Principales

| Entidad | Descripción | Campos Clave |
|---------|-------------|--------------|
| `Client` | Cliente del seguro | id, documento, nombre, email, teléfono |
| `Policy` | Clase base abstracta | id, type, client |
| `LifePolicy` | Póliza de vida | insuredAmount, beneficiaries |
| `HealthPolicy` | Póliza de salud | coversClientOnly, members |
| `VehiclePolicy` | Póliza de vehículos | vehicles |
| `Beneficiary` | Beneficiario de póliza de vida | name, relationship |
| `HealthMember` | Miembro de póliza de salud | name, relationship, extraCost |
| `Vehicle` | Vehículo asegurado | plate, brand, model, year |

---

## Arquitectura del Sistema

### Arquitectura en Capas

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTACIÓN (Controller)                 │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────────────┐ │
│  │  Client  │ │  Life    │ │ Health   │ │ Beneficiary    │ │
│  │Controller│ │Controller│ │Controller│ │   Controller   │ │
│  └──────────┘ └──────────┘ └──────────┘ └────────────────┘ │
│  ┌──────────┐ ┌──────────┐                                  │
│  │ Vehicle  │ │  Policy  │                                  │
│  │Controller│ │Controller│                                  │
│  └──────────┘ └──────────┘                                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      NEGOCIO (Service)                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────────────┐  │
│  │  Client  │ │  Life    │ │ Health   │ │ Beneficiary    │  │
│  │ Service  │ │ Service  │ │ Service  │ │    Service     │  │
│  └──────────┘ └──────────┘ └──────────┘ └────────────────┘  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐                     │
│  │ Vehicle  │ │  Policy  │ │  Health  │                     │
│  │ Service  │ │ Service  │ │ Service  │                     │
│  └──────────┘ └──────────┘ └──────────┘                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   PERSISTENCIA (Repository)                 │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐         │
│  │   Client     │ │ LifePolicy   │ │HealthPolicy  │         │
│  │  Repository  │ │  Repository  │ │  Repository  │         │
│  └──────────────┘ └──────────────┘ └──────────────┘         │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐         │
│  │ Beneficiary  │ │VehiclePolicy │ │   Vehicle    │         │
│  │  Repository  │ │  Repository  │ │  Repository  │         │
│  └──────────────┘ └──────────────┘ └──────────────┘         │
│  ┌──────────────┐ ┌──────────────┐                          │
│  │HealthMember  │ │    Policy    │                          │
│  │  Repository  │ │  Repository  │                          │
│  └──────────────┘ └──────────────┘                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    BASE DE DATOS (H2)                       │
│                     JPA / Hibernate                         │
└─────────────────────────────────────────────────────────────┘
```

### Componentes Adicionales

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA TRANSVERSAL                         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │ Global Exception│  │   Swagger/      │                   │
│  │    Handler      │  │   OpenAPI       │                   │
│  └─────────────────┘  └─────────────────┘                   │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │   DTOs          │  │   Validaciones  │                   │
│  │   (Requests)    │  │   (Jakarta)     │                   │
│  └─────────────────┘  └─────────────────┘                   │
└─────────────────────────────────────────────────────────────┘
```

### Flujo de una Petición

```
1. Cliente → HTTP Request → Controller
2. Controller → Valida DTO → Service
3. Service → Regla de Negocio → Repository
4. Repository → Query → Database
5. Database → Resultado → Repository
6. Repository → Entidad → Service
7. Service → Procesa → Controller
8. Controller → Response → Cliente
```

---

## Cómo Correr Localmente

### Requisitos Previos

| Software | Versión | Descripción |
|----------|-------|-------------|
| **Java** | 21 | JDK de Java |
| **Maven** | 3.8 | Gestor de dependencias |
| **Git** | 2.x | Control de versiones |

### Pasos de Instalación

#### 1. Clonar el Repositorio

```bash
git clone https://github.com/JorgeAndresLucero/GestionClientesPolizas.git
cd GestionClientesPolizas
```

#### 2. Configuración de Base de Datos

Por defecto, la aplicación usa **H2 en memoria**.
```

#### 3. Compilar el Proyecto

```bash
# Usando Maven wrapper (recomendado)
./mvnw clean install

# O usando Maven global
mvn clean install
```

#### 4. Ejecutar la Aplicación

```bash
# Usando Maven wrapper
./mvnw spring-boot:run

# O ejecutando el JAR
java -jar target/insurance-api-0.0.1-SNAPSHOT.jar
```

#### 5. Verificar la Aplicación

La aplicación estará disponible en:
```
http://localhost:8080
```

### Endpoints de Salud

```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
http://localhost:8080/swagger-ui.html

# OpenAPI JSON
http://localhost:8080/v3/api-docs
```

### Ejecutar Tests

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar con cobertura
./mvnw test jacoco:report

# Ver reporte de cobertura
# Abrir: target/site/jacoco/index.html
```
---
## Arquitectura AWS Propuesta

### Componentes AWS Detallados
### Capa de Aplicación 
La aplicación se desplegaría inicialmente en una instancia Amazon EC2, ejecutando el servicio Spring Boot.

- Uso de Docker para facilitar despliegues.

- Configuración mediante variables de entorno.

- Posibilidad de escalar horizontalmente agregando más instancias a futuro.


### Evolución gradual hacia arquitecturas más complejas (ECS o Kubernetes).

- Balanceo de Carga: Se propone el uso de Application Load Balancer (ALB):

- Distribución de tráfico HTTP/HTTPS.

- Health checks automáticos.

- Punto único de entrada.

- Preparación para escalado futuro.

Inicialmente se puede trabajar con una sola instancia EC2.

### Base de Datos: Se utilizaría Amazon RDS (MySQL):

- Administración gestionada.

- Backups automáticos.

- Escalabilidad vertical.

Esto evita la complejidad de gestionar bases de datos en servidores propios.

### Seguridad:

- Uso de Security Groups para limitar acceso.

- Base de datos en red privada.

- Acceso solo desde la capa de aplicación.

- Uso de HTTPS en el balanceador.

- AWS Secrets Manager para credenciales.

- IAM roles para acceso seguro.

### Monitoreo

Se utilizaría Amazon CloudWatch para:

- Logs de la aplicación.

- Métricas básicas de CPU y memoria.

- Alarmas ante caídas del servicio.

---

##  Documentación API
### Swagger UI

Accede a la documentación interactiva en:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/clients` | Crear cliente |
| GET | `/clients` | Listar clientes |
| GET | `/clients/{id}` | Buscar cliente por ID |
| PUT | `/clients/{id}` | Actualizar cliente |
| DELETE | `/clients/{id}` | Eliminar cliente |
| POST | `/policies/life` | Crear póliza de vida |
| POST | `/policies/health` | Crear póliza de salud |
| GET | `/policies/health/{id}/members` | Listar miembros de salud |
| POST | `/policies/health/{id}/members` | Añadir miembro a salud |
| POST | `/policies/vehicle` | Crear póliza de vehículo |
| POST | `/policies/vehicle/{id}/vehicles` | Añadir vehículo |
| GET | `/policies/client/{id}` | Listar pólizas por cliente |
| GET | `/policies/{id}` | Buscar póliza por ID |
| DELETE | `/policies/{id}` | Eliminar póliza |
| GET | `/policies/life/{id}/beneficiaries` | Listar beneficiarios |
| POST | `/policies/life/{id}/beneficiaries` | Añadir beneficiario |
| GET | `/policies/beneficiaries` | Listar todos los beneficiarios |

---

## Tecnologías

| Categoría | Tecnología | Versión |
|-----------|------------|---------|
| **Lenguaje** | Java | 21 |
| **Framework** | Spring Boot | 3.5.11 |
| **Build Tool** | Maven | 3.8+ |
| **ORM** | Hibernate | 6.x |
| **Database** | H2 / MySQL | 8.0 |
| **Testing** | JUnit 5, Mockito | 5.x |
| **Documentation** | Springdoc OpenAPI | 2.8.6 |
| **Validation** | Jakarta Validation | 3.x |
| **Lombok** | Project Lombok | 1.18.x |

---

## Cobertura de Tests

El proyecto cuenta con pruebas unitarias que cubren:

-  Servicios (ClientService, LifePolicyService, HealthPolicyService, etc.)
-  Controladores (ClientController, LifePolicyController, etc.)
-  Validaciones de reglas de negocio
-  Manejo de excepciones

---

<div align="center">

</div>
