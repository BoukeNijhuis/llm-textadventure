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
        return "qwen/qwen3-next-80b-a3b-instruct";
    }

    @Override
    public String getRateLimitMessage() {
        return "{\"status\":429,\"title\":\"Too Many Requests\"}";
    }
}
