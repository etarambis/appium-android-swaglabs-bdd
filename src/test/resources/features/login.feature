Feature: Login

  Background: Precondicion del feature
    Given El usuario navega a la pagina de Login

  @regression @smoke
  Scenario Outline: Credenciales
    When El usuario escribe el username <username> con el password <password> y presiona el boton de login
    Then Debe aparecer un mensaje de error indicando <mensaje>
    Examples:
      | username          | password          | mensaje                                                          |
      | "locked_out_user" | "secret_sauce"    | "Sorry, this user has been locked out."                          |
      | "hello world"     | "secret_password" | "Username and password do not match any user in this service."   |

  @regression @smoke
  Scenario: Verificar la UI de la pagina de Login
    Then El usuario verifica que la UI de la pagina de Login es correcta

  @regression
  Scenario: Usuario loguea haciendo tap
    When El usuario se loguea haciendo tap
    Then Se debe redirigir a la pagina de Shopping

  @regression
  Scenario: Verificar label de credenciales
    Then El usuario verifica los labels de credenciales

