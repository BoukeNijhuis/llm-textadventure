package nl.boukenijhuis.cli;

import nl.boukenijhuis.game.Hitchhiker;
import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.game.Zork;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;

public class GameConverter implements CommandLine.ITypeConverter<Game> {

    public enum ValidGame {
        ZORK(new Zork()),
        HITCHHIKER(new Hitchhiker());

        private final Game game;

        ValidGame(Game game) {
            this.game = game;
        }

        static class CompletionCandidates extends ArrayList<String> {
            CompletionCandidates() {
                super(new ArrayList<>(
                        Arrays.stream(values())
                                .map(Enum::name)
                                .map(String::toLowerCase)
                                .toList()));
            }
        }
    }

    public Game convert(String value) {
        Game game = ValidGame.valueOf(value.toUpperCase()).game;
        return game;
    }
}