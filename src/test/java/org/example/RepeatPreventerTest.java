package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepeatPreventerTest {

    @Test
    public void happyFlow() {
        String input = "input";
        String output;
        for (int i = 0; i < 4; i++) {
            output = RepeatPreventer.updateOutputWhenTheGameKeepsRepeating(input);
            assertEquals(output, input);
        }
        output = RepeatPreventer.updateOutputWhenTheGameKeepsRepeating(input);
        assertNotEquals(input, output);
    }

}