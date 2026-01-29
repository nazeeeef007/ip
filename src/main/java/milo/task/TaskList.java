package milo.task;

import java.util.ArrayList;

/**
 * Manages an in-memory list of tasks.
 * Provides methods to add, delete, and retrieve tasks from the collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an existing collection of tasks.
     * Used primarily when loading tasks from storage.
     *
     * @param tasks An ArrayList of tasks to initialize the list.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param t The task to be added.
     */
    public void addTask(Task t) {
        tasks.add(t);
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to be removed.
     * @return The task that was removed.
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the full list of tasks.
     *
     * @return An ArrayList containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the specified index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Finds tasks that contain the specified keyword in their description.
     *
     * @param keyword The string to search for.
     * @return A list of tasks that match the keyword.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.toString().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}