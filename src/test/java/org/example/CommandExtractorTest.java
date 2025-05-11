package org.example;

import dev.langchain4j.chain.ConversationalChain;
import org.example.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandExtractorTest {

    Model model = mock(Model.class);
    ConversationalChain chain = mock(ConversationalChain.class);
    CommandExtractor commandExtractor = new CommandExtractor(model, chain);

    @Test
    public void emptyInput() {
        String noInput = "Did not receive any input from the game.";
        Mockito.when(chain.execute(noInput)).thenReturn(noInput);

        String command = commandExtractor.getCommand("", false);
        assertEquals(noInput, command);
    }

    @Test
    public void withoutChecks() {
        String input = "input";
        Mockito.when(chain.execute(input)).thenReturn("LLM answer");

        String command = commandExtractor.getCommand(input, false);
        assertEquals("LLM answer", command);
    }

    // TODO: add test for every check
}