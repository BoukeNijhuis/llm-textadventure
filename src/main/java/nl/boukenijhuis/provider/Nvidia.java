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
        return "meta/llama-3.3-70b-instruct";
    }

    @Override
    public String getRateLimitMessage() {
        return "java.lang.RuntimeException: status code: 429";
    }
}
