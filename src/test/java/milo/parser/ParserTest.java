// src/test/java/milo/parser/ParserTest.java
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
    public void parse_byeCommand_returnsTrue() throws MiloException, IOException {
        TaskList tasks = new TaskList();
        Ui ui = new Ui();
        // Using a temporary test file
        Storage storage = new Storage("./data/test.txt");

        // Verify that 'bye' triggers the exit signal (true)
        assertTrue(Parser.parse("bye", tasks, ui, storage));
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