import java.util.Stack;

public class Pseudocode_Snapshot {

    static class SnapshotEditor {
        private String document;
        private final Stack<String> undoStack = new Stack<>();
        private final Stack<String> redoStack = new Stack<>();

        SnapshotEditor(String document) {
            this.document = document;
        }

        public void insert(int position, String newText) {
            if (newText == null || newText.isEmpty()) {
                System.out.println("ERROR: Invalid text");
                return;
            }

            if (position < 0 || position > document.length()) {
                System.out.println("ERROR: Invalid position");
                return;
            }

            undoStack.push(document);
            redoStack.clear();

            document = document.substring(0, position)
                    + newText
                    + document.substring(position);
        }

        public void delete(int position, int length) {
            if (position < 0 || length < 0
                    || position + length > document.length()) {
                System.out.println("ERROR: Invalid position or length");
                return;
            }

            undoStack.push(document);
            redoStack.clear();

            document = document.substring(0, position)
                    + document.substring(position + length);
        }

        public void replace(int position, int length, String newText) {
            if (newText == null || newText.isEmpty()) {
                System.out.println("ERROR: Invalid text");
                return;
            }

            if (position < 0 || length < 0
                    || position + length > document.length()) {
                System.out.println("ERROR: Invalid position or length");
                return;
            }

            undoStack.push(document);
            redoStack.clear();

            document = document.substring(0, position)
                    + newText
                    + document.substring(position + length);
        }

        public void undo() {
            if (undoStack.isEmpty()) {
                System.out.println("Nothing to Undo");
                return;
            }

            redoStack.push(document);
            document = undoStack.pop();
        }

        public void redo() {
            if (redoStack.isEmpty()) {
                System.out.println("Nothing to Redo");
                return;
            }

            undoStack.push(document);
            document = redoStack.pop();
        }

        public String getDocument() {
            return document;
        }
    }

    public static void main(String[] args) {

        SnapshotEditor editor =
                new SnapshotEditor("HelloWorld");

        System.out.println("Initial: "
                + editor.getDocument());

        editor.insert(5, "AI");
        System.out.println("After INSERT: "
                + editor.getDocument());

        editor.undo();
        System.out.println("After UNDO: "
                + editor.getDocument());

        editor.redo();
        System.out.println("After REDO: "
                + editor.getDocument());

        editor.replace(5, 5, "Earth");
        System.out.println("After REPLACE: "
                + editor.getDocument());

        editor.undo();
        System.out.println("After UNDO: "
                + editor.getDocument());

        editor.delete(5, 5);
        System.out.println("After DELETE: "
                + editor.getDocument());
    }
}
