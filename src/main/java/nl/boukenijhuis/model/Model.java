package nl.boukenijhuis.model;

import dev.langchain4j.model.chat.ChatLanguageModel;

public interface Model {

    ChatLanguageModel getChatLanguageModel();

    String handleException(Exception e) throws Exception;

    default String getName() {
        return this.getClass().getSimpleName();
    }
}
