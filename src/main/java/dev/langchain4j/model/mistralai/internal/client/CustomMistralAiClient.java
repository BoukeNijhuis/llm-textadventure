//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.langchain4j.model.mistralai.internal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.mistralai.internal.api.MistralAiApi;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionChoice;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingRequest;
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbeddingResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelResponse;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolCall;
import dev.langchain4j.model.mistralai.internal.api.MistralAiUsage;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class CustomMistralAiClient extends MistralAiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMistralAiClient.class);
    private static final ObjectMapper OBJECT_MAPPER;
    private final MistralAiApi mistralAiApi;
    private final OkHttpClient okHttpClient;
    private final boolean logStreamingResponses;

    public static Builder builder() {
        return new Builder();
    }

    CustomMistralAiClient(Builder builder) {
        OkHttpClient.Builder okHttpClientBuilder = (new OkHttpClient.Builder()).callTimeout(builder.timeout).connectTimeout(builder.timeout).readTimeout(builder.timeout).writeTimeout(builder.timeout);
        okHttpClientBuilder.addInterceptor(new MistralAiApiKeyInterceptor(builder.apiKey));
        if (builder.logRequests) {
            okHttpClientBuilder.addInterceptor(new MistralAiRequestLoggingInterceptor());
        }

        if (builder.logResponses) {
            okHttpClientBuilder.addInterceptor(new MistralAiResponseLoggingInterceptor());
        }

        this.logStreamingResponses = builder.logResponses;
        this.okHttpClient = okHttpClientBuilder.build();
        Retrofit retrofit = (new Retrofit.Builder()).baseUrl(formattedUrlForRetrofit(builder.baseUrl)).client(this.okHttpClient).addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER)).build();
        this.mistralAiApi = (MistralAiApi)retrofit.create(MistralAiApi.class);
    }

    private static String formattedUrlForRetrofit(String baseUrl) {
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    public MistralAiChatCompletionResponse chatCompletion(MistralAiChatCompletionRequest request) {
        try {
            Response<MistralAiChatCompletionResponse> retrofitResponse = this.mistralAiApi.chatCompletion(request).execute();
            if (retrofitResponse.isSuccessful()) {
                return (MistralAiChatCompletionResponse)retrofitResponse.body();
            } else {
                throw this.toException(retrofitResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void streamingChatCompletion(MistralAiChatCompletionRequest request, final StreamingResponseHandler<AiMessage> handler) {
        EventSourceListener eventSourceListener = new EventSourceListener() {
            final StringBuffer contentBuilder = new StringBuffer();
            List<ToolExecutionRequest> toolExecutionRequests;
            TokenUsage tokenUsage;
            FinishReason finishReason;

            public void onOpen(EventSource eventSource, okhttp3.Response response) {
                if (CustomMistralAiClient.this.logStreamingResponses) {
                    CustomMistralAiClient.LOGGER.debug("onOpen()");
                }

            }

            public void onEvent(EventSource eventSource, String id, String type, String data) {
                if (CustomMistralAiClient.this.logStreamingResponses) {
                    CustomMistralAiClient.LOGGER.debug("onEvent() {}", data);
                }

                if ("[DONE]".equals(data)) {
                    AiMessage aiMessage;
                    if (!Utils.isNullOrEmpty(this.toolExecutionRequests)) {
                        aiMessage = AiMessage.from(this.toolExecutionRequests);
                    } else {
                        aiMessage = AiMessage.from(this.contentBuilder.toString());
                    }

                    dev.langchain4j.model.output.Response<AiMessage> response = dev.langchain4j.model.output.Response.from(aiMessage, this.tokenUsage, this.finishReason);
                    handler.onComplete(response);
                } else {
                    try {
                        MistralAiChatCompletionResponse chatCompletionResponse = (MistralAiChatCompletionResponse) CustomMistralAiClient.OBJECT_MAPPER.readValue(data, MistralAiChatCompletionResponse.class);
                        MistralAiChatCompletionChoice choice = (MistralAiChatCompletionChoice)chatCompletionResponse.getChoices().get(0);
                        String chunk = choice.getDelta().getContent();
                        if (Utils.isNotNullOrEmpty(chunk)) {
                            this.contentBuilder.append(chunk);
                            handler.onNext(chunk);
                        }

                        List<MistralAiToolCall> toolCalls = choice.getDelta().getToolCalls();
                        if (!Utils.isNullOrEmpty(toolCalls)) {
                            this.toolExecutionRequests = MistralAiMapper.toToolExecutionRequests(toolCalls);
                        }

                        MistralAiUsage usageInfo = chatCompletionResponse.getUsage();
                        if (usageInfo != null) {
                            this.tokenUsage = MistralAiMapper.tokenUsageFrom(usageInfo);
                        }

                        String finishReasonString = choice.getFinishReason();
                        if (finishReasonString != null) {
                            this.finishReason = MistralAiMapper.finishReasonFrom(finishReasonString);
                        }
                    } catch (Exception e) {
                        handler.onError(e);
                        throw new RuntimeException(e);
                    }
                }

            }

            public void onFailure(EventSource eventSource, Throwable t, okhttp3.Response response) {
                if (CustomMistralAiClient.this.logStreamingResponses) {
                    CustomMistralAiClient.LOGGER.debug("onFailure()", t);
                }

                if (t != null) {
                    handler.onError(t);
                } else {
                    handler.onError(new RuntimeException(String.format("status code: %s; body: %s", response.code(), response.body())));
                }

            }

            public void onClosed(EventSource eventSource) {
                if (CustomMistralAiClient.this.logStreamingResponses) {
                    CustomMistralAiClient.LOGGER.debug("onClosed()");
                }

            }
        };
        EventSources.createFactory(this.okHttpClient).newEventSource(this.mistralAiApi.streamingChatCompletion(request).request(), eventSourceListener);
    }

    public MistralAiEmbeddingResponse embedding(MistralAiEmbeddingRequest request) {
        try {
            Response<MistralAiEmbeddingResponse> retrofitResponse = this.mistralAiApi.embedding(request).execute();
            if (retrofitResponse.isSuccessful()) {
                return (MistralAiEmbeddingResponse)retrofitResponse.body();
            } else {
                throw this.toException(retrofitResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MistralAiModelResponse listModels() {
        try {
            Response<MistralAiModelResponse> retrofitResponse = this.mistralAiApi.models().execute();
            if (retrofitResponse.isSuccessful()) {
                return (MistralAiModelResponse)retrofitResponse.body();
            } else {
                throw this.toException(retrofitResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RuntimeException toException(Response<?> retrofitResponse) throws IOException {
        int code = retrofitResponse.code();
        if (code >= 400) {
            ResponseBody errorBody = retrofitResponse.errorBody();
            if (errorBody != null) {
                String errorBodyString = errorBody.string();
                String errorMessage = String.format("status code: %s; body: %s", code, errorBodyString);
//                LOGGER.error("Error response: {}", errorMessage);
                return new RuntimeException(errorMessage);
            }
        }

        return new RuntimeException(retrofitResponse.message());
    }

    static {
        OBJECT_MAPPER = (new ObjectMapper()).enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static class Builder extends MistralAiClient.Builder<CustomMistralAiClient, Builder> {
        public Builder() {
        }

        public CustomMistralAiClient build() {
            return new CustomMistralAiClient(this);
        }
    }
}
