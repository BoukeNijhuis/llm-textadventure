package org.example;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.example.game.Game;
import org.example.game.VoiceAdventure;
import org.example.game.Zork;
import org.example.model.Gemini;
import org.example.model.Model;
import org.example.model.Ollama;

import java.io.IOException;

import static org.example.Printer.initialPrint;
import static org.example.Printer.print;
import static org.example.RepeatPreventer.updateOutputWhenTheGameKeepsRepeating;

public class Main {

    // TODO: extra line after initial prompt
    // TODO: something goes wrong after incorrect commands from the llm (check the scratch)
    // TODO: choose model & game with input parameters
    // TODO: readme with instructions for Zork, Ollama & Gemini
    // TODO: backup scratch for playing VoiceAdventure

    private static final Model model = new Gemini();
    private static final Game game = new VoiceAdventure();

    public static void main(String[] args) throws IOException {

        // setup the chain
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model.getChatLanguageModel())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
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
        initialPrint(modelInput);

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