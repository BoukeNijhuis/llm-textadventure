package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class Groq extends AbstractOpenAiProvider {

    public Groq(String model) {
        super(model);
    }

    @Override
    public String getEnvironmentVariableValue() {
        return "GROQ_API_KEY";
    }

    @Override
    public String getBaseURL() {
        return "https://api.groq.com/openai/v1";
    }

    @Override
    public String getDefaultModel() {
        return "llama-3.3-70b-versatile";
    }

    @Override
    public String getRateLimitMessage() {
        return "Rate limit reached";
    }
}
