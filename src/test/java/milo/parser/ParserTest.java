package milo.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import milo.task.TaskList;
import milo.ui.Ui;
import milo.storage.Storage;
import milo.exception.MiloException;
import java.io.IOException;

public class ParserTest {
    @Test
    public void parse_byeCommand_returnsExitMessage() throws MiloException, IOException {
        TaskList tasks = new TaskList();
        Ui ui = new Ui();
        // Using a temporary test file for the test
        Storage storage = new Storage("./data/test.txt");

        // Get the actual response string from the parser
        String response = Parser.parse("bye", tasks, ui, storage);

        // Verify that the response contains the exit keyword
        // This fixes the 'String cannot be converted to boolean' error
        assertTrue(response.contains("Bye"), "The response should contain the word 'Bye'");
    }

    @Test
    public void parse_invalidCommand_throwsMiloException() {
        TaskList tasks = new TaskList();
        Ui ui = new Ui();
        Storage storage = new Storage("./data/test.txt");

        assertThrows(MiloException.class, () -> {
            Parser.parse("blahblah", tasks, ui, storage);
        });
    }
}