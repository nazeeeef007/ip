// src/test/java/milo/task/TodoTest.java
package milo.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {
    @Test
    public void testStringConversion() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void testMarkAsDone() {
        Todo todo = new Todo("read book");
        todo.markAsDone();
        // Ensure this matches your Task class's status icon for 'done'
        assertEquals("[T][X] read book", todo.toString());
    }
}