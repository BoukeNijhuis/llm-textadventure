package nl.boukenijhuis;

import nl.boukenijhuis.game.Zork;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZorkTestIT {

    @Test
    void writeAndRead() throws IOException {
        Zork zork = new Zork();
        zork.start();
        zork.read();
        String output = zork.writeAndRead("Open mailbox");
        String expected = """
                Opening the mailbox reveals:
                  A leaflet.""";
        assertEquals(expected, output);
    }

    @Test
    void read() throws IOException {
        Zork zork = new Zork();
        zork.start();
        String output = zork.read();
        String expected = """
                Welcome to Dungeon.			This version created 11-MAR-91.
                You are in an open field west of a big white house with a boarded
                front door.
                There is a small mailbox here.""";
        assertEquals(expected, output);
    }
}