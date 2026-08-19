package models;

public class TextDocument {

    private StringBuilder text;

    public TextDocument() {
        this.text = new StringBuilder();
    }

    public TextDocument(String initialText) {
        if (initialText == null) {
            initialText = "";
        }

        this.text = new StringBuilder(initialText);
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String newText) {
        if (newText == null) {
            newText = "";
        }

        text.setLength(0);
        text.append(newText);
    }

    public int length() {
        return text.length();
    }

    public boolean isEmpty() {
        return text.length() == 0;
    }

    public boolean isValidInsertPosition(int position) {
        return position >= 0 && position <= text.length();
    }

    public boolean isValidRange(int position, int length) {
        return position >= 0
                && length >= 0
                && position + length <= text.length();
    }

    public String getSubstring(int position, int length) {
        if (!isValidRange(position, length)) {
            throw new IllegalArgumentException("Invalid position or length.");
        }

        return text.substring(position, position + length);
    }

    public boolean insert(int position, String newText) {

        if (!isValidInsertPosition(position)) {
            return false;
        }

        if (newText == null) {
            newText = "";
        }

        text.insert(position, newText);
        return true;
    }

    public boolean delete(int position, int length) {

        if (!isValidRange(position, length)) {
            return false;
        }

        text.delete(position, position + length);
        return true;
    }

    public boolean replace(
            int position,
            int length,
            String newText
    ) {

        if (!isValidRange(position, length)) {
            return false;
        }

        if (newText == null) {
            newText = "";
        }

        text.replace(
                position,
                position + length,
                newText
        );

        return true;
    }

    @Override
    public String toString() {
        return text.toString();
    }
}