package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;

public class Nvidia extends AbstractProvider {

    public Nvidia(String model) {
        super(model);
    }

    @Override
    public ChatLanguageModel getChatLanguageModel() {

        return MistralAiChatModel.builder()
                .apiKey(System.getenv("NVIDIA_API_KEY"))
                .baseUrl("https://integrate.api.nvidia.com/v1")
                .modelName("")
                .modelName(model)
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "meta/llama-3.3-70b-instruct";
    }

    public String handleException(Exception e) throws Exception {
        // ignore the rate limiter
        if (e.getMessage().contains("java.lang.RuntimeException: status code: 429;")) {
//                System.out.print("\n.");
            // no new command, just retry
            return null;
        } else {
            throw e;
        }
    }
}
