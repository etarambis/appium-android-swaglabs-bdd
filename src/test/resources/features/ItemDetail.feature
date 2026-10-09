Feature: Item Detail

  Background: Precondicion del Feature
    Given El usuario navega al detalle del item con index 0

  @regression @smoke
  Scenario: Verificar la UI de la pagina de Item Detail
    Then El usuario verifica que la UI de la pagina de Login sea correcta

  @regression @smoke
  Scenario: Boton Back to Products
    When El usuario hace click en el boton Back to Products
    Then Se debe redirigir a la pagina de Shopping

  @regression
  Scenario: Regresar con back
    When El usuario presiona el boton back de su celular
    Then Se debe redirigir a la pagina de Shopping