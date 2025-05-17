package org.example.cli;

import org.example.game.Game;
import org.example.game.VoiceAdventure;
import org.example.game.Zork;
import org.example.model.Gemini;
import org.example.model.Model;
import org.example.model.Ollama;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandLineParserTest {

    private CommandLineParser parser;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        parser = new CommandLineParser();

        // capture System.out and System.err for assertions
        originalOut = System.out;
        originalErr = System.err;
        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        // restore System.out and System.err
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testDefaultValues() {
        // parse empty args
        new CommandLine(parser).execute();

        // Default values should be null
        assertNull(parser.getGame());
        assertNull(parser.getModel());
    }

    @Test
    void testSetGame() {
        new CommandLine(parser).execute("--game", "zork");

        Game game = parser.getGame();
        assertNotNull(game);
        assertTrue(game instanceof Zork);

        assertTrue(outputStream.toString().contains("Game: Zork"));
    }

    @Test
    void testSetModel() {
        new CommandLine(parser).execute("--model", "gemini");

        Model model = parser.getModel();
        assertNotNull(model);
        assertTrue(model instanceof Gemini);

        assertTrue(outputStream.toString().contains("Model: Gemini"));
    }

    @Test
    void testSetBothGameAndModel() {
        new CommandLine(parser).execute("--game", "voiceadventure", "--model", "gemini");

        Game game = parser.getGame();
        assertNotNull(game);
        assertTrue(game instanceof VoiceAdventure);

        Model model = parser.getModel();
        assertNotNull(model);
        assertTrue(model instanceof Gemini);

        String output = outputStream.toString();
        assertTrue(output.contains("Game: VoiceAdventure"));
        assertTrue(output.contains("Model: Gemini"));
    }

    @Test
    void testInvalidGameThrowsException() {
        int exitCode = new CommandLine(parser).execute("--game", "invalid");

        assertNotEquals(0, exitCode);

        String output = outputStream.toString();
        String error = errorStream.toString();
        String combinedOutput = output + error;

        // verify that the error message is present in one of the streams
        assertTrue(combinedOutput.contains("Invalid value for option '--game'") || 
                   combinedOutput.contains("cannot convert 'invalid' to Game") ||
                   combinedOutput.contains("No enum constant org.example.cli.GameConverter.ValidGame.INVALID"),
                   "Error message not found in output or error streams");
    }

    @Test
    void testRunMethod() {
        // The run method is required by Picocli but doesn't do anything
        // Just call it to ensure it doesn't throw exceptions
        parser.run();
    }
}
