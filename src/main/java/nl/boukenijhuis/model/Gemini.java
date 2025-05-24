package nl.boukenijhuis.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class Gemini implements Model {

    @Override
    public ChatLanguageModel getChatLanguageModel() {
        return VertexAiGeminiChatModel.builder()
                .project("voiceadventure-3cf8a")
                .location("us-central1")
//                .modelName("gemini-2.5-pro-exp-03-25")
                .modelName("gemini-2.5-flash-preview-05-20")
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    public String handleException(Exception e) throws Exception {
        // ignore the rate limiter
        if (e.getMessage().contains("com.google.api.gax.rpc.ResourceExhaustedException")) {
//                System.out.print("\n.");
            // no new command, just retry
            return null;
        } else {
            throw e;
        }
    }
}
