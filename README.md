# AppiumBDD

Framework de automatización de pruebas móviles **Android** con enfoque **BDD** para la app de demo **Swag Labs** (Sauce Labs), construido con **Java 17, Appium, Cucumber (Gherkin), JUnit Platform y Maven**.

![Java](https://img.shields.io/badge/Java-17-orange)
![Appium Java Client](https://img.shields.io/badge/Appium%20Java%20Client-10.1.1-purple)
![Cucumber](https://img.shields.io/badge/Cucumber-7.34.9-brightgreen)
![JUnit](https://img.shields.io/badge/JUnit%20Platform-6.1.3-red)
![License](https://img.shields.io/badge/License-MIT-green)

## Resumen ejecutivo

Proyecto de portafolio que automatiza 15 escenarios Gherkin (18 ejecuciones contando cada fila de `Examples`) sobre login, catálogo, detalle de producto, carrito, formulario de checkout, menú lateral y deeplinks. Muestra un framework mantenible: escenarios en español legibles por negocio, Page Object Model, gestos táctiles W3C, esperas explícitas, evidencia automática en fallos, logs por escenario y reporte HTML de Cucumber.

| | |
|---|---|
| **Plataforma** | Android (UiAutomator2), emulador o dispositivo físico |
| **App bajo prueba** | Swag Labs (`com.swaglabsmobileapp`), APK incluido |
| **Features / escenarios** | 7 features, 15 escenarios definidos, 18 ejecuciones; tags `@regression` (los 15) y `@smoke` (8 escenarios, 9 ejecuciones) |
| **Última ejecución completa verificada** | **18 de 18 pasados**, 0 fallidos, 0 omitidos, 364,5 s (≈ 6 min), suite `@regression`, emulador API 34 x86_64 en Windows 11, Appium 3.7.0, **2026-10-09, commit `602b443`** |
| **Ejecuciones parciales posteriores** | `@smoke` 9/9 (205,9 s), `Login.feature` 5/5, filtro por nombre y "Your Information" 10 veces seguidas (30/30), todas en la misma fecha y máquina |
| **CI** | **No existe pipeline.** Ver [Integración continua](#integración-continua) |

> **Alcance honesto:** los resultados provienen de una sola máquina y un solo emulador. Después de la última suite completa (commit `602b443`) solo cambiaron el README y la configuración del plugin de reporte (commit `01df0d2`), y esos cambios se comprobaron con ejecuciones parciales, no repitiendo la suite completa. Además, el trabajo de mejora vive en la rama `feature/phased-improvements`; `main` no lo contiene hasta que se integre. Ver [Limitaciones conocidas](#limitaciones-conocidas).

## Capturas

<!-- PENDIENTE: no hay capturas ni reportes versionados en el repositorio. Añade las reales con el formato
     ![descripción](docs/images/archivo.png) y quita este comentario:
     - docs/images/login.png            -> pantalla de login de la app en el emulador (pendiente)
     - docs/images/cucumber-report.png  -> resumen de target/cucumber-html-reports/overview-features.html (pendiente)
     - docs/images/ejecucion.gif        -> GIF de una ejecución en el emulador (pendiente) -->

*(Pendiente: todavía no hay capturas del emulador ni del reporte en el repositorio.)*

## Qué demuestra este proyecto

| Habilidad | Dónde verlo |
|---|---|
| **BDD con Gherkin** | [`features/`](src/test/resources/features): `Background`, `Scenario Outline` con `Examples`, DataTables y parámetros `{int}`, `{string}` y `{double}`; tags `@regression` y `@smoke`. |
| **Page Object Model** | [`pages/`](src/test/java/pages) (7 pantallas) sobre [`BasePage`](src/test/java/utilities/BasePage.java): locators privados, esperas y verificaciones encapsuladas; los steps no tocan locators. |
| **Flujos reutilizables** | [`CommonFlows`](src/test/java/utilities/CommonFlows.java): navegación hasta cada pantalla sin repetir el login en los features. |
| **Hooks y evidencia en fallos** | [`Hooks`](src/test/java/hooks/Hooks.java) y [`ReportAttachments`](src/test/java/utilities/ReportAttachments.java): screenshot, page source, logcat y log del escenario, adjuntos al reporte. |
| **Observabilidad** | [`StepLogger`](src/test/java/hooks/StepLogger.java) (log por step) y [`log4j2.xml`](src/test/resources/log4j2.xml) (un log global y uno por escenario). |
| **Gestos táctiles W3C** | [`Gestures`](src/test/java/utilities/Gestures.java): tap, long tap, doble tap, drag & drop y swipe con `PointerInput` y `Sequence`. |
| **Deeplinks** | [`DeepLinks`](src/test/java/utilities/DeepLinks.java) y [`DeepLinks.feature`](src/test/resources/features/DeepLinks.feature): navegación directa con `swaglabs://`. |
| **Datos externos** | [`DataGiver`](src/test/java/data/DataGiver.java) y [`JsonReader`](src/test/java/data/JsonReader.java) leen [`credenciales.json`](src/test/resources/data/credenciales.json) con Jackson. |
| **Aislamiento entre escenarios** | Driver en `ThreadLocal` ([`DriverProvider`](src/test/java/utilities/DriverProvider.java)) y un driver nuevo por escenario. |
| **Configuración externa** | [`Config`](src/test/java/utilities/Config.java) y [`config.properties`](src/test/resources/config.properties), sobrescribibles con `-D` o variable de entorno. |
| **Ejecución local** | [`DriverManager`](src/test/java/utilities/DriverManager.java) crea un `AndroidDriver` contra Appium en `127.0.0.1:4723`. |
| **Ejecución remota** | **No implementada.** Con `JOB_NAME` definida, `buildRemoteDriver()` lanza una `UnsupportedOperationException` con un mensaje claro. |

## Arquitectura

```
Feature (.feature)  ──►  Steps (steps/)  ──►  CommonFlows  ──►  Pages (pages/)  ──►  BasePage
                              │                                      │                  │
                              └──────────── usan ────────────────────┘                  │ usa
                                                                                        ▼
                                                    Gestures · DeepLinks · Logs · Config
Hooks (@BeforeAll / @Before / @After / @AfterAll) ──► DriverManager ──► DriverProvider (ThreadLocal<AndroidDriver>)
      │                                                                          ▲
      └─► FileManager (disco) · ReportAttachments (reporte)  ◄── evidencia en fallos ┘
StepLogger (plugin de Cucumber) ──► log4j2 ──► target/logs
Datos: data/ (JsonReader, DataGiver) ──► models/ (Credential, CredentialJson)
```

| Paquete | Rol |
|---|---|
| `pages` | Un Page Object por pantalla: `LoginPage`, `ShoppingPage`, `ItemDetailPage`, `YourCartPage`, `YourInformationPage`, `TopBar` y `BurgerMenuPage`. |
| `steps` | Una clase de step definitions por funcionalidad (`*StepDefinition`); traducen el Gherkin a llamadas a las páginas. |
| `hooks` | `Hooks` (ciclo de vida de cada escenario) y `StepLogger` (plugin de log por step). |
| `utilities` | `BasePage`, `CommonFlows`, `Config`, `DriverManager`/`DriverProvider`, `Gestures`, `DeepLinks`, `FileManager`, `ReportAttachments` y `Logs`. |
| `data` | `DataGiver` (credenciales) y `JsonReader`. |
| `models` | `Credential` y `CredentialJson`. |
| `principal` | `RunTests`, una clase `@Suite` que **no se usa para ejecutar** (falta `junit-platform-suite-engine`; Cucumber corre directamente como motor de JUnit Platform). |

No existe un paquete `exceptions`: los errores de configuración usan excepciones estándar de Java con mensaje descriptivo.

### Estructura de carpetas

```
AppiumBDD/
├── pom.xml                      # Dependencias y plugins
├── mvnw / mvnw.cmd              # Maven Wrapper (Maven 3.9.11)
├── runSuite.sh                  # Ejecuta los escenarios @regression
├── openReport.sh                # Abre el reporte HTML (Windows, macOS o Linux)
├── .gitattributes               # Finales de línea (LF para .sh y mvnw)
└── src/
    └── test/
        ├── java/
        │   ├── data/  hooks/  models/
        │   ├── pages/  principal/  steps/  utilities/
        └── resources/
            ├── apk/sauceLabs.apk
            ├── data/credenciales.json
            ├── features/        # Login, Shopping, ItemDetail, YourCart, YourInformation, BurgerMenu, DeepLinks
            ├── config.properties
            ├── junit-platform.properties
            └── log4j2.xml
```

`src/main` no contiene código: todo vive en `src/test`.

## Decisiones técnicas

| Decisión | Por qué |
|---|---|
| **Cucumber sobre JUnit Platform** (`cucumber-junit-platform-engine`) | Se integra con Surefire sin runner propio y permite filtrar por tag con `-Dgroups`. A cambio, `RunTests` queda sin función y Surefire cuenta como "Skipped" los escenarios filtrados por nombre. |
| **Driver en `ThreadLocal` y uno por escenario** | Cada escenario arranca limpio y el diseño no impide ejecutar en paralelo. Coste: cada escenario reinstala y abre la app (en esta máquina, unos 20 s por escenario en promedio). |
| **`find` espera la presencia del elemento, no su visibilidad** | Así `isDisplayed()` en las verificaciones falla como una aserción clara y no como un timeout. Las acciones (`click`, `type`) sí esperan que el elemento sea clickable. |
| **Esperas explícitas de 20 s por defecto, sin `sleep`** | El emulador corre en un equipo con poca memoria libre; un tiempo generoso evita falsos negativos y esperar una condición real es más estable que una pausa fija. Se configura con `timeout.default`. |
| **Concesión: pausas fijas dentro de los gestos** | Los gestos de `Gestures` mantienen pausas de 1 a 3,5 s para ser fiables en el emulador lento. Son lentas pero no se han medido ni optimizado. |
| **Evidencia en `@After`, sin lanzar excepciones** | Un error al guardar o adjuntar evidencia se registra y no oculta el fallo real del escenario. Si el driver no existe, solo se adjunta el log del escenario. |
| **Un log por escenario con `RoutingAppender`** | Permite diagnosticar un fallo sin volver a ejecutar; el log se adjunta al reporte. |
| **`testFailureIgnore=true` + `checkBuildResult=true`** | Surefire no se detiene para que el plugin de reporte genere el HTML; luego `checkBuildResult` hace fallar el build si hay steps fallidos. Tiene un hueco conocido (ver [Limitaciones](#limitaciones-conocidas)). |
| **Jackson 2 declarado explícitamente** | El código usa `com.fasterxml.jackson`; se evitó mantener a la vez Jackson 3, sin uso. |
| **Selenium en tres módulos** (`api`, `remote-driver`, `support`) | `selenium-java` arrastraba drivers de navegador que Appium no usa. `java-client` acepta Selenium en el rango `[4.42.0, 5.0)`. |
| **Surefire fijado en 3.2.5** | Con 3.5.4 el reporte mostraba `Tests run: 0` pese a que los escenarios corrían. |
| **Dependencias en scope `test`** | `src/main` está vacío; todo el código es de pruebas. |

## Tecnologías y versiones

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje |
| Maven (wrapper `mvnw`) | 3.9.11 | Build y ejecución |
| Appium Java Client | 10.1.1 | `AndroidDriver`, `AppiumBy` |
| Selenium (`selenium-api`, `selenium-remote-driver`, `selenium-support`) | 4.49.0 (directas) | `WebDriverWait`, `ExpectedConditions`, `PointerInput`/`Sequence` |
| Cucumber (`cucumber-java`, `cucumber-junit-platform-engine`) | 7.34.9 | Gherkin y steps |
| JUnit Jupiter engine y `junit-platform-suite-api` | 6.1.3 | Motor de JUnit Platform y `Assertions.assertAll` |
| `cucumber-reporting` | 5.9.0 | Librería del reporte HTML |
| `maven-cucumber-reporting` | 5.8.1 | Genera el reporte en la fase `test` |
| Maven Surefire | 3.2.5 | Ejecución (`testFailureIgnore=true`) |
| Log4j2 (core + api) | 2.26.1 | Logging (logger `AUTOMATION`) |
| Jackson Databind | 2.21.2 | Lectura del JSON de credenciales |
| jsoup | 1.20.1 | Formato del page source |
| commons-io | 2.18.0 | Utilidades de archivos |
| Appium server | 3.7.0 (externo, no está en el `pom.xml`) | Servidor al que se conecta el driver |

**Transitivas relevantes** (según `mvn dependency:tree`): `cucumber-core` (de `cucumber-junit-platform-engine`), `junit-platform-engine` (de `junit-jupiter-engine`), `jackson-datatype-jsr310` 2.18.3, `slf4j-api` 2.0.17 y `commons-lang3` (de `cucumber-reporting`), `guava` 33.7.1 y OpenTelemetry (de `selenium-remote-driver`) y Gson (de `java-client`).

## Requisitos previos

1. **JDK 17** (`java -version`) con `JAVA_HOME` configurado.
2. **Maven** (opcional: se incluye `mvnw`).
3. **Node.js** y **Appium 3.x** con el driver UiAutomator2 (probado con 3.7.0):
   ```bash
   npm install -g appium@3.7.0
   appium driver install uiautomator2
   ```
4. **Android SDK** con `adb` y un **emulador (AVD)** o dispositivo físico, con `ANDROID_HOME` configurada y `platform-tools` en el `PATH`. Probado con un emulador API 34 x86_64 (`emulator-5554`).
5. **APK** en `src/test/resources/apk/sauceLabs.apk` (incluido). Ver [Sobre el APK](#sobre-el-apk).
6. **Servidor Appium en ejecución** antes de lanzar los tests (el proyecto no lo inicia):
   ```bash
   appium
   ```
7. **Verificación del entorno:**
   ```bash
   adb devices                                  # el dispositivo debe aparecer como "device"
   curl http://127.0.0.1:4723/status            # debe responder "ready": true
   ```

> Los comandos de instalación son los estándar de Appium; no se ejecutaron desde cero en esta revisión. Tampoco se probó clonar el repositorio en un equipo limpio.

### Sobre el APK

`sauceLabs.apk` (25 734 673 bytes) es la app de demo **Swag Labs** (paquete `com.swaglabsmobileapp`) de Sauce Labs. Es **el mismo archivo** (mismo SHA-256) que usa el proyecto hermano `appium-android-swaglabs-testng`.

> **Aviso:** no se ha verificado de dónde salió este binario ni su versión. El README del proyecto hermano indica que el repositorio de código [saucelabs/sample-app-mobile](https://github.com/saucelabs/sample-app-mobile) declara licencia MIT; ese dato **no se ha vuelto a comprobar aquí**, ni que este binario sea un build oficial o que su redistribución esté permitida. Si prefieres no depender de él, descarga el APK desde ese repositorio y colócalo en `src/test/resources/apk/sauceLabs.apk`.

SHA-256 del archivo incluido:

```
f1793404b21e2ea29c8e203ebc1b1fd16c685d701cfb405c243bfbb520e9bce6
```

## Configuración

### Capabilities (ejecución local)
Definidas en [`DriverManager.getDesiredLocalCapabilities()`](src/test/java/utilities/DriverManager.java):

| Capability | Valor |
|---|---|
| `platformName` | `Android` |
| `appium:automationName` | `UiAutomator2` |
| `appium:app` | Ruta absoluta calculada a partir de `app.path` (por defecto `src/test/resources/apk/sauceLabs.apk`) |
| `appium:appWaitActivity` | `com.swaglabsmobileapp.MainActivity` |
| `appium:autoGrantPermissions` | `true` |
| `appium:adbExecTimeout` | `60000` ms |
| `appium:uiautomator2ServerLaunchTimeout` | `90000` ms |

No se define `deviceName` ni `udid`: Appium usa el dispositivo o emulador conectado.

### Ejecución local vs remota (`JOB_NAME`)
- **`JOB_NAME` no definida** → driver local contra `http://127.0.0.1:4723/`.
- **`JOB_NAME` definida** → se invoca `buildRemoteDriver()`, que lanza `UnsupportedOperationException`. La ejecución remota no está implementada.

### Valores configurables
Los valores viven en [`config.properties`](src/test/resources/config.properties). Cada clave se puede sobrescribir con una propiedad de sistema (`-Dclave=valor`) o con una variable de entorno (clave en mayúsculas y `.` cambiado por `_`). Prioridad: propiedad de sistema, variable de entorno, archivo.

| Clave | Por defecto | Uso |
|---|---|---|
| `appium.url` | `http://127.0.0.1:4723/` | Servidor Appium |
| `app.path` | `src/test/resources/apk/sauceLabs.apk` | APK a instalar |
| `app.wait.activity` | `com.swaglabsmobileapp.MainActivity` | Actividad esperada al arrancar |
| `timeout.default` | `20` | Espera por defecto de elementos y pantallas (s) |
| `timeout.adb.exec` | `60000` | Timeout de comandos adb (ms) |
| `timeout.uiautomator2.launch` | `90000` | Arranque del servidor UiAutomator2 (ms) |
| `evidence.screenshots.dir` | `target/evidence/screenshots/` | Capturas de fallos |
| `evidence.pagesource.dir` | `target/evidence/pageSource/` | Page source de fallos |
| `credentials.file` | `src/test/resources/data/credenciales.json` | Datos de login |
| `log.dir` | `target/logs` | Carpeta de logs (debe coincidir con `logDir` de `log4j2.xml`) |
| `logcat.max.lines` | `300` | Líneas del logcat que se adjuntan en un fallo |

Ejemplo: `./mvnw test -Dappium.url=http://192.168.1.50:4723/ -Dtimeout.default=30`

### Datos de prueba
[`credenciales.json`](src/test/resources/data/credenciales.json) contiene las claves `valid`, `locked` e `invalid`. Son las **credenciales de demo públicas de Swag Labs** (`standard_user`, `locked_out_user`, contraseña `secret_sauce`); el repositorio no contiene ningún secreto real. Los flujos reutilizables usan `DataGiver.getValidCredentials()`; el `Scenario Outline` de [`Login.feature`](src/test/resources/features/Login.feature) escribe sus credenciales directamente en la tabla `Examples`.

### Cucumber y JUnit Platform
[`junit-platform.properties`](src/test/resources/junit-platform.properties):

```properties
cucumber.features=src/test/resources/features
cucumber.glue=hooks,steps
cucumber.plugin=pretty,json:target/cucumber/report.json,hooks.StepLogger
```

La estrategia de nombres `long` de Cucumber se define en el `pom.xml` (configuración de Surefire).

## Cómo ejecutar

> Con el emulador iniciado y Appium corriendo. Ejecuta desde la carpeta que contiene `pom.xml`. En Windows usa Git Bash para los `.sh` (o `mvnw.cmd`).

| Objetivo | Comando | Verificado |
|---|---|---|
| Suite completa (`@regression`) | `./runSuite.sh` (equivale a `./mvnw clean test -Dgroups="regression"`) | Sí: 18/18 |
| Solo `@smoke` | `./mvnw clean test -Dgroups="smoke"` | Sí: 9/9 |
| Un feature | `./mvnw test -Dcucumber.features=src/test/resources/features/Login.feature` | Sí: 5/5 |
| Un escenario por nombre | `./mvnw test -Dcucumber.filter.name="Logout"` | Sí: se ejecuta 1 y Surefire cuenta los demás como "Skipped" |
| Abrir el reporte HTML | `./openReport.sh` | No ejecutado (abre el navegador) |

El filtro por tag usa `-Dgroups` (los tags de Cucumber se exponen como tags de JUnit, sin la `@`).

**Desde IntelliJ IDEA** (no verificado): ejecuta el archivo `.feature` con el glue `hooks steps` y el directorio de trabajo en la raíz del proyecto, porque las rutas de `config.properties` son relativas. El log por step (`hooks.StepLogger`) se registra mediante `cucumber.plugin`; si ejecutas con el runner propio de IntelliJ puede hacer falta añadir `--plugin hooks.StepLogger` a los argumentos del programa.

### Resultado del build
- Si un escenario falla, el build termina en **`BUILD FAILURE` con código de salida 1 después de generar el reporte** (verificado con un escenario fallido a propósito).
- Si todos pasan, termina en `BUILD SUCCESS` con código 0 (verificado con `BurgerMenu.feature`).
- No cambies `testFailureIgnore` a `false`: Maven se detendría antes del plugin de reporte y no habría HTML justo cuando hay fallos.
- **Hueco conocido:** si el hook `@Before` falla (por ejemplo, Appium caído), los steps quedan como `skipped` y el build actual puede terminar en `BUILD SUCCESS`. Ver [Limitaciones conocidas](#limitaciones-conocidas).

## Reportes y evidencias

Todo se genera bajo `target/` (se limpia con `mvn clean`):

| Qué | Dónde |
|---|---|
| Reporte HTML | `target/cucumber-html-reports/overview-features.html` |
| JSON de Cucumber | `target/cucumber/report.json` |
| Log global | `target/logs/automation.log` |
| Log por escenario | `target/logs/scenarios/<escenario>_L<línea>.log` |
| Screenshot y page source de fallos | `target/evidence/screenshots/` y `target/evidence/pageSource/` |

Cuando un escenario falla (`FAILED` o `SKIPPED`), el reporte lleva adjuntos: **screenshot, page source, logcat del dispositivo (últimas 300 líneas) y el log del escenario**. Si el driver no llega a crearse, se adjunta igualmente el log del escenario. Los escenarios que pasan no generan evidencia. El nombre de cada archivo es el nombre del escenario más su línea en el feature, para que las filas de un `Scenario Outline` no se pisen.

El log de cada escenario incluye cada step con su estado y duración, los elementos buscados, los timeouts y el stack trace del error. El texto escrito en los campos **no** se registra (solo su longitud), por si es una contraseña.

## Integración continua

**No existe pipeline de CI.** La carpeta `.github/` solo contiene scripts de una herramienta de actualización de Java, no workflows de GitHub Actions. Los tests necesitan un emulador Android y un servidor Appium, que un runner de GitHub Actions no tiene sin configuración adicional; hoy se ejecutan solo en local.

## Cobertura de pruebas

| Feature | Escenario | Cobertura | Técnica Gherkin | Tags |
|---|---|---|---|---|
| [`Login`](src/test/resources/features/Login.feature) | Credenciales (2 filas) | Mensajes de error con usuario bloqueado y credenciales inválidas | `Background`, `Scenario Outline`, `{string}` | regression, smoke |
| | Verificar la UI de la página de Login | Elementos de la pantalla de login | `Background` | regression, smoke |
| | Usuario loguea haciendo tap | Login tocando el label `standard_user` (gesto tap) | `Background` | regression |
| | Verificar label de credenciales | Labels de usuarios y contraseña visibles tras un swipe vertical | `Background` | regression |
| [`Shopping`](src/test/resources/features/Shopping.feature) | Verificar la UI de la página de Shopping | UI de la pantalla de productos | `Background` | regression, smoke |
| | Agregar al carrito arrastrando | Drag & drop de 3 productos y validación del contador | `Background`, DataTable, `{int}` | regression |
| [`ItemDetail`](src/test/resources/features/ItemDetail.feature) | Verificar la UI de la página de Item Detail | UI del detalle y botón Add to cart tras hacer scroll | `Background`, `{int}` | regression, smoke |
| | Botón Back to Products | Vuelve a Shopping con el botón de la app | `Background` | regression, smoke |
| | Regresar con back | Vuelve a Shopping con el botón *Back* del sistema | `Background` | regression |
| [`YourCart`](src/test/resources/features/YourCart.feature) | Eliminar item usando swipe | Swipe horizontal, botón Delete y contador del carrito | `Background`, DataTable, `{int}` | regression, smoke |
| [`YourInformation`](src/test/resources/features/YourInformation.feature) | Mensajes de Error (3 filas) | Errores por nombre, apellido o código postal vacíos | `Background`, DataTable, `Scenario Outline`, `{string}` | regression |
| [`BurgerMenu`](src/test/resources/features/BurgerMenu.feature) | Logout | Cerrar sesión desde el menú | `Background` | regression, smoke |
| | Cerrar Burger Menu | Cerrar el menú con la X | `Background` | regression, smoke |
| [`DeepLinks`](src/test/resources/features/DeepLinks.feature) | Item Detail Deeplinks | Deeplink al detalle del item 0, nombre y precio | `{int}`, `{string}`, `{double}` | regression |
| | Shopping Deeplinks | Deeplink a Shopping con la lista `"0,2"` y contador del carrito | `{string}`, `{int}` | regression |

Total: **15 escenarios definidos, 18 ejecuciones** contando las filas de `Examples`. Con `@smoke`: 8 escenarios (9 ejecuciones). Los steps están en [`steps/`](src/test/java/steps) (29 anotaciones) y varios se reutilizan entre features (por ejemplo "Se debe redirigir a la pagina de Shopping").

## Limitaciones conocidas

Se listan con honestidad lo que **no** está resuelto o verificado:

- **El build puede quedar en verde con la infraestructura caída.** Observado el 2026-10-09: con Appium apagado, los 18 escenarios fallaron en el hook `@Before` y aun así el build terminó con `BUILD SUCCESS` y código 0, porque `checkBuildResult` solo cuenta steps `failed` y aquí quedan `skipped`. Se probó que activar `treatSkippedAsFailed`, `treatPendingAsFailed`, `treatUndefinedAsFailed` y `treatScenariosAsFailed` en el plugin hace que ese caso termine con código 1, pero **ese cambio no está aplicado** en el `pom.xml` y no se comprobó que una suite sin fallos siga terminando en verde con esos parámetros.
- **No hay CI:** no hay evidencia automática de que los tests pasan fuera de la máquina del autor.
- **Resultados de una sola máquina y un solo emulador.** Se corrió "Your Information" 10 veces seguidas (30/30), pero eso no demuestra ausencia de intermitencia.
- **Escenario intermitente histórico no reproducido:** "Your Information" (*Last Name is required*) fallaba de forma intermitente antes de las mejoras. No se logró reproducir después; no está confirmada la causa ni que esté resuelto.
- **Ejecución remota no implementada.**
- **`addToCartDrag(List<Integer>)` ignora los índices de la tabla:** solo cuenta cuántas filas hay y arrastra los primeros items visibles ([`ShoppingPage`](src/test/java/pages/ShoppingPage.java)). Los features indican los índices por legibilidad.
- **Rendimiento:** un driver nuevo por escenario y pausas fijas en `Gestures`. Las medidas de tiempo varían con la carga del equipo (entre 336 y 392 s en las suites completas de las distintas fases, 470 s antes de las mejoras).
- **Datos de login duplicados:** `Login.feature` escribe credenciales a mano (con `hello world`), mientras `credenciales.json` tiene `hello_world` y `secret_passsword` (typo); `getLockedCredentials()` y `getInvalidCredentials()` no se usan.
- **`LoginPage.verifyLabels()`** hace un scroll dentro de una verificación.
- **`RunTests`** y el paquete `principal` no se usan para ejecutar.
- **Sin análisis de vulnerabilidades** de dependencias. Solo se retiraron las que no se usaban (entre ellas `snakeyaml 1.23`, vía `javafaker`); `jackson-datatype-jsr310` queda en 2.18.3 por una dependencia transitiva.
- **Ejecución desde IntelliJ, clonar en un equipo limpio e instalar Appium desde cero:** no verificados.
- **Solo Android y una app de demo.**
- **APK:** procedencia exacta y licencia de redistribución no verificadas (ver [Sobre el APK](#sobre-el-apk)).

### Medidas de estabilidad implementadas

- **Esperas explícitas** en `find`, `findAll`, `click` y `type` (20 s por defecto, configurable) y espera real tras cada arrastre en lugar de un `sleep`.
- **Tiempos de capabilities generosos** para emuladores lentos: `adbExecTimeout` de 60 s y `uiautomator2ServerLaunchTimeout` de 90 s.
- **Sin estado compartido:** driver en `ThreadLocal` que se cierra y se libera siempre en el `finally` del `@After`.
- **Evidencia que nunca oculta el error real:** los fallos al capturar o adjuntar se registran sin lanzar excepciones.
- **Logs de diagnóstico:** cada timeout deja registrado qué se esperaba y cuántos segundos.

### Historial de fallos corregidos

Solo se incluyen cambios comprobables en el historial de git de la rama `feature/phased-improvements`:

| Escenario o componente | Causa | Corrección | Verificación |
|---|---|---|---|
| Evidencia de varios escenarios fallidos | `FileManager.getPageSource` usaba `mkdir()` y solo escribía el archivo si creaba la carpeta, así que solo se guardaba la primera falla de una ejecución (commit `123ba35`) | `mkdirs` y escritura siempre | Con 2 fallos a propósito se guardaron los 2 XML |
| `@After` con el driver sin crear | Intentaba capturar evidencia con un driver `null` y ocultaba el error real (commit `123ba35`) | Comprobación de driver y cierre en `finally` | Con Appium inalcanzable no apareció ningún `NullPointerException` |
| Filas de un `Scenario Outline` | Las evidencias se nombraban solo con el nombre del escenario y se pisaban entre filas (commit `602b443`) | Nombre más línea del feature (`Mensajes_de_Error_L14`) | 18 logs distintos en una suite completa |
| `Your Cart`: eliminar un item | `deleteItemFromList(index)` hacía swipe en el item `index` pero pulsaba el primer botón Delete de la pantalla (commit `58ac469`) | Se toca el botón de la misma fila | Un escenario temporal con el índice 1 pasó. No se probó que el código anterior fallara en ese caso |
| Build con escenarios fallidos | El build terminaba en verde aunque fallara un escenario (commit `01df0d2`) | `checkBuildResult=true` tras generar el reporte | Un escenario fallido dio código 1 con reporte; `BurgerMenu.feature` dio código 0. Ver el hueco abierto arriba |
| Esperas del mensaje de error de login | Esperaba solo 3 s (commit `123ba35`) | 20 s por defecto | Cambio preventivo: no se observó un fallo causado por esto |

## Solución de problemas

La columna **Origen** indica si el problema se observó durante esta revisión o procede de lo que reporta el autor del proyecto.

| Síntoma | Qué hacer | Origen |
|---|---|---|
| `SessionNotCreatedException ... Connection error (POST http://127.0.0.1:4723/session)` | Appium no está corriendo o la URL es otra. Inicia `appium` y comprueba `.../status`; si usas otro host, `-Dappium.url=...` | Observado |
| `BUILD FAILURE` con `Error Found:: BUILD FAILED - Check Report For Details` | Hay al menos un escenario fallido: abre el reporte (`./openReport.sh`) y el log del escenario | Observado |
| `Tests run: 0` pese a que los escenarios corren | Surefire 3.5.x no cuenta bien el motor de Cucumber con JUnit 6. Mantén Surefire en 3.2.5 | Observado (con 3.5.4) |
| `TimeoutException ... esperando` | El elemento no apareció en el tiempo configurado. Revisa el log y el page source adjuntos; sube `-Dtimeout.default=30` si es solo lentitud | Observado en pruebas de fallo |
| `BUILD SUCCESS` con todos los escenarios en error | Es el hueco descrito en [Limitaciones](#limitaciones-conocidas): comprueba que Appium y el emulador estén activos antes de ejecutar | Observado |
| Puerto 4723 ocupado | Windows: `netstat -ano \| findstr :4723` y `taskkill /PID <pid> /F`; o inicia Appium en otro puerto y usa `-Dappium.url` | Reportado por el autor, no reproducido |
| Timeout de adb (por ejemplo en `pm clear`) | Sube `timeout.adb.exec`; si persiste, `adb kill-server && adb start-server` y reinicia el emulador | Reportado por el autor, no reproducido |
| `instrumentation process is not running` (servidor UiAutomator2 caído) | Reinicia Appium y el emulador; si persiste, desinstala del dispositivo `io.appium.uiautomator2.server` y `io.appium.uiautomator2.server.test` | Reportado por el autor, no reproducido |
| Emulador lento por falta de memoria | Cierra aplicaciones, ajusta la RAM del AVD y sube `timeout.default` y `timeout.uiautomator2.launch` | Reportado por el autor, no reproducido |
| Emulador con `Settings isn't responding` | Cierra el diálogo o reinicia el AVD con *Cold Boot Now* | Reportado por el autor, no reproducido |
| Steps "undefined" | Comprueba `cucumber.glue=hooks,steps`, que el package de la clase de steps exista, que el proyecto compile (`./mvnw test-compile`) y que el texto sea idéntico al del feature | Reportado por el autor, no reproducido |
| `ClassNotFoundException` tras mezclar Maven e IntelliJ | `./mvnw clean test-compile` y, en IntelliJ, *Rebuild Project* | Reportado por el autor, no reproducido |
| No se encuentra `adb` | Añade `platform-tools` al `PATH` o configura `ANDROID_HOME` | Genérico, no reproducido |
| Rutas que no existen o `config.properties` no encontrado | Ejecuta desde la raíz del proyecto (o fija el directorio de trabajo en IntelliJ) | Genérico, no reproducido |
| `bad interpreter` o `\r: command not found` en un `.sh` | El `.gitattributes` fuerza LF en `.sh` y `mvnw`; vuelve a clonar o ejecuta `git add --renormalize .` | Preventivo, no observado |
| No aparece `target/evidence` | Es lo esperado si no hubo fallos | Observado |

## Buenas prácticas

- Un Page Object por pantalla; los steps **no** usan locators directamente.
- Los locators se declaran como `private final By` al inicio de la página.
- Prefiere `AppiumBy.accessibilityId` (la app expone ids `test-*`); evita XPath y los locators por índice cuando se pueda.
- Usa los métodos de `BasePage` (`find`, `findAll`, `click`, `type`, `verifyDisplayed`, `verifyErrorMessage`): todos esperan antes de actuar. No uses `sleep`.
- El texto de cada anotación (`@Given`, `@When`, `@Then`) debe ser **idéntico** al del `.feature`.
- Reutiliza los steps existentes en vez de duplicarlos: un step como "Se debe redirigir a la pagina de Shopping" sirve a varios features.
- Reutiliza los flujos de navegación en `CommonFlows`.
- Etiqueta cada escenario con `@regression` y, si es crítico, `@smoke`.
- Nombres de código en inglés; mensajes de log y Gherkin en español. Los mensajes de commit, en inglés.

## Cómo extender el proyecto

### Agregar una nueva página

1. Crea `src/test/java/pages/CheckoutOverviewPage.java` extendiendo `BasePage`.
2. Declara los locators e implementa `waitPageToLoad()` y `verifyPage()`:
   ```java
   public class CheckoutOverviewPage extends BasePage {
       private final By finishButton = AppiumBy.accessibilityId("test-FINISH");

       @Override
       public void waitPageToLoad() {
           waitPage(finishButton);
       }

       @Override
       public void verifyPage() {
           Logs.info("Verificando la pagina de checkout overview");
           verifyDisplayed(finishButton);
       }

       public void clickFinish() {
           Logs.info("Haciendo click en finish");
           click(finishButton);
       }
   }
   ```

### Agregar un flujo en `CommonFlows`

Si varios escenarios necesitan llegar a esa pantalla, reutiliza los flujos existentes:

```java
public void goToCheckoutOverviewPage(List<Integer> itemListAdd) {
    goToYourInformationPage(itemListAdd);
    // ...completar el formulario y continuar...
    new CheckoutOverviewPage().waitPageToLoad();
}
```

### Agregar un feature con su clase de steps

1. Crea `src/test/resources/features/CheckoutOverview.feature`:
   ```gherkin
   Feature: Checkout Overview

     Background: Precondicion del Feature
       Given El usuario navega a Checkout Overview agregando al carrito items con indices:
         | 0 |

     @regression @smoke
     Scenario: Finalizar la compra
       When El usuario hace click en finish
       Then Se debe mostrar la confirmacion de la compra
   ```
2. Crea la clase de steps en el paquete `steps` (el glue `hooks,steps` la descubre sola):
   ```java
   public class CheckoutOverviewStepDefinition {

       private final CommonFlows commonFlows = new CommonFlows();
       private final CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage();

       @Given("El usuario navega a Checkout Overview agregando al carrito items con indices:")
       public void goToCheckoutOverview(List<Integer> indexList) {
           commonFlows.goToCheckoutOverviewPage(indexList);
       }

       @When("El usuario hace click en finish")
       public void clickFinish() {
           checkoutOverviewPage.clickFinish();
       }
   }
   ```
3. Ejecútalo: `./mvnw test -Dcucumber.filter.name="Finalizar la compra"`.

> Los ejemplos de esta sección ilustran el estilo del proyecto; la pantalla *Checkout Overview* y sus steps **no existen** todavía en el código.

## Licencia

Proyecto bajo licencia **MIT**. <!-- PENDIENTE: el archivo LICENSE aún no existe en el repositorio; al agregarlo, enlázalo aquí como [MIT](LICENSE). -->

El APK de Swag Labs pertenece a sus autores y no queda cubierto por la licencia de este repositorio; ver [Sobre el APK](#sobre-el-apk).
