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
        return "meta-llama/llama-3.3-70b-instruct:free";
    }

    @Override
    public String getRateLimitMessage() {
        return "Rate limit exceeded";
    }
}
