package nl.boukenijhuis;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import nl.boukenijhuis.cli.CommandLineParser;
import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.model.Model;
import picocli.CommandLine;

import java.io.IOException;

import static nl.boukenijhuis.Printer.*;
import static nl.boukenijhuis.RepeatPreventer.updateOutputWhenTheGameKeepsRepeating;

public class Main {

    // TODO: something goes wrong after incorrect commands from the llm (check the scratch)
    // TODO: readme with instructions for Zork, DumbFrotz & all models
    // TODO: introduce family

    public static void main(String[] args) throws IOException {
        CommandLineParser clParser = new CommandLineParser();
        new CommandLine(clParser).execute(args);

        Game game = clParser.getGame();
        Model model = clParser.getModel();

        printStatus(game, model);

        // setup the chain
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model.getChatLanguageModel())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
                .build();

        // initial prompt
        String modelInput = """
                I would like to play a text adventure with you.
                I provide the descriptions and you will provide ONLY ONE command in the form of OPEN DOOR, GO WEST, etc. 
                Do not use formatting. 
                Try not to die. 
                Use the hints that the game gives you.
                """;

        CommandExtractor commandExtractor = new CommandExtractor(model, chain);
        // do not check the provided instructions
        commandExtractor.getCommand(modelInput, false);

        // init game
        game.start();
        modelInput = game.read();
        print(modelInput);

        // game loop
        while (true) {

            String command = commandExtractor.getCommand(modelInput, true);

            if (command != null) {
                print(command.toUpperCase());
                modelInput = game.writeAndRead(command);
                print(modelInput);

                // check if the game is over
                if (modelInput.contains(game.getCompletionString())) {
                    System.exit(0);
                }

                // help the llm when it is stuck
                modelInput = updateOutputWhenTheGameKeepsRepeating(modelInput);
            }
        }
    }
}
