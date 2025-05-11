package org.example;

import dev.langchain4j.chain.ConversationalChain;
import org.example.model.Model;

import java.util.function.Function;

import static org.example.Printer.print;

public class CommandExtractor {

    private Model model;
    private ConversationalChain chain;

    public CommandExtractor(Model model, ConversationalChain chain) {
        this.model = model;
        this.chain = chain;
    }

    public String getCommand(String modelInput, boolean doChecks) {
        try {
            if (modelInput.isBlank()) {
                modelInput = "Did not receive any input from the game.";
            }

            String command = chain.execute(modelInput);

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
            print(String.format("WARNING -> %s: %s%n", errorMessage.toUpperCase(), command));
            return chain.execute(hint);

        } else {
            return command;
        }
    }
}
