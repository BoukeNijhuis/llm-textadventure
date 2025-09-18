package nl.boukenijhuis.provider;

public class Together extends AbstractOpenAiProvider {

    public Together(String model) {
        super(model);
    }

    @Override
    public String getEnvironmentVariableValue() {
        return "TOGETHER_API_KEY";
    }

    @Override
    public String getBaseURL() {
        return "https://api.together.xyz/v1";
    }

    @Override
    public String getDefaultModel() {
        return "openai/gpt-oss-20b";
    }

    @Override
    public String getRateLimitMessage() {
        // dummy implementation
        return "";
    }

}
