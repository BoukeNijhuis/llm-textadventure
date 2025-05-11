package org.example.model;

import dev.langchain4j.model.chat.ChatLanguageModel;

public interface Model {

    ChatLanguageModel getChatLanguageModel();

    String handleException(Exception e) throws Exception;

}
