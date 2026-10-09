Feature: Deeplinks

  @regression
  Scenario: Item Detail Deeplinks
    When El usuario usa el deeplink del item detail con index 0
    Then Se debe redirigir a la pagina de Item Detail
    And Verifica que se tenga el item "Sauce Labs Backpack" con precio 29.99

  @regression
  Scenario: Shopping Deeplinks
    When El usuario usa el deeplink de Shopping con indices "0,2"
    Then Se debe redirigir a la pagina de Shopping
    And El usuario verifica que haya 2 items en el carrito