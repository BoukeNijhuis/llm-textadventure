package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class Gemini extends AbstractProvider {

    public Gemini(String model) {
        super(model);
    }

    @Override
    public ChatLanguageModel getChatLanguageModel() {

        return VertexAiGeminiChatModel.builder()
                .project("voiceadventure-3cf8a")
                .location("us-central1")
                .modelName(model)
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "gemini-2.5-flash-preview-05-20";
    }

    @Override
    public String getRateLimitMessage() {
        return "com.google.api.gax.rpc.ResourceExhaustedException";
    }
}
