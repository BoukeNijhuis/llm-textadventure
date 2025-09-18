package nl.boukenijhuis.provider;

import dev.langchain4j.model.chat.ChatModel;

public interface Provider {

    ChatModel getChatModel();

    String getDefaultModel();

    String handleException(Exception e) throws Exception;

    default String getName() {
        return this.getClass().getSimpleName();
    }

    String getModel();
}
