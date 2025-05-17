package org.example.cli;

import org.example.game.Game;
import org.example.model.Model;

import static picocli.CommandLine.Option;

public class CommandLineParser implements Runnable {

    @Option(names = "--game", converter = GameConverter.class)
    private Game game;

    @Option(names = "--model", converter = ModelConverter.class)
    private Model model;

    @Override
    public void run() {
        // picocli needs a runnable
    }

    public Game getGame() {
        return game;
    }

    public Model getModel() {
        return model;
    }
}
