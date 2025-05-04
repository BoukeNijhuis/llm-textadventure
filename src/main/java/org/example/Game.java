package org.example;

import java.io.IOException;

public interface Game {

    void start() throws IOException;

    String read();

    String writeAndRead(String input);

    String getCompletionString();
}
