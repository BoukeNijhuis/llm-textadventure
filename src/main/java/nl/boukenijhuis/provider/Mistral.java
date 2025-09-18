package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;

public class Mistral extends AbstractProvider {

    public Mistral(String model) {
        super(model);
    }

    @Override
    public ChatModel getChatModel() {

        return MistralAiChatModel.builder()
                .apiKey(System.getenv("MISTRAL_AI_API_KEY"))
                .modelName(model)
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "mistral-small-latest";
    }

    @Override
    public String getRateLimitMessage() {
        return "java.lang.RuntimeException: status code: 429";
    }
}
