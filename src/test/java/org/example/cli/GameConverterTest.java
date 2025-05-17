package org.example.cli;

import org.example.game.Game;
import org.example.game.VoiceAdventure;
import org.example.game.Zork;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameConverterTest {

    private GameConverter converter;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        converter = new GameConverter();

        // capture System.out for assertions
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // restore System.out
        System.setOut(originalOut);
    }

    @Test
    void convertZork() {
        Game game = converter.convert("zork");
        assertTrue(game instanceof Zork);
        assertTrue(outputStream.toString().contains("Game: Zork"));
    }

    @Test
    void convertVoiceAdventure() {
        Game game = converter.convert("voiceadventure");
        assertTrue(game instanceof VoiceAdventure);
        assertTrue(outputStream.toString().contains("Game: VoiceAdventure"));
    }

    @Test
    void convertCaseInsensitive() {
        Game game = converter.convert("ZORK");
        assertTrue(game instanceof Zork);
        assertTrue(outputStream.toString().contains("Game: Zork"));
    }

    @Test
    void convertInvalidGameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convert("invalid");
        });
    }

    @Test
    void testAsTypeConverter() throws Exception {
        // Test that the converter works with Picocli's CommandLine
        CommandLine.ITypeConverter<Game> typeConverter = new GameConverter();

        // Test converting "zork" to a Zork game
        Game game = typeConverter.convert("zork");

        // Verify the game is a Zork instance
        assertTrue(game instanceof Zork);
    }
}
