# Demo Jenkins + SonarQube (Java)

Proyecto Maven de ejemplo con pruebas JUnit 5, cobertura con JaCoCo y un pipeline de Jenkins que analiza el código en SonarQube.

## Estructura

```
demo-jenkins-sonar/
├── pom.xml                  # Dependencias y plugins (JUnit 5, Surefire, JaCoCo)
├── Jenkinsfile              # Pipeline: build → tests → SonarQube → Quality Gate
├── docker-compose.yml       # Levanta Jenkins (8080) y SonarQube (9000)
└── src/
    ├── main/java/com/ejemplo/
    │   ├── Calculadora.java
    │   └── Inventario.java
    └── test/java/com/ejemplo/
        ├── CalculadoraTest.java
        └── InventarioTest.java
```

## Requisitos

- Java 17 o superior y Maven (solo para correrlo en tu máquina)
- Docker Desktop
- Una cuenta de GitHub

## Paso 1: probar en local

```bash
mvn verify
```

Deben pasar los 11 tests. El reporte de cobertura queda en `target/site/jacoco/index.html`.

## Paso 2: subir a GitHub

Crea un repositorio vacío llamado `demo-jenkins-sonar` en GitHub y luego:

```bash
git init
git add .
git commit -m "Proyecto inicial con pipeline Jenkins + SonarQube"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/demo-jenkins-sonar.git
git push -u origin main
```

## Paso 3: levantar Jenkins y SonarQube

```bash
docker compose up -d
```

SonarQube tarda un par de minutos en arrancar.

## Paso 4: configurar SonarQube (http://localhost:9000)

1. Entra con `admin` / `admin` y cambia la contraseña.
2. Avatar → **My Account → Security → Generate Token**. Copia el token.
3. **Administration → Configuration → Webhooks → Create**:
   - Name: `jenkins`
   - URL: `http://jenkins:8080/sonarqube-webhook/`

## Paso 5: configurar Jenkins (http://localhost:8080)

1. Contraseña inicial:
   ```bash
   docker compose exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```
2. Instala los plugins sugeridos y crea tu usuario.
3. **Manage Jenkins → Plugins → Available**: instala **SonarQube Scanner**.
4. **Manage Jenkins → Credentials → (global) → Add Credentials**:
   - Kind: *Secret text*, Secret: el token de SonarQube, ID: `sonar-token`
5. **Manage Jenkins → System → SonarQube servers → Add**:
   - Name: `SonarQube`, URL: `http://sonarqube:9000`, token: `sonar-token`
6. **Manage Jenkins → Tools → Maven installations → Add**:
   - Name: `Maven3`, marca *Install automatically*

## Paso 6: crear el pipeline

1. **New Item** → nombre `demo-jenkins-sonar` → tipo **Pipeline**.
2. En la sección *Pipeline*: **Pipeline script from SCM** → SCM: **Git**
   - Repository URL: la URL de tu repositorio
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
3. Guarda y dale **Build Now**.

## Qué deberías ver

- **Jenkins:** las 5 etapas en verde y el resultado de los 11 tests.
- **SonarQube:** el proyecto `Demo Jenkins SonarQube` con su cobertura, bugs, code smells y el Quality Gate en *Passed*.

## Prueba de que la automatización funciona

Cambia un valor esperado en un test (por ejemplo `assertEquals(5, ...)` por `assertEquals(6, ...)` en `CalculadoraTest`), haz commit y push, y vuelve a correr el pipeline: debe quedar en rojo en la etapa *Tests y cobertura*. Revierte el cambio y vuelve a verde.

## Problemas comunes

| Síntoma | Causa probable |
|---|---|
| `No tool named Maven3 found` | El nombre en *Tools* no coincide con el del Jenkinsfile. |
| `SonarQube installation 'SonarQube' not found` | El nombre en *System → SonarQube servers* no coincide. |
| Quality Gate se queda esperando hasta el timeout | Falta el webhook en SonarQube o la URL está mal. |
| SonarQube no arranca | Poca RAM asignada a Docker (dale al menos 4 GB). |
| Jenkins no puede clonar un repo privado | Agrega credenciales de GitHub (usuario + token) en la configuración del job. |
