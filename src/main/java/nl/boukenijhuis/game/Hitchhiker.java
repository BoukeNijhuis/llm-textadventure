package nl.boukenijhuis.game;

import java.io.IOException;

public class Hitchhiker extends AbstractGame {

    @Override
    public void start() throws IOException {
        this.start(new String[]{"dfrotz", "/Users/boukenijhuis/hitchhiker-invclues-r31-s871119.z5"});
    }

    @Override
    public String getCompletionString() {
        return "The game is over.";
    }
}
