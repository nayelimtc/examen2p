# API de Manejo de Efectivo - Banco Banquito

## Descripción
API para el manejo de efectivo en las ventanillas de las agencias del banco Banquito. Permite a los cajeros realizar tres acciones principales:
- Abrir turno
- Procesar transacciones (depósitos y retiros)
- Cerrar turno

## Tecnologías Utilizadas
- Spring Boot 3.5.3
- Spring Data MongoDB
- OpenAPI/Swagger 2.1.0
- Lombok
- Java 21

## Configuración

### Requisitos Previos
- Java 21
- MongoDB (instalado y ejecutándose en localhost:27017)
- Maven

### Instalación y Ejecución
```bash
# Clonar el repositorio
git clone <repository-url>
cd examen2p

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Acceso a la Documentación
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## Estructura de la Base de Datos

### Colecciones MongoDB
- `turno_cajas`: Información de los turnos de caja
- `transacciones_turnos`: Transacciones realizadas en los turnos

### Denominaciones Soportadas
- $1, $5, $10, $20, $50, $100

## Endpoints de la API

### 1. Turnos de Caja

#### 1.1 Abrir Turno
**POST** `/api/v1/turnos/abrir`

**Descripción:** Permite abrir un nuevo turno de caja con las denominaciones iniciales.

**Cuerpo de la petición:**
```json
{
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "denominacionesIniciales": [
    {
      "valor": 1,
      "cantidad": 100,
      "monto": 100.00
    },
    {
      "valor": 5,
      "cantidad": 50,
      "monto": 250.00
    },
    {
      "valor": 10,
      "cantidad": 30,
      "monto": 300.00
    },
    {
      "valor": 20,
      "cantidad": 20,
      "monto": 400.00
    },
    {
      "valor": 50,
      "cantidad": 10,
      "monto": 500.00
    },
    {
      "valor": 100,
      "cantidad": 5,
      "monto": 500.00
    }
  ]
}
```

**Respuesta exitosa (201):**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d0",
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "codigoTurno": "CAJ01-USU01-20241209",
  "montoInicial": 2050.00,
  "fechaInicio": "2024-12-09T08:00:00",
  "estado": "ABIERTO",
  "denominacionesIniciales": [...],
  "creationDate": "2024-12-09T08:00:00",
  "lastModifiedDate": "2024-12-09T08:00:00",
  "version": 1
}
```

#### 1.2 Cerrar Turno
**POST** `/api/v1/turnos/{codigoTurno}/cerrar`

**Descripción:** Permite cerrar un turno de caja con las denominaciones finales.

**Cuerpo de la petición:**
```json
[
  {
    "valor": 1,
    "cantidad": 95,
    "monto": 95.00
  },
  {
    "valor": 5,
    "cantidad": 45,
    "monto": 225.00
  },
  {
    "valor": 10,
    "cantidad": 25,
    "monto": 250.00
  },
  {
    "valor": 20,
    "cantidad": 15,
    "monto": 300.00
  },
  {
    "valor": 50,
    "cantidad": 8,
    "monto": 400.00
  },
  {
    "valor": 100,
    "cantidad": 3,
    "monto": 300.00
  }
]
```

**Respuesta exitosa (200):**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d0",
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "codigoTurno": "CAJ01-USU01-20241209",
  "montoInicial": 2050.00,
  "montoFinal": 1570.00,
  "fechaInicio": "2024-12-09T08:00:00",
  "fechaCierre": "2024-12-09T17:00:00",
  "estado": "CERRADO",
  "denominacionesFinales": [...],
  "lastModifiedDate": "2024-12-09T17:00:00",
  "version": 2
}
```

#### 1.3 Obtener Turno por Código
**GET** `/api/v1/turnos/{codigoTurno}`

**Descripción:** Obtiene la información de un turno específico.

**Respuesta exitosa (200):**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d0",
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "codigoTurno": "CAJ01-USU01-20241209",
  "montoInicial": 2050.00,
  "estado": "ABIERTO",
  "fechaInicio": "2024-12-09T08:00:00"
}
```

#### 1.4 Obtener Turnos por Caja
**GET** `/api/v1/turnos/caja/{codigoCaja}`

**Descripción:** Obtiene todos los turnos de una caja específica.

#### 1.5 Obtener Turnos por Cajero
**GET** `/api/v1/turnos/cajero/{codigoCajero}`

**Descripción:** Obtiene todos los turnos de un cajero específico.

#### 1.6 Obtener Turno Abierto
**GET** `/api/v1/turnos/abierto?codigoCaja={codigoCaja}&codigoCajero={codigoCajero}`

**Descripción:** Obtiene el turno abierto para una caja y cajero específicos.

### 2. Transacciones de Turno

#### 2.1 Procesar Depósito
**POST** `/api/v1/transacciones/deposito?codigoTurno={codigoTurno}`

**Descripción:** Permite procesar un depósito en un turno específico.

**Cuerpo de la petición:**
```json
[
  {
    "valor": 20,
    "cantidad": 5,
    "monto": 100.00
  },
  {
    "valor": 50,
    "cantidad": 2,
    "monto": 100.00
  }
]
```

**Respuesta exitosa (201):**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d1",
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "codigoTurno": "CAJ01-USU01-20241209",
  "tipoTransaccion": "DEPOSITO",
  "montoTotal": 200.00,
  "denominaciones": [...],
  "fechaTransaccion": "2024-12-09T10:30:00",
  "estado": "ACTIVO"
}
```

#### 2.2 Procesar Retiro
**POST** `/api/v1/transacciones/retiro?codigoTurno={codigoTurno}`

**Descripción:** Permite procesar un retiro en un turno específico.

**Cuerpo de la petición:**
```json
[
  {
    "valor": 10,
    "cantidad": 3,
    "monto": 30.00
  },
  {
    "valor": 5,
    "cantidad": 4,
    "monto": 20.00
  }
]
```

**Respuesta exitosa (201):**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d2",
  "codigoCaja": "CAJ01",
  "codigoCajero": "USU01",
  "codigoTurno": "CAJ01-USU01-20241209",
  "tipoTransaccion": "AHORRO",
  "montoTotal": 50.00,
  "denominaciones": [...],
  "fechaTransaccion": "2024-12-09T11:15:00",
  "estado": "ACTIVO"
}
```

#### 2.3 Obtener Transacción por ID
**GET** `/api/v1/transacciones/{id}`

**Descripción:** Obtiene la información de una transacción específica.

#### 2.4 Obtener Transacciones por Turno
**GET** `/api/v1/transacciones/turno/{codigoTurno}`

**Descripción:** Obtiene todas las transacciones de un turno específico.

#### 2.5 Obtener Transacciones por Turno y Tipo
**GET** `/api/v1/transacciones/turno/{codigoTurno}/tipo/{tipoTransaccion}`

**Descripción:** Obtiene las transacciones de un turno específico por tipo (DEPOSITO, AHORRO).

#### 2.6 Obtener Transacciones por Caja y Cajero
**GET** `/api/v1/transacciones/caja/{codigoCaja}/cajero/{codigoCajero}`

**Descripción:** Obtiene todas las transacciones de una caja y cajero específicos.

#### 2.7 Obtener Balance del Turno
**GET** `/api/v1/transacciones/turno/{codigoTurno}/balance`

**Descripción:** Obtiene el balance actual del turno (depósitos - retiros).

**Respuesta exitosa (200):**
```json
150.00
```

## Códigos de Error

| Código | Descripción |
|--------|-------------|
| 400 | Datos inválidos o estado inválido del turno |
| 404 | Turno o transacción no encontrado |
| 409 | Ya existe un turno abierto para esta caja y cajero |
| 500 | Error interno del servidor |

## Ejemplos de Uso Completo

### Flujo Completo de un Turno

1. **Abrir Turno:**
```bash
curl -X POST "http://localhost:8080/api/v1/turnos/abrir" \
  -H "Content-Type: application/json" \
  -d '{
    "codigoCaja": "CAJ01",
    "codigoCajero": "USU01",
    "denominacionesIniciales": [
      {"valor": 1, "cantidad": 100, "monto": 100.00},
      {"valor": 5, "cantidad": 50, "monto": 250.00},
      {"valor": 10, "cantidad": 30, "monto": 300.00},
      {"valor": 20, "cantidad": 20, "monto": 400.00},
      {"valor": 50, "cantidad": 10, "monto": 500.00},
      {"valor": 100, "cantidad": 5, "monto": 500.00}
    ]
  }'
```

2. **Procesar Depósito:**
```bash
curl -X POST "http://localhost:8080/api/v1/transacciones/deposito?codigoTurno=CAJ01-USU01-20241209" \
  -H "Content-Type: application/json" \
  -d '[
    {"valor": 20, "cantidad": 5, "monto": 100.00},
    {"valor": 50, "cantidad": 2, "monto": 100.00}
  ]'
```

3. **Procesar Retiro:**
```bash
curl -X POST "http://localhost:8080/api/v1/transacciones/retiro?codigoTurno=CAJ01-USU01-20241209" \
  -H "Content-Type: application/json" \
  -d '[
    {"valor": 10, "cantidad": 3, "monto": 30.00},
    {"valor": 5, "cantidad": 4, "monto": 20.00}
  ]'
```

4. **Consultar Balance:**
```bash
curl -X GET "http://localhost:8080/api/v1/transacciones/turno/CAJ01-USU01-20241209/balance"
```

5. **Cerrar Turno:**
```bash
curl -X POST "http://localhost:8080/api/v1/turnos/CAJ01-USU01-20241209/cerrar" \
  -H "Content-Type: application/json" \
  -d '[
    {"valor": 1, "cantidad": 95, "monto": 95.00},
    {"valor": 5, "cantidad": 45, "monto": 225.00},
    {"valor": 10, "cantidad": 25, "monto": 250.00},
    {"valor": 20, "cantidad": 15, "monto": 300.00},
    {"valor": 50, "cantidad": 8, "monto": 400.00},
    {"valor": 100, "cantidad": 3, "monto": 300.00}
  ]'
```

## Validaciones de Negocio

1. **Apertura de Turno:**
   - No puede existir un turno abierto para la misma caja y cajero
   - Debe registrar denominaciones iniciales
   - El código de turno se genera automáticamente

2. **Transacciones:**
   - Solo se pueden procesar en turnos abiertos
   - Se calcula automáticamente el monto total
   - Se registra la fecha y hora de la transacción

3. **Cierre de Turno:**
   - Solo se puede cerrar un turno abierto
   - Se compara el monto final con el calculado
   - Se genera alerta si hay diferencias

## Estructura del Código

```
src/main/java/com/banquito/core/examen/
├── controller/
│   ├── TurnoCajaController.java
│   └── TransaccionTurnoController.java
├── service/
│   ├── TurnoCajaService.java
│   └── TransaccionTurnoService.java
├── repository/
│   ├── TurnoCajaRepository.java
│   └── TransaccionTurnoRepository.java
├── model/
│   ├── TurnoCaja.java
│   ├── TransaccionTurno.java
│   └── Denominacion.java
└── exception/
    ├── TurnoCajaNotFoundException.java
    ├── TransaccionTurnoNotFoundException.java
    ├── TurnoCajaAlreadyOpenException.java
    └── InvalidTurnoStateException.java
```

## Características Técnicas

- **Base de Datos:** MongoDB con colecciones documentales
- **Documentación:** OpenAPI/Swagger integrado
- **Logging:** SLF4J con logs informativos y de error
- **Validaciones:** Excepciones personalizadas para casos de negocio
- **Transacciones:** Anotaciones @Transactional para operaciones CRUD
- **Inyección de Dependencias:** @Autowired en servicios y controladores
- **API REST:** Endpoints bien estructurados con códigos de respuesta apropiados
