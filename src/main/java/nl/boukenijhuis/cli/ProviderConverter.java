package nl.boukenijhuis.cli;

import nl.boukenijhuis.provider.*;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;

public class ProviderConverter implements CommandLine.ITypeConverter<ProviderBuilder> {

    public enum ValidProvider {
        OLLAMA(new ProviderBuilder(Ollama.class)),
        GEMINI(new ProviderBuilder(Gemini.class)),
        MISTRAL(new ProviderBuilder(Mistral.class)),
        NVIDIA(new ProviderBuilder(Nvidia.class)),
        GROQ(new ProviderBuilder(Groq.class)),
        TOGETHER(new ProviderBuilder(Together.class)),
        OPENROUTER(new ProviderBuilder(OpenRouter.class));

        private final ProviderBuilder providerBuilder;

        ValidProvider(ProviderBuilder providerBuilder) {
            this.providerBuilder = providerBuilder;
        }

        static class CompletionCandidates extends ArrayList<String> {
            CompletionCandidates() {
                super(new ArrayList<>(
                        Arrays.stream(values())
                                .map(Enum::name)
                                .map(String::toLowerCase)
                                .toList()));
            }
        }
    }

    public ProviderBuilder convert(String value) {
        try {
            return ValidProvider.valueOf(value.toUpperCase()).providerBuilder;
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid value for option '--provider': %s.", value);
            throw new IllegalArgumentException(message);
        }
    }
}