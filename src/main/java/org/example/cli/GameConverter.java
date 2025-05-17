package org.example.cli;

import org.example.game.Game;
import org.example.game.VoiceAdventure;
import org.example.game.Zork;
import picocli.CommandLine;

public class GameConverter implements CommandLine.ITypeConverter<Game> {

    private enum ValidGame {
        ZORK(new Zork()),
        VOICEADVENTURE(new VoiceAdventure());

        private final Game game;

        ValidGame(Game game) {
            this.game = game;
        }
    }

    public Game convert(String value) {
        Game game = ValidGame.valueOf(value.toUpperCase()).game;
        System.out.printf("Game: %s%s", game.getName(), System.lineSeparator());
        return game;
    }
}