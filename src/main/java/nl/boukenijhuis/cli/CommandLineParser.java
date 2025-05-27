package nl.boukenijhuis.cli;

import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.provider.Provider;
import nl.boukenijhuis.provider.ProviderBuilder;

import static picocli.CommandLine.Option;

public class CommandLineParser implements Runnable {

    @Option(names = "--game", required = true, converter = GameConverter.class)
    private Game game;

    @Option(names = "--model")
    private String model;

    @Option(names = "--provider", required = true, converter = ProviderConverter.class)
    private ProviderBuilder providerBuilder;

    @Override
    public void run() {
        // picocli needs a runnable
    }

    public Game getGame() {
        return game;
    }

    public String getModel() {
        return model;
    }

    public ProviderBuilder getProviderBuilder() { return providerBuilder; }
}
