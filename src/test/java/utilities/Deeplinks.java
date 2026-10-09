package utilities;

public class Deeplinks {
    private static final String ITEM_DETAIL = "swaglabs://swag-item/%d";
    private static final String SHOPPING = "swaglabs://swag-overview/%s";

    private static void goTo(String deeplink) {
        new DriverProvider().get().get(deeplink);
    }

    public static void goToItemDetail(int index) {
        final var deeplink = String.format(ITEM_DETAIL, index);
        goTo(deeplink);
    }

    public static void goToShopping(String list) {
        final var deeplink = String.format(SHOPPING, list);
        goTo(deeplink);
    }

}
