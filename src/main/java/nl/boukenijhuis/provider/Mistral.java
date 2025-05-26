package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;

public class Mistral extends AbstractProvider {

    @Override
    public ChatLanguageModel getChatLanguageModel(String model) {

        setModel(model);

        return MistralAiChatModel.builder()
                .apiKey(System.getenv("MISTRAL_AI_API_KEY"))
                .modelName(getModel())
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "mistral-small-latest";
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
