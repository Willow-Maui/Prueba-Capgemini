# ORIGINAL:
# Capgemini-Test

## Prerrequisitos

Asegúrese de tener instalados los siguientes componentes antes de ejecutar el proyecto:

- Java 17
- Maven
- Git
- Docker

---

## Ejecución del Docker para tener la infraestructura necesaria

1. Clone el repositorio:
   bash
   git clone <url-del-repositorio>
   
2. Navegue a la carpeta docker:
   bash
   cd docker
   
3. Levante los contenedores con Docker Compose:
   bash
   docker-compose up
   

### Contenedores del Docker

- *PostgreSQL:* Base de datos.
- *Mock-server:* API expuesta en el puerto 1080.

---

## APIs Expuestas en el Mock-server

### 1. API check-dni
- *Método:* PATCH
- *URL:* http://localhost:1080/check-dni
- *Body de la petición:*
  json
  {
    "dni": "<dni>"
  }
  
- *Respuestas:*
  - *OK:* Código HTTP 200 para cualquier DNI.
  - *KO:* Código HTTP 409 si el DNI es 99999999w.

---

### 2. API notification
#### Email
- *Método:* POST
- *URL:* http://localhost:1080/email
- *Body de la petición:*
  json
  {
    "email": "<email>",
    "message": "<msg>"
  }
  
- *Respuesta:*
  - *OK:* Código HTTP 200.

#### SMS
- *Método:* POST
- *URL:* http://localhost:1080/sms
- *Body de la petición:*
  json
  {
    "phone": "<phone>",
    "message": "<msg>"
  }
  
- *Respuesta:*
  - *OK:* Código HTTP 200.

---

## Contexto

La aplicación debe gestionar salas y usuarios bajo las siguientes condiciones:

- Cada sala tiene un ID único (long incremental).
- Una sala puede contener N usuarios.
- Un usuario puede estar en una única sala.
- Al guardar el usuario en la sala, se valida su DNI contra una API externa.
- Almacena el usuario en la base de datos y notifica según su rol.
- Devuelve el ID del usuario almacenado.

---

## Requisitos

### Validaciones:
1. *Nombre:* No debe contener más de 6 caracteres.
2. *Email:* Debe contener un @ y un ..
3. *Rol:* Sólo puede ser admin o superadmin.
4. Si el usuario ya existe (por email), se debe lanzar una excepción.
5. Validar el DNI contra la API externa del mock-server.

### Notificaciones:
- *Admin:* Notificación por email con el mensaje: "usuario guardado".
- *Superadmin:* Notificación por SMS con el mensaje: "usuario guardado".

### Respuesta esperada:
- *Retornar el ID generado.*

---

## Métodos a Implementar

### 1. Crear Usuario
- *Método:* POST
- *Descripción:* Guarda un usuario en la sala 1 y retorna su ID.
- *Ejemplo de JSON a guardar:*
  json
  {
    "name": "pablo",
    "email": "email@email.com",
    "phone": "677998899",
    "rol": "admin",
    "dni": "23454234W"
  }
  
- *Respuestas:*
  - *OK:* 
    - Código HTTP 201 Created
    - Body:
      json
      {
        "id": "<id>"
      }
      
  - *KO:* 
    - Código HTTP 409 Conflict
    - Body:
      json
      {
        "code": 409,
        "message": "error validation <email | userName | dni>"
      }
      

### 2. Obtener Usuario
- *Método:* GET
- *Descripción:* Obtiene un usuario basado en su ID dentro de la sala 1.

---

## Condiciones Opcionales

1. *Escalabilidad:* La aplicación podrá escalar para manejar diferentes contextos (pagos, pedidos, etc.).
2. *Capacidad:* La aplicación podrá procesar desde una hasta millones de peticiones (no es necesario implementar Kubernetes).

---

## Cosas que se Valoran

1. *Pruebas:*
   - Unitarias.
   - De integración.
   - De aceptación.
2. *Arquitectura e implementación.*
3. *Uso de Spring y abstracción del framework.*
4. *Patrones de diseño.*

---

## Método de Entrega

- Subir el proyecto a un repositorio público personal.
- Todos los commits deben estar realizados en la rama main.

---

# MODIFICADO: Arquitectura y Documentación Completa

## 📋 Tabla de Contenidos

1. [Visión General](#visión-general)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Arquitectura Hexagonal](#arquitectura-hexagonal)
4. [Decisiones de Diseño](#decisiones-de-diseño)
5. [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
6. [Estructura del Proyecto](#estructura-del-proyecto)
7. [Cómo Ejecutar](#cómo-ejecutar)
8. [APIs Disponibles](#apis-disponibles)
9. [Testing](#testing)
10. [Troubleshooting](#troubleshooting)

---

## 🎯 Visión General

Esta aplicación es un sistema de gestión de usuarios y salas construido bajo arquitectura **hexagonal (puertos y adaptadores)** con principios **SOLID** y patrones de diseño modernos. La solución es escalable, testeable y fácil de mantener.

### Objetivos Alcanzados

✅ Arquitectura hexagonal totalmente implementada  
✅ Separación clara de responsabilidades (Domain → Application → Infrastructure)  
✅ Lógica de negocio pura sin dependencias de frameworks  
✅ Persistencia dual: PostgreSQL (escritura) + MySQL (lectura)  
✅ Integración con APIs externas mediante Feign  
✅ Notificaciones por Email/SMS según rol del usuario  
✅ Validación de DNI contra API externa  
✅ Testing: unitarios, integración y E2E con TestContainers  
✅ Migraciones de BD con Flyway  
✅ Cobertura de código con JaCoCo  

---

## 📦 Stack Tecnológico

| Componente | Versión | Propósito |
|-----------|---------|----------|
| **Java** | 17 LTS | Lenguaje de programación |
| **Spring Boot** | 3.2.0 | Framework web y orquestación |
| **Spring Data JPA** | 3.2.0 | ORM e interacción con BD |
| **Spring Cloud OpenFeign** | 4.x | Cliente HTTP declarativo |
| **PostgreSQL** | 13+ | Base de datos de escritura (WriteDB) |
| **MySQL** | 8.0+ | Base de datos de lectura (ReadDB) |
| **Flyway** | 9.22.3 | Versionado de esquema de BD |
| **MapStruct** | 1.5.5 | Mapeo DTO ↔ Domain (compile-time) |
| **Lombok** | 1.18.x | Reducción de boilerplate |
| **Kafka** | 3.x | Sincronización de datos entre BDs |
| **JUnit 5** | 5.9.x | Framework de testing |
| **Mockito** | 5.x | Mocking en tests |
| **TestContainers** | 1.19.3 | Contenedores Docker para tests |
| **Maven** | 3.8.1+ | Gestor de dependencias y construcción |

---

## 🏛️ Arquitectura Hexagonal

La arquitectura hexagonal separa la lógica de negocio del acceso a datos y sistemas externos mediante puertos y adaptadores.

### Estructura Conceptual

```
╔════════════════════════════════════════════════════════════════╗
║                   CAPA DE DOMINIO (PURO)                       ║
║  Entidades, Lógica de Negocio, Repositorios (Interfaces)      ║
║  ⚠️ SIN DEPENDENCIAS DE SPRING, BD, O FRAMEWORKS              ║
╚════════════════════════════════════════════════════════════════╝
                           ↓ ↑
╔════════════════════════════════════════════════════════════════╗
║                  CAPA DE APLICACIÓN (PURA)                     ║
║  Application Services, Casos de Uso, Puertos, DTOs            ║
║  ⚠️ SIN DEPENDENCIAS DE SPRING NI FRAMEWORKS                  ║
╚════════════════════════════════════════════════════════════════╝
                           ↓ ↑
╔════════════════════════════════════════════════════════════════╗
║              CAPA DE INFRAESTRUCTURA                           ║
║  REST Controllers, Adaptadores: JPA, Feign, Kafka, APIs       ║
║  Request/Response, Configuración Spring                       ║
╚════════════════════════════════════════════════════════════════╝
```

### Reglas de Dependencia

- **Las capas internas NO conocen a las externas**
- Domain NO depende de Application ni Infrastructure
- Application NO depende de Infrastructure
- Infrastructure depende de Application y Domain
- Las dependencias apuntan siempre hacia adentro
- Application no importa Spring
- Domain es totalmente independiente

---

## 🎨 Decisiones de Diseño

### 1. Arquitectura Hexagonal (Puertos y Adaptadores)

**Por qué:**
- Desacoplamiento total del dominio y aplicación de Spring y tecnologías
- Facilita cambiar BD, APIs externas sin tocar lógica de negocio
- Testeable sin necesidad de contenedores (tests unitarios puros)
- Escalable para nuevas features sin quebrar existentes

**Cómo se implementa:**
```
domain/user/
├── User.java (Entidad de dominio - PURA)
├── UserRepository.java (Puerto de salida - Interface)
└── UserValidator.java (Lógica de negocio)

application/
├── usecase/user/CreateUserUseCase.java (Caso de uso - PURO)
├── ports/input/CreateUserInputPort.java (Puerto de entrada)
├── ports/output/UserRepositoryPort.java (Puerto de salida)
└── dto/CreateUserRequest.java (DTO de aplicación)

infrastructure/adapter/
├── output/persistence/UserPersistenceAdapter.java (Implementa puerto)
├── input/rest/UserRestController.java (REST → Puerto)
└── dto/CreateUserRestRequest.java (DTO REST)
```

### 2. Persistencia Dual (CQRS Pattern)

**Problema:** Necesidad de separar lecturas (optimizadas) de escrituras (consistentes)

**Solución:**
- **WriteDB (PostgreSQL):** Fuente de verdad, transacciones ACID
- **ReadDB (MySQL):** Réplica optimizada para lectura
- **Sincronización:** Kafka procesa eventos de escritura y actualiza ReadDB

**Ventajas:**
✅ Escrituras consistentes en PostgreSQL  
✅ Lecturas escalables en MySQL  
✅ Modelo de datos optimizado por operación (lectura vs escritura)  
✅ Desacoplamiento temporal mediante eventos  

### 3. Capas Independientes sin Spring en Domain y Application

**Implementación:**
- **Domain:** Solo Java puro, lógica de negocio sin dependencias
- **Application:** Cases de uso, orquestación, DTOs internos - sin Spring
- **Infrastructure:** Spring, REST, JPA, clientes externos

**Beneficio:** 
- Lógica de negocio completamente aislada y reutilizable
- Fácil de testear sin contenedores Spring
- Cambios tecnológicos no afectan el core de la aplicación

### 4. Objetos de Dominio, Aplicación e Infraestructura Diferenciados

**Problema:** Contaminar las capas internas con detalles técnicos

**Solución:**
```
Domain:              Application:             Infrastructure:
├── User             ├── CreateUserRequest    ├── CreateUserRestRequest
├── Room             ├── CreateUserResponse   ├── CreateUserRestResponse
└── UserValidator    └── UserDTO              └── UserJpaEntity
```

**Regla Clave:** Los DTOs de REST (`CreateUserRestRequest`) **no** salen de Infrastructure. La aplicación trabaja con sus propios DTOs (`CreateUserRequest`), totalmente independientes de la presentación.

**Ventaja:** 
✅ Cambios en la API REST no rompen la lógica de aplicación  
✅ Puedo cambiar de REST a GraphQL sin tocar Application  
✅ Reutilización de Application Services en diferentes canales  

### 5. Inyección de Dependencias en Infrastructure

**Implementación:**
```java
@Configuration
public class ApplicationConfig {
    @Bean
    public CreateUserUseCase createUserUseCase(
        UserRepositoryPort repositoryPort,
        DniValidationPort dniPort,
        NotificationPort notificationPort) {
        return new CreateUserUseCase(repositoryPort, dniPort, notificationPort);
    }
}
```

**Beneficio:** Los servicios puros de aplicación reciben sus dependencias desde Infrastructure, facilitando tests e inyección.

### 6. Excepciones Personalizadas Jerárquicas

```java
public class UserException extends DomainException { }
public class UserAlreadyExistsException extends UserException { }
public class InvalidUserNameException extends UserException { }
```

**Beneficio:** Manejo específico de errores, mapeado a códigos HTTP apropiados en Infrastructure.

---

## 🔧 Patrones de Diseño Implementados

### 1. **Hexagonal Architecture (Ports & Adapters)**
Separación clara entre puertos (interfaces) y adaptadores (implementaciones).

### 2. **Application Service Pattern**
Servicio que orquesta casos de uso, delegando lógica al dominio.

```java
public class UserApplicationService {
    public CreateUserResponse createUser(CreateUserRequest request) {
        // Orquesta la lógica: validación, persistencia, notificaciones
        User user = createUserUseCase.execute(request);
        return userMapper.toResponse(user);
    }
}
```

### 3. **Repository Pattern**
Abstracción de persistencia mediante interfaces:
- `UserRepository` (puerto de dominio)
- `UserPersistenceAdapter` (adaptador JPA en Infrastructure)

### 4. **Mapper Pattern (DTO ↔ Domain)**
Conversión entre capas usando MapStruct:

```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toApplicationDTO(User user);
    User toDomain(UserDTO dto);
}
```

### 5. **Decorator Pattern (Feign Clients)**
Clientes HTTP externos envueltos en adaptadores:

```java
@FeignClient(name = "dni-validation")
public interface DniClient {
    @PatchMapping("/check-dni")
    DniResponse checkDni(@RequestBody DniRequest request);
}

public class DniValidationAdapter implements DniValidationPort {
    // Implementa puerto, usa DniClient internamente
}
```

### 6. **CQRS Pattern (Write & Read Separation)**
- **Write Model:** PostgreSQL (comandos, transacciones)
- **Read Model:** MySQL (consultas optimizadas)
- **Sincronización:** Kafka (eventos)

### 7. **Error Handling Pattern**
Manejo global de excepciones en `RestExceptionHandler`:

```java
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists() {
        return ResponseEntity
            .status(409)
            .body(new ErrorResponse(409, "User already exists"));
    }
}
```

### 8. **Builder Pattern**
Construcción segura de objetos complejos:

```java
CreateUserRequest.builder()
    .name("Pablo")
    .email("pablo@example.com")
    .phone("677998899")
    .rol("admin")
    .dni("23454234W")
    .build();
```

---

## 📁 Estructura del Proyecto

```
src/main/java/com/capgemini/test/code/
│
├── domain/                              [NÚCLEO PURO - Sin Spring]
│   ├── user/
│   │   ├── User.java                    [Entidad de dominio]
│   │   ├── UserRole.java                [Enum: admin, superadmin]
│   │   ├── UserValidator.java           [Lógica de validación]
│   │   └── UserRepository.java          [Puerto de salida - Interface]
│   │
│   ├── room/
│   │   ├── Room.java                    [Entidad de dominio]
│   │   └── RoomRepository.java          [Puerto de salida - Interface]
│   │
│   └── shared/
│       ├── DomainException.java         [Excepción base]
│       └── AggregateRoot.java           [Raíz de agregado]
│
├── application/                         [ORQUESTACIÓN - Sin Spring (mostly)]
│   ├── usecase/
│   │   ├── user/
│   │   │   ├── CreateUserUseCase.java   [Caso de uso puro]
│   │   │   ├── GetUserUseCase.java
│   │   │   └── UserApplicationService.java [@Service - Orquestador]
│   │   └── room/
│   │       └── RoomApplicationService.java
│   │
│   ├── ports/
│   │   ├── input/
│   │   │   ├── CreateUserInputPort.java [Interface]
│   │   │   └── GetUserInputPort.java
│   │   │
│   │   └── output/
│   │       ├── UserRepositoryPort.java
│   │       ├── DniValidationPort.java
│   │       └── NotificationPort.java
│   │
│   ├── dto/
│   │   ├── CreateUserRequest.java       [DTO entrada]
│   │   ├── CreateUserResponse.java      [DTO salida]
│   │   ├── UserDTO.java
│   │   └── ErrorResponse.java
│   │
│   └── mapper/
│       └── UserMapper.java              [Mapeo DTO ↔ Domain]
│
├── infrastructure/                      [DETALLES TÉCNICOS - Spring + Presentación]
│   ├── adapter/
│   │   ├── input/
│   │   │   ├── rest/
│   │   │   │   ├── user/
│   │   │   │   │   ├── UserRestController.java [@RestController]
│   │   │   │   │   └── CreateUserRestRequest.java [DTO REST entrada]
│   │   │   │   └── room/
│   │   │   │       └── RoomRestController.java
│   │   │   │
│   │   │   └── dto/
│   │   │       └── ErrorResponse.java [DTO REST salida]
│   │   │
│   │   └── output/
│   │       ├── persistence/
│   │       │   ├── user/
│   │       │   │   ├── UserJpaEntity.java      [@Entity JPA]
│   │       │   │   ├── UserJpaRepository.java  [Spring Data]
│   │       │   │   └── UserPersistenceAdapter.java [Implementa UserRepositoryPort]
│   │       │   │
│   │       │   └── room/
│   │       │       └── (similar)
│   │       │
│   │       └── external/
│   │           ├── dni/
│   │           │   ├── DniClient.java          [@FeignClient]
│   │           │   └── DniValidationAdapter.java [Implementa DniValidationPort]
│   │           │
│   │           └── notification/
│   │               ├── EmailClient.java
│   │               ├── SmsClient.java
│   │               └── NotificationAdapter.java
│   │
│   ├── config/
│   │   ├── ApplicationConfig.java        [Bean configuration]
│   │   ├── DatasourceConfig.java         [Dual datasources]
│   │   ├── KafkaConfig.java              [Kafka producers/consumers]
│   │   └── WebConfig.java                [CORS, interceptores]
│   │
│   ├── exception/
│   │   ├── RestExceptionHandler.java     [Manejo global de excepciones]
│   │   └── ErrorCodes.java               [Códigos de error]
│   │
│   └── messaging/
│       ├── event/
│       │   └── UserCreatedEvent.java
│       └── kafka/
│           ├── UserEventProducer.java
│           └── ReadDbSyncConsumer.java
│
└── CodeApplication.java                 [Punto de entrada]
```

---

## 🚀 Cómo Ejecutar

### Requisitos Previos

- **Java 17+** (JDK 17 LTS)
- **Maven 3.8.1+**
- **Docker & Docker Compose**
- **Git**

### Perfiles de Ejecución

La aplicación tiene dos perfiles de configuración:

#### **Sin Perfil (Default)**
Es la configuración por defecto. Se ejecuta sin especificar perfil.
```yaml
# Usa src/main/resources/application.yml
Conecta a:
- PostgreSQL en localhost:5432
- MySQL en localhost:3306
- Kafka en localhost:9092 (servidor externo en Docker)
```

#### **Perfil `local`**
Añade Kafka embebido en la aplicación (no requiere servidor Kafka externo en Docker).
```yaml
# Usa src/main/resources/application-local.yml
Conecta a:
- PostgreSQL en localhost:5432
- MySQL en localhost:3306
- Kafka embebido (en memoria)
```

**Casos de uso:**
- **Sin perfil:** Producción, desarrollo con Kafka externo
- **`local`:** Desarrollo rápido sin dependencias externas adicionales

### Ejecución Paso a Paso

#### Terminal 1: Compilar Proyecto

Desde la raíz del proyecto:
```bash
mvn clean compile
```

Esto descarga dependencias y compila el código.

#### Terminal 2: Levantar Infraestructura Docker

```bash
cd docker
docker-compose up
```

Espera hasta ver:
```
postgres_1  | LOG:  database system is ready to accept connections
mysql_1     | [Note] [Entrypoint]: MySQL Community Server 8.0.x started
mockserver_1 | MockServer started on port 1080
```

#### Terminal 3: Ejecutar Tests (Opcional)

```bash
mvn test
```

#### Terminal 4: Iniciar Aplicación

**Perfil Default (sin perfil, default):**
```bash
mvn spring-boot:run
```

**Perfil Local (Kafka embebido):**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

Espera hasta ver:
```
Started CodeApplication in 15.234 seconds (JVM running for 16.789)
```

### ✅ Verificar que Está Funcionando

```bash
# Obtener usuario que no existe (debe retornar 404)
curl -X GET http://localhost:8080/api/v1/users/999

# Respuesta esperada:
# {"code": 404, "message": "User not found with id: 999"}
```

---

## 📡 APIs Disponibles

### Crear Usuario

```http
POST /api/v1/users
Content-Type: application/json

{
  "name": "Pablo",
  "email": "pablo@example.com",
  "phone": "677998899",
  "rol": "admin",
  "dni": "23454234W"
}
```

**Respuestas:**

✅ **201 Created** - Usuario creado exitosamente
```json
{
  "id": 1
}
```

❌ **409 Conflict** - Error de validación
```json
{
  "code": 409,
  "message": "error validation email"
}
```

### Obtener Usuario

```http
GET /api/v1/users/{id}
```

**Respuestas:**

✅ **200 OK**
```json
{
  "id": 1,
  "name": "Pablo",
  "email": "pablo@example.com",
  "phone": "677998899",
  "rol": "admin"
}
```

❌ **404 Not Found**
```json
{
  "code": 404,
  "message": "User not found with id: 999"
}
```

### Validaciones Aplicadas

| Campo | Regla | Ejemplo |
|-------|-------|---------|
| **name** | Max 6 caracteres | "Pablo" ✅, "PabloLargo" ❌ |
| **email** | Debe contener @ y . | "p@e.com" ✅, "pemail" ❌ |
| **rol** | Solo "admin" o "superadmin" | "admin" ✅, "user" ❌ |
| **dni** | Validado contra API externa | Consulta `/check-dni` |
| **email único** | No puede duplicarse | Primer usuario con mismo email ✅, segundo ❌ |

### Notificaciones

- **Admin:** Recibe Email con mensaje "usuario guardado"
- **Superadmin:** Recibe SMS con mensaje "usuario guardado"

---

## 🧪 Testing

La aplicación incluye tests en tres niveles:

### 1. Tests Unitarios (Domain)
Lógica pura sin dependencias externas.

```bash
mvn test -Dtest=UserValidatorTest
```

**Ubicación:** `src/test/java/com/capgemini/test/code/domain/`

### 2. Tests de Integración (Application)
Casos de uso con puertos mockeados.

```bash
mvn test -Dtest=UserApplicationServiceTest
```

**Ubicación:** `src/test/java/com/capgemini/test/code/application/`

### 3. Tests End-to-End (Infrastructure)
Contra contenedores reales (TestContainers).

```bash
mvn test -Dtest=UserCreateIntegrationTest
```

**Ubicación:** `src/test/java/com/capgemini/test/code/infrastructure/`

### Ejecutar Todos los Tests

```bash
mvn clean test
```

### Generar Reporte de Cobertura

```bash
mvn clean test jacoco:report
```

Abre: `target/site/jacoco/index.html`

---

## 🔍 Troubleshooting

### Puerto 8080 en Uso

Opción 1: Cambiar puerto en compilación:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

Opción 2: Modificar `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Docker no inicia contenedores

```bash
# Verifica Docker está corriendo
docker --version
docker ps

# Desde el directorio docker/ del proyecto:
docker-compose down -v
docker-compose up --build
```

### Error de Conexión a BD

```
Could not connect to PostgreSQL/MySQL
```

**Solución:**
1. Verifica que Docker Compose está en marcha: `docker ps`
2. Espera ~30 segundos después de `docker-compose up`
3. Comprueba credenciales en `src/main/resources/application.yml`

### Tests Fallan por Timeouts

```bash
mvn test -DargLine="-Xmx1024m"
```

### Maven sin compilar cambios

```bash
mvn clean
mvn compile
```

---

## 📊 Características Principales

### ✅ Implementado

- [x] Arquitectura Hexagonal completa
- [x] Domain puro sin dependencias externas
- [x] Application Services orquestadores
- [x] Adaptadores de entrada/salida
- [x] Persistencia dual (PostgreSQL + MySQL)
- [x] Sincronización Kafka entre BDs
- [x] Integración con APIs externas (Feign)
- [x] Validación de DNI
- [x] Notificaciones por Email/SMS
- [x] Manejo global de excepciones
- [x] Tests unitarios, integración y E2E
- [x] Migraciones Flyway
- [x] Cobertura de código JaCoCo
- [x] Separación clara de responsabilidades
- [x] SOLID Principles implementados

### 🚀 Potencial de Escalabilidad

Esta arquitectura permite fácilmente:
- Agregar nuevas features sin modificar existentes
- Cambiar BD, APIs o tecnologías sin tocar lógica de negocio
- Escalar horizontalmente con múltiples instancias
- Agregar CQRS avanzado, Event Sourcing, o Saga Pattern
- Soportar millones de peticiones con cache distribuido

---

## 👥 Autor

**Willow Maui García Moreno**  
Arquitectura y diseño implementados siguiendo principios de Domain-Driven Design y SOLID.

