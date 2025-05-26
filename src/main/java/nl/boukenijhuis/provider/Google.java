package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;

public class Google extends AbstractProvider {

    @Override
    public ChatLanguageModel getChatLanguageModel(String model) {

        setModel(model);

        return VertexAiGeminiChatModel.builder()
                .project("voiceadventure-3cf8a")
                .location("us-central1")
                .modelName(getModel())
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "gemini-2.5-flash-preview-05-20";
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
