package nl.boukenijhuis;

import dev.langchain4j.chain.ConversationalChain;
import nl.boukenijhuis.provider.Provider;

import java.util.function.Function;

import static nl.boukenijhuis.Printer.print;

public class CommandExtractor {

    private static final String NO_INPUT_MESSAGE = "Did not receive any input from the game.";
    private static final String COMMAND_TOO_LONG_ERROR = "command too long";
    private static final String COMMAND_TOO_LONG_HINT = "Your answer is too long. Please give me EXACTLY ONE short command in the form of GO EAST, OPEN DOOR, etc. Do not invent your own story, we are playing an existing game!";
    private static final String QUOTES_FOUND_ERROR = "quotes found";
    private static final String QUOTES_FOUND_HINT = "Do not use quotes. Give only simple commands without any formatting.";
    private static final String FORMATTING_FOUND_ERROR = "formatting found";
    private static final String FORMATTING_FOUND_HINT = "Do not use formatting like *. Give only simple commands without any formatting.";
    private static final String WARNING_FORMAT = "!!! WARNING -> %s: %s !!!";

    private Provider model;
    private ConversationalChain chain;

    public CommandExtractor(Provider model, ConversationalChain chain) {
        this.model = model;
        this.chain = chain;
    }

    public String getCommand(String modelInput, boolean doChecks) {
        try {
            if (modelInput.isBlank()) {
                modelInput = NO_INPUT_MESSAGE;
            }

            String command = chain.execute(modelInput);

            // remove new lines (the removing of <think> works better with no new lines)
            command = command.replaceAll("\\n", " ");

            // remove <think>*</think>
            command = command.replaceAll("<think>.*</think>", "").trim();

            if (doChecks) {
                // the order matters
                command = checkCommand(command, x -> x.split(" ").length > 6, COMMAND_TOO_LONG_ERROR, COMMAND_TOO_LONG_HINT, chain);
                command = checkCommand(command, x -> x.indexOf("\"") > 1 || x.indexOf("'") > 1, QUOTES_FOUND_ERROR, QUOTES_FOUND_HINT, chain);
                command = checkCommand(command, x -> x.contains("*"), FORMATTING_FOUND_ERROR, FORMATTING_FOUND_HINT, chain);
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
            print(String.format(WARNING_FORMAT, errorMessage.toUpperCase(), command));
            return chain.execute(hint);

        } else {
            return command;
        }
    }
}
