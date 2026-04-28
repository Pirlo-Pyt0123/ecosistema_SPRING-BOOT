# Instrucciones — Ejecución de Tests en IntelliJ IDEA

## Requisitos previos
- JDK 21 instalado
- IntelliJ IDEA Community (o superior)
- Plugin **Lombok** instalado en IntelliJ
- Annotation Processing habilitado

---

## 1. Habilitar Lombok en IntelliJ

1. `File → Settings → Plugins` → buscar **Lombok** → Install → Reiniciar IntelliJ
2. `File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors`
3. Marcar ✅ **Enable annotation processing**
4. `Build → Rebuild Project`

---

## 2. Orden de levantamiento del ecosistema (para pruebas manuales / Postman)

Levantar cada módulo en este orden desde IntelliJ:

| Orden | Módulo | Puerto |
|-------|--------|--------|
| 1° | `EurekaServerApp` | 8761 |
| 2° | `ms-books-catalogue-main` | 8080 |
| 3° | `MsBookPayments` | 7171 |
| 4° | `CloudGatewayProxy` | 8181 |

> Esperar que cada uno muestre `Started` en consola antes de arrancar el siguiente.

Verificar en `http://localhost:8761` que los microservicios aparezcan registrados.

---

## 3. Ejecutar todos los tests de un módulo

### Opción A — Desde IntelliJ
1. Clic derecho sobre la carpeta `src/test/java` del módulo
2. `Run 'All Tests'`

### Opción B — Desde terminal (recomendado, evita problemas con Lombok)

```bash
# ms-books-catalogue
cd ms-books-catalogue-main
mvn test

# MsBookPayments
cd ../MsBookPayments
mvn test

# GatewayTranscripcionPeticiones
cd ../GatewayTranscripcionPeticiones
mvn test

# EurekaServerApp
cd ../EurekaServerApp
mvn test

# CloudGatewayProxy
cd ../CloudGatewayProxy
mvn test
```

### Ejecutar un test específico
```bash
mvn test -Dtest=LibrosControllerTest
mvn test -Dtest=LibrosServiceImplTest
mvn test -Dtest=BuscadorServiceTest
```

---

## 4. Ver reporte de cobertura en IntelliJ

1. Clic derecho sobre `src/test/java` → **Run 'All Tests' with Coverage**
2. Al terminar, IntelliJ muestra el panel **Coverage** con porcentaje por clase
3. Las líneas verdes = cubiertas, rojas = no cubiertas

### Generar reporte HTML de cobertura (JaCoCo)
Agregar en el `pom.xml` del módulo (dentro de `<plugins>`):
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

Luego ejecutar:
```bash
mvn test
```
El reporte queda en: `target/site/jacoco/index.html`

---

## 5. Importar y usar la colección Postman

1. Abrir **Postman**
2. `Import` → seleccionar `postman/ecosistema-spring-boot.postman_collection.json`
3. Asegurarse que los 4 módulos estén levantados
4. Ejecutar las requests en orden:
   - Health Checks primero (verificar que todo responde)
   - ms-books-catalogue (crear un libro primero, luego buscar/editar/eliminar)
   - MsBookPayments (usar el ID del libro creado para registrar una compra)

---

## 6. Estructura de carpetas de tests

```
ms-books-catalogue-main/src/test/
└── com/unir/ms_books_catalogue/
    ├── controller/
    │   └── LibrosControllerTest.java          (Elmer - 10 tests)
    ├── service/
    │   └── LibrosServiceImplTest.java          (Brayan - 20+ tests)
    └── data/utils/
        ├── SearchCriteriaTest.java             (Brayan)
        ├── SearchStatementTest.java            (Brayan)
        ├── SearchOperationTest.java            (Brayan)
        └── ConstantesTest.java                 (Brayan)

MsBookPayments/src/test/
└── com/g5/relpapel/msbookpayments/MsBookPayments/
    ├── controller/
    │   └── CompraControllerTest.java           (Brayan - 7 tests)
    └── service/
        └── BuscadorServiceTest.java            (Isaac - 10 tests)

GatewayTranscripcionPeticiones/src/test/
└── com/g5/relpapel/cloudgateway/resolucionpeticiones/
    ├── decorator/
    │   └── GetAndPostRequestDecoratorTest.java (Isaac - 11 tests)
    └── utils/
        └── RequestBodyExtractorTest.java       (Isaac - 5 tests)

EurekaServerApp/src/test/
└── EurekaServerAppApplicationTests.java        (Elmer - 2 tests)

CloudGatewayProxy/src/test/
└── CloudGatewayProxyApplicationTests.java      (Elmer - 2 tests)
```

---

## 7. Convención de commits usada en el proyecto

```
[MÓDULO][TIPO]: descripción en infinitivo

Módulos:  CATALOGUE | PAYMENTS | GATEWAY | EUREKA | CLOUDGATEWAY | GENERAL
Tipos:    test | feat | fix | docs | setup | postman
```

Ejemplos:
```
[CATALOGUE][test]: agregar tests unitarios para LibrosController
[PAYMENTS][test]: mockear RestTemplate en BuscadorService
[GENERAL][setup]: agregar colección Postman con todos los endpoints
```