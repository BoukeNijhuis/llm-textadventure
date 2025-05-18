package nl.boukenijhuis.game;

import java.io.IOException;

public interface Game {

    void start() throws IOException;

    String read();

    String writeAndRead(String input);

    String getCompletionString();

    default String getName() {
        return this.getClass().getSimpleName();
    }
}
