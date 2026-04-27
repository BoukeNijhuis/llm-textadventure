package nl.boukenijhuis.provider;

public class Nvidia extends AbstractOpenAiProvider {

    public Nvidia(String model) {
        super(model);
    }

    @Override
    public String getEnvironmentVariableValue() {
        return "NVIDIA_API_KEY";
    }

    @Override
    public String getBaseURL() {
        return "https://integrate.api.nvidia.com/v1";
    }

    @Override
    public String getDefaultModel() {
        return "mistralai/devstral-2-123b-instruct-2512";
    }

    @Override
    public String getRateLimitMessage() {
        return "{\"status\":429,\"title\":\"Too Many Requests\"}";
    }
}
