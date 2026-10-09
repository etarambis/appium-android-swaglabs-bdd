Feature: Your Information

  Background: Precondicion del Feature
    Given El usuario navega a Your Information agregando al carrito items con indices:
      | 0 |
      | 2 |

    @regression
  Scenario Outline: Mensajes de Error
    When El usuario escribe el <nombre>, <apellido>, <zipcode> y presiona el boton de Continue
    Then Debe aparecer el mensaje con texto <mensaje>
    Examples:
      | nombre  | apellido  | zipcode      | mensaje                     |
      | ""      | "Academy" | "Automation" | "First Name is required"    |
      | "Blass" | ""        | "Automation" | "Last Name is required"     |
      | "Blass" | "Academy" | ""           | "Postal Code is required"   |