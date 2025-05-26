package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class Ollama extends AbstractProvider {

    @Override
    public ChatLanguageModel getChatLanguageModel(String model) {

        setModel(model);

        return OllamaChatModel.builder()
                .modelName(getModel())
                .baseUrl("http://localhost:11434")
                .maxRetries(0)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "gemma3:12b";
    }

    @Override
    public String handleException(Exception e) throws Exception {
        throw e;
    }
}
