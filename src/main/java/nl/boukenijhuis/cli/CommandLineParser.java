package nl.boukenijhuis.cli;

import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.provider.ProviderBuilder;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(sortOptions = false)
public class CommandLineParser implements Runnable {

    @Option(names = "--game", required = true,
            description = "Supported games: ${COMPLETION-CANDIDATES}",
            converter = GameConverter.class, completionCandidates = GameConverter.ValidGame.CompletionCandidates.class)
    private Game game;

    @Option(names = "--provider", required = true,
            description = "Supported models: ${COMPLETION-CANDIDATES}",
            converter = ProviderConverter.class, completionCandidates = ProviderConverter.ValidProvider.CompletionCandidates.class)
    private ProviderBuilder providerBuilder;

    @Option(names = "--model")
    private String model;

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
