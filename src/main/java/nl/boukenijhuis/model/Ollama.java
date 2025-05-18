package nl.boukenijhuis.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class Ollama implements Model {

    private final String modelName;

    public Ollama(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public ChatLanguageModel getChatLanguageModel() {
        return OllamaChatModel.builder()
                .modelName(modelName)
                .baseUrl("http://localhost:11434")
                .maxRetries(0)
                .build();
    }

    @Override
    public String handleException(Exception e) throws Exception {
        throw e;
    }

    @Override
    public String getName() {
        return String.format("Ollama with %s", modelName);
    }
}
