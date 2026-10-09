Feature: Shopping

  Background: Precondicion del Feature
    Given El usuario navega a la pagina de Shopping

  @regression @smoke
  Scenario: Verificar la UI de la pagina de Shopping
    Then El usuario verifica que la UI de la pagina de Shopping es correcta

  @regression
  Scenario: Agregar al carrito arrastrando
    When El usuario activa el modo vertical
    And El usuario agrega al carrito los items con indices:
      | 0 |
      | 2 |
      | 3 |
    Then El usuario verifica que haya 3 items en el carrito