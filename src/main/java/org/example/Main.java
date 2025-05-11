package org.example;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.example.game.Game;
import org.example.game.VoiceAdventure;
import org.example.game.Zork;
import org.example.model.Gemini;
import org.example.model.Model;
import org.example.model.Ollama;

import java.io.IOException;
import java.util.function.Function;

public class Main {

    private static final Model model  = new Gemini();
//    private static final Model model  = new Ollama("gemma3:12b");
    private static final Game game = new Zork();
//        private static final Game game = new VoiceAdventure();
    private static int repeatCounter = 0;
    private static String repeatPhrase;

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
        getCommand(chain, modelInput, false);
        modelInput = game.read();
        System.out.print(modelInput);

        // game loop
        for (int i = 0; i < 100000; i++) {

            // do not check the provided instructions
            String command = getCommand(chain, modelInput, true);

            if (command != null) {
                System.out.printf("\n\n%s", command.toUpperCase());
                modelInput = game.writeAndRead(command);
                System.out.printf("\n\n%s", modelInput);

                if (modelInput.contains(game.getCompletionString())) {
                    System.exit(0);
                }

                modelInput = updateOutputWhenTheGameKeepsRepeating(modelInput);
            }
        }
    }

    private static String updateOutputWhenTheGameKeepsRepeating(String output) {
        if (repeatPhrase != null && output.startsWith(repeatPhrase)) {
            if (++repeatCounter == 5) {
                output = "The game keeps repeating'. So walk around and check your inventory.";
                // warn the spectator
                System.out.println("\n\nThe counter tripped! With phrase: " + repeatPhrase);

                // reset the counter & phrase
                resetCounterAndPhrase();
            }

        } else {
            resetCounterAndPhrase();
        }
        // update the repeat phrase
        repeatPhrase = output;
        return output;
    }

    private static void resetCounterAndPhrase() {
        repeatCounter = 0;
        repeatPhrase = null;
    }

    private static String getCommand(ConversationalChain chain, String modelInput, boolean doChecks) {
        try {
            if (modelInput.isBlank()) {
                modelInput = "Did not receive any input from the game.";
            }

            String command = chain.execute(modelInput);

            // when using deepseek-r1 remove <think>*</think>
            int index = command.indexOf("</think>");
            if (index != -1) {
                command = command.substring(index + 9);
            }

            if (doChecks) {
                // the order matters
                command = checkCommand(command, x -> x.split(" ").length > 6, "command too long", "Your answer is too long. Please give me EXACTLY ONE short command in the form of GO EAST, OPEN DOOR, etc.", chain);
                command = checkCommand(command, x -> x.indexOf("\"") > 1 || x.indexOf("'") > 1, "quotes found", "Do not use quotes. Give only simple commands without any formatting.", chain);
                command = checkCommand(command, x -> x.startsWith("*"), "formatting found", "Do not use formatting like *. Give only simple commands without any formatting.", chain);
            }
            return command;
        } catch (Exception e) {

            try {
                return model.handleException(e);
            } catch (Exception ex) {
                throw e;
            }
        }
    }

    private static String checkCommand(String command, Function<String, Boolean> check, String errorMessage, String hint, ConversationalChain chain) {

        if (check.apply(command)) {
            System.out.printf("\n\nWARNING -> %s: %s%n", errorMessage.toUpperCase(), command);
            return chain.execute(hint);

        } else {
            return command;
        }
    }
}