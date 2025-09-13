package nl.boukenijhuis.cli;

import nl.boukenijhuis.provider.*;
import picocli.CommandLine;

public class ProviderConverter implements CommandLine.ITypeConverter<ProviderBuilder> {

    private enum ValidProvider {
        OLLAMA(new ProviderBuilder(Ollama.class)),
        GEMINI(new ProviderBuilder(Gemini.class)),
        MISTRAL(new ProviderBuilder(Mistral.class)),
        NVIDIA(new ProviderBuilder(Nvidia.class)),
        GROQ(new ProviderBuilder(Groq.class)),
        TOGETHER(new ProviderBuilder(Together.class));

        private final ProviderBuilder providerBuilder;

        ValidProvider(ProviderBuilder providerBuilder) {
            this.providerBuilder = providerBuilder;
        }
    }

    public ProviderBuilder convert(String value) {
        try {
            return ValidProvider.valueOf(value.toUpperCase()).providerBuilder;
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid value for option '--provider': %s.");
            throw new IllegalArgumentException(message);
        }
    }
}