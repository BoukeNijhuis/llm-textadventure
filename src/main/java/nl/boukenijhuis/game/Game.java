package nl.boukenijhuis.game;

import java.io.IOException;

public interface Game extends AutoCloseable {

    void start() throws IOException;

    String read();

    String writeAndRead(String input);

    String getCompletionString();

    default String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    default void close() throws IOException {
    }
}
