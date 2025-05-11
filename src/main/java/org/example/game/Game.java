package org.example.game;

import java.io.IOException;

public interface Game {

    void start() throws IOException;

    String read();

    String writeAndRead(String input);

    String getCompletionString();
}
