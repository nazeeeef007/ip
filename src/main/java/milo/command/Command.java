package milo.command;

public enum Command {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND_DATE, FIND, UNKNOWN;

    public static Command fromString(String input) {
        try {
            if (input.equalsIgnoreCase("find-date")) {
                return FIND_DATE;
            }
            // Standardizes input to match enum names
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}