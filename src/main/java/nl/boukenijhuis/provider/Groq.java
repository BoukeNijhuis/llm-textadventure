package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class Groq extends AbstractProvider {

    public Groq(String model) {
        super(model);
    }

    @Override
    public ChatLanguageModel getChatLanguageModel() {

        return OpenAiChatModel.builder()
                // TODO: put in env variable
                .apiKey(System.getenv("GROQ_API_KEY"))
                .baseUrl("https://api.groq.com/openai/v1")
                .modelName(model)
                // prevents rate limiter logging
                .maxRetries(1)
                .build();
    }

    @Override
    public String getDefaultModel() {
        return "llama-3.3-70b-versatile";
    }

    public String handleException(Exception e) throws Exception {
        // TODO implement
        throw e;
    }
}
