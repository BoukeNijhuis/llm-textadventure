package nl.boukenijhuis.cli;

import nl.boukenijhuis.game.Game;
import nl.boukenijhuis.game.Hitchhiker;
import nl.boukenijhuis.game.Zork;
import nl.boukenijhuis.provider.Gemini;
import nl.boukenijhuis.provider.ProviderBuilder;
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
    }

    @Test
    void testProvider() {
        new CommandLine(parser).execute("--provider", "gemini");

        ProviderBuilder providerBuilder = parser.getProviderBuilder();
        assertNotNull(providerBuilder);
        assertTrue(providerBuilder.model(parser.getModel()).build() instanceof Gemini);
    }

    @Test
    void testSetBothGameAndProvider() {
        new CommandLine(parser).execute("--game", "hitchhiker", "--provider", "gemini");

        Game game = parser.getGame();
        assertNotNull(game);
        assertTrue(game instanceof Hitchhiker);

        ProviderBuilder providerBuilder = parser.getProviderBuilder();
        assertNotNull(providerBuilder);
        assertTrue(providerBuilder.model(parser.getModel()).build() instanceof Gemini);
    }

    @Test
    void testInvalidGameThrowsException() {
        int exitCode = new CommandLine(parser).execute("--game", "invalid");

        assertNotEquals(0, exitCode);

        String output = outputStream.toString();
        String error = errorStream.toString();
        String combinedOutput = output + error;

        // verify that the error message is present in one of the streams
        assertTrue(combinedOutput.contains("Invalid value for option '--game'"));
    }

    @Test
    void testRunMethod() {
        // The run method is required by Picocli but doesn't do anything
        // Just call it to ensure it doesn't throw exceptions
        parser.run();
    }
}
