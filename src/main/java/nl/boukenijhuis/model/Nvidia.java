package nl.boukenijhuis.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;

public class Nvidia implements Model {

    @Override
    public ChatLanguageModel getChatLanguageModel() {
        return MistralAiChatModel.builder()
                .apiKey(System.getenv("NVIDIA_API_KEY"))
                .baseUrl("https://integrate.api.nvidia.com/v1")
//                .modelName("meta/llama-3.3-70b-instruct")
//                .modelName("nvidia/llama-3.1-nemotron-ultra-253b-v1")
                .modelName("nvidia/llama-3.3-nemotron-super-49b-v1")
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
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
