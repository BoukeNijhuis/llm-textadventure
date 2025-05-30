package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatLanguageModel;

public interface Provider {

    ChatLanguageModel getChatLanguageModel();

    String getDefaultModel();

    String handleException(Exception e) throws Exception;

    default String getName() {
        return this.getClass().getSimpleName();
    }

    String getModel();
}
