package nl.boukenijhuis.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractGame implements Game {

    protected InputStream gameOutput;
    protected OutputStream gameInput;

    protected void start(String[] commandArray) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process game = processBuilder
                .redirectErrorStream(true)
                .command(commandArray)
                .start();

        gameOutput = game.getInputStream();
        gameInput = game.getOutputStream();
    }

    public String read() {
        int availableBytes;

        try {
            do {
                availableBytes = gameOutput.available();
                sleep();
            } while (availableBytes == 0);

            return clean(new String(gameOutput.readNBytes(availableBytes)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String writeAndRead(String input) {
        writeInput(input);
        return clean(this.read());
    }

    private String clean(String s) {
        return s.replace(">", "")
                .trim();
    }

    private void writeInput(String input) {
        try {
            gameInput.write((input + System.lineSeparator()).getBytes());
            gameInput.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
