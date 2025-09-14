package nl.boukenijhuis.provider;

public class OpenRouter extends AbstractOpenAiProvider {

    public OpenRouter(String model) {
        super(model);
    }

    @Override
    public String getEnvironmentVariableValue() {
        return "OPENROUTER_API_KEY";
    }

    @Override
    public String getBaseURL() {
        return "https://openrouter.ai/api/v1";
    }

    @Override
    public String getDefaultModel() {
        return "deepseek/deepseek-chat-v3.1:free";
    }

    @Override
    public String getRateLimitMessage() {
        return "Rate limit exceeded";
    }
}
