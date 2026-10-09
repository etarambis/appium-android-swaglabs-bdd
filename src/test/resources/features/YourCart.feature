Feature: Your Cart

  Background: Precondicion del Feature
    Given El usuario navega a la pagina de Your Cart agregando al carrito items con indices:
      | 1 |
      | 2 |

  @regression @smoke
  Scenario: Eliminar item usando swipe
    When El usuario elimina el item con index 0 arrastrando de derecha a izquierda
    Then El usuario verifica que haya 1 items en el carrito