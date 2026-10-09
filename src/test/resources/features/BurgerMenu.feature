Feature: Burger Menu

  Background: Precondicion del Feature
    Given El usuario abre el Burger Menu

    @regression @smoke
  Scenario: Logout
    When El usuario hace click en logout
    Then Se debe redirigir a la pagina de Login

  @regression @smoke
  Scenario: Cerrar Burger Menu
    When El usuario hace click en la X
    Then Se debe redirigir a la pagina de Shopping