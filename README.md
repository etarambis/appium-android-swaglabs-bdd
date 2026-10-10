# AppiumBDD

Automatización de pruebas móviles para la app Android **Swag Labs** (Sauce Labs) con enfoque **BDD**.

- **Lenguaje y build:** Java 17 y Maven (se usa el wrapper `./mvnw`, no hace falta instalar Maven).
- **Automatización móvil:** Appium 3 con el driver UiAutomator2 (`io.appium:java-client` 10.1.1).
- **BDD:** Cucumber 7 sobre JUnit Platform, con los escenarios escritos en Gherkin en español.
- **Patrón:** Page Object Model.
- **Reportes:** `cucumber-reporting` (HTML) y logs con Log4j2.

Estado actual: 7 features, 15 escenarios (18 ejecuciones por los `Scenario Outline`). Todos llevan `@regression` y 8 llevan además `@smoke`.

---

## 1. Requisitos

| Requisito | Detalle |
|---|---|
| JDK | 17 (`java -version`) con `JAVA_HOME` configurado |
| Node.js | Para instalar Appium |
| Appium | 3.7.0 con el driver `uiautomator2` |
| Android SDK | `adb` en el `PATH` y `ANDROID_HOME` configurado |
| Dispositivo | Emulador Android (probado con API 34, x86_64, `emulator-5554`) o un dispositivo físico |
| Shell | Git Bash en Windows, o cualquier shell en Linux/macOS (los scripts son `.sh`) |

> El emulador necesita memoria libre. Si tu equipo está al límite, las esperas de 20 s del framework suelen alcanzar, pero ver la sección de problemas comunes.

## 2. Instalación

```bash
# 1. Clonar el repositorio
git clone https://github.com/etarambis/appium-android-swaglabs-bdd.git
cd appium-android-swaglabs-bdd

# 2. Instalar Appium y el driver de Android
npm install -g appium@3.7.0
appium driver install uiautomator2

# 3. Compilar (descarga las dependencias de Maven la primera vez)
./mvnw clean test-compile
```

El APK de la app ya viene en `src/test/resources/apk/sauceLabs.apk`.

## 3. Cómo ejecutar

Antes de ejecutar, deja listos estos dos puntos:

1. **Emulador encendido:** `adb devices` debe listar el dispositivo como `device`.
2. **Servidor Appium corriendo:** en otra terminal ejecuta `appium` y comprueba `http://127.0.0.1:4723/status`.

```bash
# Suite completa (@regression), desde la raíz del proyecto
./runSuite.sh

# Solo los smoke
./mvnw clean test -Dgroups="smoke"

# Un feature concreto
./mvnw test -Dcucumber.features=src/test/resources/features/Login.feature

# Un escenario por nombre (Surefire cuenta los demás escenarios como "Skipped")
./mvnw test -Dcucumber.filter.name="Logout"

# Abrir el reporte HTML
./openReport.sh
```

**Desde IntelliJ IDEA:** ejecuta el archivo `.feature` (o la carpeta `features`) con el glue `hooks steps` y el directorio de trabajo en la raíz del proyecto (las rutas de `config.properties` son relativas a ella). El log por step (`hooks.StepLogger`) se activa con la propiedad `cucumber.plugin` de `junit-platform.properties`; si ejecutas con el runner propio de IntelliJ, agrega `--plugin hooks.StepLogger` a los argumentos del programa. El resto de logs y evidencias funciona igual.

> **Importante:** el `pom.xml` mantiene `testFailureIgnore=true`. El build termina en `BUILD SUCCESS` aunque falle una prueba. Revisa siempre el reporte o la línea `Tests run: ..., Failures: ...`.

## 4. Configuración

Los valores configurables viven en [`src/test/resources/config.properties`](src/test/resources/config.properties). Cada clave se puede sobrescribir con una propiedad de sistema (`-Dclave=valor`) o con una variable de entorno (clave en mayúsculas y `.` cambiado por `_`). Prioridad: propiedad de sistema, variable de entorno, archivo.

| Clave | Valor por defecto | Para qué |
|---|---|---|
| `appium.url` | `http://127.0.0.1:4723/` | Servidor Appium |
| `app.path` | `src/test/resources/apk/sauceLabs.apk` | APK a instalar |
| `app.wait.activity` | `com.swaglabsmobileapp.MainActivity` | Actividad que se espera al arrancar |
| `timeout.default` | `20` | Espera por defecto de las páginas, en segundos |
| `timeout.adb.exec` | `60000` | Timeout de comandos adb, en ms |
| `timeout.uiautomator2.launch` | `90000` | Arranque del servidor UiAutomator2, en ms |
| `evidence.screenshots.dir` | `target/evidence/screenshots/` | Capturas de fallos |
| `evidence.pagesource.dir` | `target/evidence/pageSource/` | Page source de fallos |
| `credentials.file` | `src/test/resources/data/credenciales.json` | Datos de login |
| `log.dir` | `target/logs` | Carpeta de logs (debe coincidir con `logDir` de `log4j2.xml`) |
| `logcat.max.lines` | `300` | Líneas del logcat que se adjuntan en un fallo |

Ejemplo: `./mvnw test -Dappium.url=http://192.168.1.50:4723/ -Dtimeout.default=30`

## 5. Estructura del proyecto

```
.
├── pom.xml · mvnw · runSuite.sh · openReport.sh
└── src/test
    ├── java
    │   ├── hooks/       Hooks (ciclo de vida de cada escenario) y StepLogger (plugin de log por step)
    │   ├── steps/       Step definitions: traducen el Gherkin a llamadas a las páginas
    │   ├── pages/       Page Objects (LoginPage, ShoppingPage, TopBar, BurgerMenuPage,
    │   │                ItemDetailPage, YourCartPage, YourInformationPage)
    │   ├── utilities/   BasePage (esperas y acciones), DriverManager/DriverProvider (driver),
    │   │                Gestures, DeepLinks, CommonFlows, Config, Logs,
    │   │                FileManager (evidencia en disco), ReportAttachments (adjuntos al reporte)
    │   ├── data/        DataGiver y JsonReader (credenciales desde JSON)
    │   ├── models/      Credential y CredentialJson
    │   └── principal/   RunTests (clase @Suite; no se usa para ejecutar)
    └── resources
        ├── features/    Los .feature (Login, Shopping, ItemDetail, BurgerMenu, YourCart, YourInformation, DeepLinks)
        ├── apk/         sauceLabs.apk
        ├── data/        credenciales.json
        ├── config.properties · log4j2.xml · junit-platform.properties
```

Cómo fluye una prueba: el `.feature` invoca un step, el step usa un Page Object, y el Page Object usa los métodos de `BasePage` (que esperan al elemento antes de actuar). `Hooks` crea el driver antes de cada escenario y lo cierra al terminar.

## 6. Evidencias, logs y reporte

Todo se genera bajo `target/` (se limpia con `mvn clean`):

| Qué | Dónde |
|---|---|
| Reporte HTML | `target/cucumber-html-reports/overview-features.html` |
| JSON de Cucumber | `target/cucumber/report.json` |
| Log global | `target/logs/automation.log` |
| Log por escenario | `target/logs/scenarios/<escenario>_L<línea>.log` |
| Screenshot y page source de fallos | `target/evidence/screenshots/` y `target/evidence/pageSource/` |

Cuando un escenario falla, el reporte lleva adjuntos: **screenshot, page source, logcat del dispositivo (últimas 300 líneas) y el log del escenario**. Si el driver ni siquiera llega a crearse, el log del escenario se adjunta igualmente. Los escenarios que pasan no generan evidencias.

Para diagnosticar un fallo sin volver a ejecutar, abre el log del escenario: tiene cada step con su estado y duración, los elementos buscados, los timeouts y el stack trace del error.

## 7. Cómo agregar una prueba nueva

1. **Page Object** (en `pages/`): extiende `BasePage`, define los locators (prefiere `AppiumBy.accessibilityId("test-...")`) e implementa `waitPageToLoad()` y `verifyPage()`.

   ```java
   public class CheckoutOverviewPage extends BasePage {
       private final By finishButton = AppiumBy.accessibilityId("test-FINISH");

       @Override
       public void waitPageToLoad() { waitPage(finishButton); }

       @Override
       public void verifyPage() { verifyDisplayed(finishButton); }

       public void clickFinish() { click(finishButton); }
   }
   ```

   Usa `click`, `type`, `find`, `findAll` y `verifyDisplayed` de `BasePage`: todos esperan al elemento (20 s por defecto).

2. **Flujo reutilizable** (opcional): si varias pruebas necesitan llegar a la misma pantalla, agrégalo a `CommonFlows`.

3. **Steps** (en `steps/`): una clase por funcionalidad. El texto de la anotación debe coincidir con el `.feature`.

   ```java
   @When("El usuario finaliza la compra")
   public void finishPurchase() {
       checkoutOverviewPage.clickFinish();
   }
   ```

4. **Feature** (en `resources/features/`): usa un `Background` para la precondición y los tags `@regression` y, si es crítico, `@smoke`.

   ```gherkin
   @regression @smoke
   Scenario: Finalizar la compra
     When El usuario finaliza la compra
     Then Se debe mostrar la pagina de confirmacion
   ```

5. **Ejecútalo**: `./mvnw test -Dcucumber.filter.name="Finalizar la compra"`.

Convenciones: clases de páginas terminan en `Page`, las de steps en `StepDefinition`, y los nombres de código van en inglés (los mensajes de log y el Gherkin, en español). Los mensajes de commit, en inglés.

## 8. Problemas comunes

| Síntoma | Causa probable | Solución |
|---|---|---|
| `Connection error (POST http://127.0.0.1:4723/session)` | Appium no está corriendo o la URL es otra | Inicia `appium` y revisa `…/status`; si usas otro host, `-Dappium.url=...` |
| La sesión no se crea o tarda mucho | Emulador apagado, `offline`, o lento | `adb devices` debe mostrar `device`; reinicia el emulador. Sube `timeout.uiautomator2.launch` si hace falta |
| `TimeoutException ... esperando` | El elemento no apareció en 20 s (emulador cargado, pantalla distinta o locator cambiado) | Revisa el log del escenario y el page source adjunto. Sube `-Dtimeout.default=30` si es solo lentitud |
| `BUILD SUCCESS` pero hay pruebas fallidas | `testFailureIgnore=true` en el `pom.xml` | Revisa `Tests run: …, Failures: …` o el reporte HTML |
| `Tests run: 0` pese a que los escenarios corren | Surefire 3.5.x no cuenta bien el motor de Cucumber con JUnit 6 | Mantén Surefire en 3.2.5 (versión actual del `pom.xml`) |
| `bad interpreter` o `\r: command not found` al ejecutar un `.sh` | Fin de línea CRLF en un script | El `.gitattributes` fuerza LF para `.sh` y `mvnw`; vuelve a clonar o ejecuta `git add --renormalize .` |
| No se encuentra `adb` | El SDK no está en el `PATH` | Agrega `platform-tools` al `PATH` o configura `ANDROID_HOME` |
| `No se encontro config.properties` / rutas que no existen | Se ejecuta desde otra carpeta | Ejecuta desde la raíz del proyecto (o fija el directorio de trabajo en IntelliJ) |
| No aparece `target/evidence` | No hubo fallos | Es lo esperado: solo se generan evidencias cuando un escenario falla |
| Un cambio en el `pom.xml` no se refleja en IntelliJ | El proyecto Maven no se recargó | `Maven > Reload project` |

## 9. Limitaciones conocidas

- **Modo remoto sin implementar:** si la variable de entorno `JOB_NAME` está definida, `DriverManager` lanza una excepción clara en lugar de crear el driver.
- **`addToCartDrag` no usa los índices de la tabla:** los features indican los índices por legibilidad, pero solo se usa la cantidad de filas y se arrastran los primeros items.
- **Un driver nuevo por escenario:** cada escenario reinstala y abre la app (unos 20 s por escenario).
- **Gestos con pausas fijas:** `Gestures` usa pausas de 1 a 3,5 s para ser fiable en emuladores lentos.
