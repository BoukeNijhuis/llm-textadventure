package nl.boukenijhuis;

import static nl.boukenijhuis.Printer.print;

public class RepeatPreventer {

    private static int repeatCounter = 0;
    private static String repeatPhrase;

    public static String updateOutputWhenTheGameKeepsRepeating(String output) {
        if (repeatPhrase != null && output.startsWith(repeatPhrase)) {
            if (++repeatCounter == 4) {
                output = "The game keeps repeating. So use the HELP command.";
                // warn the spectator
                print("The repeat counter tripped! With phrase: " + repeatPhrase);

                // reset the counter & phrase
                resetCounterAndPhrase();
            }

        } else {
            resetCounterAndPhrase();
        }
        // update the repeat phrase
        repeatPhrase = output;
        return output;
    }

    private static void resetCounterAndPhrase() {
        repeatCounter = 0;
        repeatPhrase = null;
    }
}
