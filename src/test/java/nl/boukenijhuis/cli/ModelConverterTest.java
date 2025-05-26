package nl.boukenijhuis.cli;

import nl.boukenijhuis.provider.Google;
import nl.boukenijhuis.provider.Provider;
import nl.boukenijhuis.provider.Ollama;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelConverterTest {

    private ProviderConverter converter;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;


    @BeforeEach
    void setUp() {
        converter = new ProviderConverter();

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
    void convertGemini() {
        Provider model = converter.convert("gemini");
        assertTrue(model instanceof Google);
    }

    @Test
    void convertGeminiCaseInsensitive() {
        Provider model = converter.convert("GEMINI");
        assertTrue(model instanceof Google);
    }

    // works only when you have the specified model downloaded
    @Test
    void convertOllama() {
        Provider model = converter.convert("ollama");
        assertTrue(model instanceof Ollama);
    }

    @Test
    void convertInvalidModelThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convert("invalid");
        });
    }
}
