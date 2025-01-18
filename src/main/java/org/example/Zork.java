package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Zork {

    private static InputStream zorkOutput;
    private static OutputStream zorkInput;

    public void start() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process zork = processBuilder
                .redirectErrorStream(true)
                .command("zork")
                .start();

        zorkOutput = zork.getInputStream();
        zorkInput = zork.getOutputStream();
    }

    public String writeAndRead(String input) {
        writeInput(input);
        return clean(getOutput());
    }

    public static String clean(String s) {
        return s.replace(">", "")
                .replace("\n", " ")
                .replace("  ", " ")
                .replace("\t\tThis version created 11-MAR-91.", "")
                ;
    }

    public void writeInput(String input) {
        try {
            zorkInput.write((input + System.lineSeparator()).getBytes());
            zorkInput.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOutput() {
        int availableBytes;

        try {
            do {
                availableBytes = zorkOutput.available();
                sleep();
            } while (availableBytes == 0);

            return new String(zorkOutput.readNBytes(availableBytes));
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
}
