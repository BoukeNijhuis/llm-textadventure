package org.example;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.example.game.Game;
import org.example.game.Zork;
import org.example.model.Gemini;
import org.example.model.Model;

import java.io.IOException;

import static org.example.Printer.initialPrint;
import static org.example.RepeatPreventer.updateOutputWhenTheGameKeepsRepeating;
import static org.example.Printer.print;

public class Main {

    private static final Model model  = new Gemini();
//    private static final Model model  = new Ollama("gemma3:12b");
    private static final Game game = new Zork();
//        private static final Game game = new VoiceAdventure();


    public static void main(String[] args) throws IOException {

        // start the game
        game.start();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model.getChatLanguageModel())
                .chatMemory(chatMemory)
                .build();

        String modelInput = """
                I would like to play a text adventure with you.
                I provide the descriptions and you will provide ONLY ONE command in the form of OPEN DOOR, GO WEST, etc. 
                Do not use formatting. 
                Try not to die. 
                Use the hints that the game gives you.
                """;

        // init game
        CommandExtractor commandExtractor = new CommandExtractor(model, chain);
        commandExtractor.getCommand(modelInput, false);
        modelInput = game.read();
        initialPrint(modelInput);

        // game loop
        while (true) {

            // do not check the provided instructions
            String command = commandExtractor.getCommand(modelInput, true);

            if (command != null) {
                print(command.toUpperCase());
                modelInput = game.writeAndRead(command);
                print(modelInput);

                if (modelInput.contains(game.getCompletionString())) {
                    System.exit(0);
                }

                modelInput = updateOutputWhenTheGameKeepsRepeating(modelInput);
            }
        }
    }
}