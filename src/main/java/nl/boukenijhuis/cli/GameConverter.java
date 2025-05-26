package nl.boukenijhuis.cli;

import nl.boukenijhuis.game.Hitchhiker;
import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.game.Zork;
import picocli.CommandLine;

public class GameConverter implements CommandLine.ITypeConverter<Game> {

    private enum ValidGame {
        ZORK(new Zork()),
        HITCHHIKER(new Hitchhiker());

        private final Game game;

        ValidGame(Game game) {
            this.game = game;
        }
    }

    public Game convert(String value) {
        Game game = ValidGame.valueOf(value.toUpperCase()).game;
        return game;
    }
}