package nl.boukenijhuis;

import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.provider.Provider;

public class Printer {

    public static void printStatus(Game game, Provider provider) {
        String message =
                "Game:     " + game.getName() + System.lineSeparator() +
                "Provider: " + provider.getName() + System.lineSeparator() +
                "Model:    " + capitalize(provider.getModel());
        System.out.print(message);
    }

    private static String capitalize(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static void print(String message) {
        System.out.printf("\n\n%s", message);
    }
}
