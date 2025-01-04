package org.example;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Zork zork = new Zork();
    private static final String OLLAMA_URL = "http://localhost:11434";

    public static void main(String[] args) throws IOException {

        zork.start();

        OllamaChatModel model = OllamaChatModel.builder()
                .modelName("falcon3:10b")
                .baseUrl(OLLAMA_URL)
                .build();

//        ChatLanguageModel model = OpenAiChatModel.builder()
//                .baseUrl("https://api.groq.com/openai/v1")
//                .apiKey("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")
//                .modelName("llama3-groq-70b-8192-tool-use-preview")
//                .build();
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .build();


        String answer = chain.execute("I would like to play a text adventure with you. I provide the descriptions and you will provide ONLY ONE command in the form of OPEN DOOR, GO WEST, etc.");

        logger.error(answer);
        String output = zork.getOutput();
        logger.info(output);

        for (int i = 0; i < 20; i++) {

            String command = getCommand(chain, output);

            logger.info(command.toUpperCase());
//            logger.info((">"));
            output = zork.writeAndRead(command);
            logger.info(output);
        }
    }

    private static String getCommand(ConversationalChain chain, String modelInput) {
        String command = chain.execute(modelInput);
        // check if command is really a command
        int words = command.split(" ").length;

        while (words > 3) {
            System.out.println("DISCARDED " + words + " :" + command);
            command = chain.execute("Your answer is too long. Please give me EXACTLY ONE short command in the form of GO EAST, OPEN DOOR, etc.");
            words = command.split(" ").length;

        }


        return command;
    }

    public static void printWriteAndRead(String input) {
        logger.info(input.toUpperCase());
        logger.info((">"));
        logger.info(zork.writeAndRead(input));

    }
}