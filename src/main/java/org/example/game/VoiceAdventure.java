package org.example.game;

import java.io.IOException;

public class VoiceAdventure extends AbstractGame {

    public void start() throws IOException {
        this.start(new String[]{"java", "-jar", "/Users/boukenijhuis/git/voice-adventure/target/voice-adventure-1.0-SNAPSHOT-jar-with-dependencies.jar"});
    }

    @Override
    public String getCompletionString() {
        return "You finished the game!";
    }
}
