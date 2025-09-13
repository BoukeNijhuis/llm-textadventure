package nl.boukenijhuis.cli;

import nl.boukenijhuis.provider.Gemini;
import nl.boukenijhuis.provider.Ollama;
import nl.boukenijhuis.provider.ProviderBuilder;
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
        ProviderBuilder providerBuilder = converter.convert("gemini");
        assertTrue(providerBuilder.build() instanceof Gemini);
    }

    @Test
    void convertGeminiCaseInsensitive() {
        ProviderBuilder providerBuilder = converter.convert("GEMINI");
        assertTrue(providerBuilder.build() instanceof Gemini);
    }

    // works only when you have the specified model downloaded
    @Test
    void convertOllama() {
        ProviderBuilder providerBuilder = converter.convert("ollama");
        assertTrue(providerBuilder.build() instanceof Ollama);
    }

    @Test
    void convertInvalidModelThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convert("invalid");
        });
    }
}
