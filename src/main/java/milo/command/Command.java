package milo.command;

public enum Command {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND_DATE, UNKNOWN;

    public static Command fromString(String input) {
        try {
            // We'll map "find-date" to FIND_DATE
            if (input.equalsIgnoreCase("find-date")) return FIND_DATE;
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}