package nl.boukenijhuis.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class Mistral implements Model {

    @Override
    public ChatLanguageModel getChatLanguageModel() {
        return MistralAiChatModel.builder()
                .apiKey(System.getenv("MISTRAL_AI_API_KEY"))
                .modelName("mistral-small-latest")
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    public String handleException(Exception e) throws Exception {
        // ignore the rate limiter
        if (e.getMessage().contains("java.lang.RuntimeException: status code: 429; body: {\"message\":\"Requests rate limit exceeded\"}")) {
//                System.out.print("\n.");
            // no new command, just retry
            return null;
        } else {
            throw e;
        }
    }
}
