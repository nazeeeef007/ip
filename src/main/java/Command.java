public enum Command {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN;

    /**
     * Converts a string input to a Command enum.
     * @param input The first word of the user's input.
     * @return The corresponding Command or UNKNOWN.
     */
    public static Command fromString(String input) {
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}