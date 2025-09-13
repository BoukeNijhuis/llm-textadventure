package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class Together extends AbstractProvider {

    public Together(String model) {
        super(model);
    }

    @Override
    public ChatLanguageModel getChatLanguageModel() {

        return OpenAiChatModel.builder()
                .apiKey(System.getenv("TOGETHER_API_KEY"))
                .baseUrl("https://api.together.xyz/v1")
                .modelName(model)
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "openai/gpt-oss-20b";
    }

}
