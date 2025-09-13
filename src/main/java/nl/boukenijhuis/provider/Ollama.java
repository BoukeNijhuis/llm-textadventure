package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class Ollama extends AbstractProvider {

    public Ollama(String model) {
        super(model);
    }

    @Override
    public ChatLanguageModel getChatLanguageModel() {
        return OllamaChatModel.builder()
                .modelName(model)
                .baseUrl("http://localhost:11434")
                .maxRetries(0)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "gemma3:12b";
    }

    @Override
    public String getRateLimitMessage() {
        // dummy implementation
        return "";
    }
}
