import java.util.Stack;

public class Pseudocode_Command {

    enum ActionType {
        INSERT, DELETE, REPLACE
    }

    static class Command {
        ActionType type;
        int position;
        String oldText;
        String newText;

        Command(ActionType type, int position,
                String oldText, String newText) {

            this.type = type;
            this.position = position;
            this.oldText = oldText;
            this.newText = newText;
        }
    }

    static class CommandEditor {
        private String document;

        private final Stack<Command> undoStack = new Stack<>();
        private final Stack<Command> redoStack = new Stack<>();

        CommandEditor(String document) {
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

            Command command = new Command(
                    ActionType.INSERT,
                    position,
                    "",
                    newText
            );

            document = document.substring(0, position)
                    + newText
                    + document.substring(position);

            undoStack.push(command);
            redoStack.clear();
        }

        public void delete(int position, int length) {

            if (position < 0 || length < 0
                    || position + length > document.length()) {
                System.out.println("ERROR: Invalid position or length");
                return;
            }

            String oldText =
                    document.substring(position, position + length);

            Command command = new Command(
                    ActionType.DELETE,
                    position,
                    oldText,
                    ""
            );

            document = document.substring(0, position)
                    + document.substring(position + length);

            undoStack.push(command);
            redoStack.clear();
        }

        public void replace(int position, int length,
                            String newText) {

            if (newText == null || newText.isEmpty()) {
                System.out.println("ERROR: Invalid text");
                return;
            }

            if (position < 0 || length < 0
                    || position + length > document.length()) {
                System.out.println("ERROR: Invalid position or length");
                return;
            }

            String oldText =
                    document.substring(position, position + length);

            Command command = new Command(
                    ActionType.REPLACE,
                    position,
                    oldText,
                    newText
            );

            document = document.substring(0, position)
                    + newText
                    + document.substring(position + length);

            undoStack.push(command);
            redoStack.clear();
        }

        public void undo() {

            if (undoStack.isEmpty()) {
                System.out.println("Nothing to Undo");
                return;
            }

            Command command = undoStack.pop();

            if (command.type == ActionType.INSERT) {

                document = document.substring(0, command.position)
                        + document.substring(
                        command.position
                                + command.newText.length());

            } else if (command.type == ActionType.DELETE) {

                document = document.substring(0, command.position)
                        + command.oldText
                        + document.substring(command.position);

            } else if (command.type == ActionType.REPLACE) {

                document = document.substring(0, command.position)
                        + command.oldText
                        + document.substring(
                        command.position
                                + command.newText.length());
            }

            redoStack.push(command);
        }

        public void redo() {

            if (redoStack.isEmpty()) {
                System.out.println("Nothing to Redo");
                return;
            }

            Command command = redoStack.pop();

            if (command.type == ActionType.INSERT) {

                document = document.substring(0, command.position)
                        + command.newText
                        + document.substring(command.position);

            } else if (command.type == ActionType.DELETE) {

                document = document.substring(0, command.position)
                        + document.substring(
                        command.position
                                + command.oldText.length());

            } else if (command.type == ActionType.REPLACE) {

                document = document.substring(0, command.position)
                        + command.newText
                        + document.substring(
                        command.position
                                + command.oldText.length());
            }

            undoStack.push(command);
        }

        public String getDocument() {
            return document;
        }
    }

    public static void main(String[] args) {

        CommandEditor editor =
                new CommandEditor("HelloWorld");

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
