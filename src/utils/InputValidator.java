package utils;

import models.TextDocument;

public class InputValidator {

    private InputValidator() {
        // Utility class
    }

    public static boolean isValidInsertPosition(
            TextDocument document,
            int position
    ) {
        if (document == null) {
            return false;
        }

        return document.isValidInsertPosition(position);
    }

    public static boolean isValidRange(
            TextDocument document,
            int position,
            int length
    ) {
        if (document == null) {
            return false;
        }

        return document.isValidRange(position, length);
    }

    public static boolean isValidText(String text) {
        return text != null;
    }

    public static boolean isValidNonNegative(int value) {
        return value >= 0;
    }

    public static boolean isInteger(String input) {

        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}