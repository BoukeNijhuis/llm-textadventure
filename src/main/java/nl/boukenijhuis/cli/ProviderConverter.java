package nl.boukenijhuis.cli;

import nl.boukenijhuis.provider.*;
import picocli.CommandLine;

import java.util.Arrays;

public class ProviderConverter implements CommandLine.ITypeConverter<Provider> {

    private enum ValidProvider {
        OLLAMA(new Ollama()),
        GEMINI(new Google()),
        MISTRAL(new Mistral()),
        NVIDIA(new Nvidia());

        private final Provider provider;

        ValidProvider(Provider provider) {
            this.provider = provider;
        }
    }

    public Provider convert(String value) {
        try {
            return ValidProvider.valueOf(value.toUpperCase()).provider;
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid value for option '--provider': %s.");
            throw new IllegalArgumentException(message);
        }
    }
}