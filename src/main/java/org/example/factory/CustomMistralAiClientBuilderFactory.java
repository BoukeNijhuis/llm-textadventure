package org.example.factory;

import dev.langchain4j.model.mistralai.internal.client.CustomMistralAiClient;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClient;
import dev.langchain4j.model.mistralai.internal.client.MistralAiClientBuilderFactory;

public class CustomMistralAiClientBuilderFactory implements MistralAiClientBuilderFactory {
    @Override
    public MistralAiClient.Builder get() {
        return CustomMistralAiClient.builder();
    }
}
