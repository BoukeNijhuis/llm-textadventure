package org.example.cli;

import org.example.model.Gemini;
import org.example.model.Model;
import org.example.model.Ollama;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelConverterTest {

    private ModelConverter converter;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;


    @BeforeEach
    void setUp() {
        converter = new ModelConverter();

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
        Model model = converter.convert("gemini");
        assertTrue(model instanceof Gemini);
        assertTrue(outputStream.toString().contains("Model: Gemini"));
    }

    @Test
    void convertGeminiCaseInsensitive() {
        Model model = converter.convert("GEMINI");
        assertTrue(model instanceof Gemini);
        assertTrue(outputStream.toString().contains("Model: Gemini"));
    }

    // works only when you have the specified model downloaded
    @Test
    void convertOllama() {
        Model model = converter.convert("gemma3:12b");
        assertTrue(model instanceof Ollama);
        assertTrue(outputStream.toString().contains("Model: Ollama with gemma3:12b"));
    }

    @Test
    void convertInvalidModelThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convert("invalid");
        });
        assertTrue(exception.getMessage().contains("Invalid"));
    }
}
