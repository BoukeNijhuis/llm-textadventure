package nl.boukenijhuis;

import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.model.Model;

public class Printer {

    public static void printStatus(Game game, Model model) {
        String message = "Game: " + game.getName() + System.lineSeparator()+ "Model: " + model.getName();
        System.out.print(message);
    }

    public static void print(String message) {
        System.out.printf("\n\n%s", message);
    }
}
