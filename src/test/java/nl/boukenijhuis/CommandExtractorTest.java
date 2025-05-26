package nl.boukenijhuis;

import dev.langchain4j.chain.ConversationalChain;
import nl.boukenijhuis.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CommandExtractorTest {

    private static final String NO_INPUT_MESSAGE = "Did not receive any input from the game.";
    private static final String COMMAND_TOO_LONG_HINT = "Your answer is too long. Please give me EXACTLY ONE short command in the form of GO EAST, OPEN DOOR, etc. Do not invent your own story, we are playing an existing game!";
    private static final String QUOTES_FOUND_HINT = "Do not use quotes. Give only simple commands without any formatting.";
    private static final String FORMATTING_FOUND_HINT = "Do not use formatting like *. Give only simple commands without any formatting.";

    Model model = mock(Model.class);
    ConversationalChain chain = mock(ConversationalChain.class);
    CommandExtractor commandExtractor = new CommandExtractor(model, chain);

    @Test
    public void emptyInput() {
        Mockito.when(chain.execute(NO_INPUT_MESSAGE)).thenReturn(NO_INPUT_MESSAGE);

        String command = commandExtractor.getCommand("", false);
        assertEquals(NO_INPUT_MESSAGE, command);
    }

    @Test
    public void withoutChecks() {
        String input = "input";
        String llmAnswer = "LLM answer";
        Mockito.when(chain.execute(input)).thenReturn(llmAnswer);

        String command = commandExtractor.getCommand(input, false);
        assertEquals(llmAnswer, command);
    }

    @Test
    public void commandTooLong() {
        String input = "input";
        Mockito.when(chain.execute(input)).thenReturn("a command that is too long for sure");
        String improvedAnswer = "improved answer";
        Mockito.when(chain.execute(COMMAND_TOO_LONG_HINT)).thenReturn(improvedAnswer);

        String command = commandExtractor.getCommand(input, true);
        assertEquals(improvedAnswer, command);
    }

    @Test
    public void quotesFound() {
        String input = "input";
        Mockito.when(chain.execute(input)).thenReturn("an answer containing \"quotes\".");
        String improvedAnswer = "improved answer";
        Mockito.when(chain.execute(QUOTES_FOUND_HINT)).thenReturn(improvedAnswer);

        String command = commandExtractor.getCommand(input, true);
        assertEquals(improvedAnswer, command);
    }

    @Test
    public void formattingFound() {
        String input = "input";
        Mockito.when(chain.execute(input)).thenReturn("an answer containing *formatting*.");
        String improvedAnswer = "improved answer";
        Mockito.when(chain.execute(FORMATTING_FOUND_HINT)).thenReturn(improvedAnswer);

        String command = commandExtractor.getCommand(input, true);
        assertEquals(improvedAnswer, command);
    }

    @Test
    public void removeThinkingPart() {
        String input = "input";
        Mockito.when(chain.execute(input)).thenReturn("<think>thinking</think> command");

        String command = commandExtractor.getCommand(input, true);
        assertEquals("command", command);
    }

    @Test
    public void removeLongThinkingPart() {
        String input = "input";
        Mockito.when(chain.execute(input)).thenReturn("<think>\nOkay, the user wants to play a text adventure game. They mentioned starting in a field west of a big white house with a boarded front door and a mailbox. The goal is to provide only one command each time.\n\nFirst, I need to think about typical text adventure mechanics. The mailbox is mentioned, so checking it is a common first step. The player might expect to find something inside, like a key or a note, which could be useful later. Since the front door is boarded up, maybe there's another way into the house, but the mailbox is the immediate interactable object here.\n\nI should make sure the command is straightforward. The user might want to examine the mailbox first. The command \"OPEN MAILBOX\" makes sense here. It's a logical first action to explore the environment and gather items or clues. There's no immediate danger mentioned, so this action should be safe. Let's go with that.\n</think>\n\nOPEN MAILBOX".formatted());

        String command = commandExtractor.getCommand(input, true);
        assertEquals("OPEN MAILBOX", command);
    }



}
