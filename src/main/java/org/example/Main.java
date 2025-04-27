package org.example;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.model.mistralai.MistralAiChatModelName;
import org.example.factory.CustomMistralAiClientBuilderFactory;

import java.io.IOException;
import java.util.function.Function;

import static org.example.Zork.clean;

public class Main {

    private static final Zork zork = new Zork();
    private static int iDoNotUnderstandCounter = 0;

    public static void main(String[] args) throws IOException {

        // start the game
        zork.start();

//        OllamaChatModel model = OllamaChatModel.builder()
//                .modelName("deepseek-r1:14b")
//                .baseUrl("http://localhost:11434")
//                .build();

//        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
//                .project("voiceadventure-3cf8a")
//                .location("us-central1")
//                .modelName("gemini-2.0-pro-exp-02-05")
//                // prevents rate limiter logging
//                .maxRetries(1)
//                .build();

        //hack
        new CustomMistralAiClientBuilderFactory();

        ChatLanguageModel model = MistralAiChatModel.builder()
                .modelName(MistralAiChatModelName.MISTRAL_SMALL_LATEST)
                .apiKey(System.getenv("MISTRAL_AI_API_KEY"))
                // prevents rate limiter logging
                .maxRetries(1)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
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
        modelInput = zork.getOutput();
        System.out.print(clean(modelInput));

        // game loop
        for (int i = 0; i < 100000; i++) {

            modelInput = updateOutputWhenTheGameKeepsSayingIDoNotUnderstand(modelInput);

            // do not check the provided instructions
            String command = getCommand(chain, modelInput, true);

            if (command != null) {
                System.out.printf("\n\n%s", clean(command.toUpperCase()));
                modelInput = zork.writeAndRead(command);
                System.out.printf("\n\n%s", modelInput);
            }
        }
    }

    private static String updateOutputWhenTheGameKeepsSayingIDoNotUnderstand(String output) {
        if (output.startsWith("I don't understand that.")) {
            if (++iDoNotUnderstandCounter == 5) {
                output = "The game keeps repeating 'I don't understand that'. So walk around and check your inventory.";
                // warn the spectator
                System.out.println("\n\nThe counter tripped!");

                // reset the counter
                iDoNotUnderstandCounter = 0;
            }

        } else {
            iDoNotUnderstandCounter = 0;
        }
        return output;
    }

    private static String getCommand(ConversationalChain chain, String modelInput, boolean doChecks) {
        try {
            String command = chain.execute(modelInput);

            // when using deepseek-r1 remove <think>*</think>
            int index = command.indexOf("</think>");
            if (index != -1) {
                command = command.substring(index + 9);
            }

            if (doChecks) {
                // the order matters
                command = checkCommand(command, x -> x.split(" ").length > 6, "command too long", "Your answer is too long. Please give me EXACTLY ONE short command in the form of GO EAST, OPEN DOOR, etc.", chain);
                command = checkCommand(command, x -> x.indexOf("\"") > 1, "quotes found", "Do not use quotes. Give only simple commands without any formatting.", chain);
                command = checkCommand(command, x -> x.indexOf("'") > 1, "quotes found", "Do not use quotes. Give only simple commands without any formatting.", chain);
                command = checkCommand(command, x -> x.startsWith("*"), "formatting found", "Do not use formatting like *. Give only simple commands without any formatting.", chain);
            }
            return command;
        } catch (Exception e) {
            // ignore the rate limiter
            if (
                    e.getMessage().indexOf("com.google.api.gax.rpc.ResourceExhaustedException") > 0 || // gemini
                    e.getMessage().indexOf("Requests rate limit exceeded") > 0) // mistral
            {
//                System.out.print("\n.");
                // no new command, just retry
                return null;
            } else {
                throw e;
            }
        }
    }

    private static String checkCommand(String command, Function<String, Boolean> check, String errorMessage, String hint, ConversationalChain chain) {

        if (check.apply(command)) {
            System.out.printf("\n\nWARNING -> %s: ---%s---%n", errorMessage.toUpperCase(), command);
            return chain.execute(hint);

        } else {
            return command;
        }
    }
}