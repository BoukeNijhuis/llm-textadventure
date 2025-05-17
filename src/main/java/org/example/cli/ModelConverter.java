package org.example.cli;

import org.example.model.Gemini;
import org.example.model.Model;
import org.example.model.Ollama;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

public class ModelConverter implements CommandLine.ITypeConverter<Model> {

    private enum ValidModel {
        GEMINI(new Gemini());

        private final Model model;

        ValidModel(Model model) {
            this.model = model;
        }
    }

    public Model convert(String value) {
        Model model;
        try {
            model = ValidModel.valueOf(value.toUpperCase()).model;
        } catch (IllegalArgumentException e) {
            // no valid model found, so probably an Ollama model
            model = getOllamaModel(value);
        }
        System.out.printf("Model: %s%s", model.getName(), System.lineSeparator());
        return model;
    }

    private Model getOllamaModel(String value) {
        Model model = new Ollama(value);
        try {
            // check if it is a valid Ollama model
            model.getChatLanguageModel().generate("test");
        }
        catch (Exception ex) {
            // if not throw Exception
            throw new IllegalArgumentException("Invalid Ollama model: " + value);
        }
        return model;
    }
}