package milo.command;

/**
 * Represents the various commands supported by the Milo application.
 */
public enum Command {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND_DATE, UNKNOWN;

    /**
     * Translates a raw string input into a Command enum constant.
     *
     * @param input The command word provided by the user.
     * @return The corresponding Command constant, or UNKNOWN if invalid.
     */
    public static Command fromString(String input) {
        try {
            if (input.equalsIgnoreCase("find-date")) {
                return FIND_DATE;
            }
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}