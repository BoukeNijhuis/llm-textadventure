package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class Gemini extends AbstractProvider {

    public Gemini(String model) {
        super(model);
    }

    @Override
    public ChatModel getChatModel() {

        return GoogleAiGeminiChatModel.builder()
                .apiKey(System.getenv("GEMINI_API_KEY"))
                .modelName(model)
                // prevents rate limiter logging
                .maxRetries(0)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "gemini-2.5-flash-lite";
    }

    @Override
    public String getRateLimitMessage() {
        return "type.googleapis.com/google.rpc.QuotaFailure";
    }
}
