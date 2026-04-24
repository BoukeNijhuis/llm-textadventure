package nl.boukenijhuis.provider;

public class Anthropic extends AbstractOpenAiProvider {

    public Anthropic(String model) {
        super(model);
    }

    @Override
    public String getEnvironmentVariableValue() {
        return "ANTHROPIC_API_KEY";
    }

    @Override
    public String getBaseURL() {
        return "https://api.anthropic.com/v1";
    }

    @Override
    public String getDefaultModel() {
        return "claude-haiku-4-5";
    }

    @Override
    public String getRateLimitMessage() {
        // dummy implementation
        return "";
    }

}
