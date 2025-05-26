package nl.boukenijhuis;

import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.provider.Provider;

public class Printer {

    public static void printStatus(Game game, Provider model) {
        String message = "Game: " + game.getName() + System.lineSeparator()+ "Model: " + model.getName();
        System.out.print(message);
    }

    public static void print(String message) {
        System.out.printf("\n\n%s", message);
    }
}
