import java.time.LocalDateTime;
import java.util.Stack;

public class TextEditor {

    // =========================
    // Action สำหรับ Algorithm B
    // =========================
    static class Action {
        String id, type, oldText, newText;
        int position;
        LocalDateTime time;

        Action(String id, String type, int position,
               String oldText, String newText) {
            this.id = id;
            this.type = type;
            this.position = position;
            this.oldText = oldText;
            this.newText = newText;
            this.time = LocalDateTime.now();
        }

        public String toString() {
            return id + " " + type +
                    " pos=" + position +
                    " old=" + oldText +
                    " new=" + newText;
        }
    }

    // =========================
    // ข้อมูลเอกสาร
    // =========================
    private String document;

    // =========================
    // Algorithm A : Snapshot
    // =========================
    private Stack<String> snapshotUndo = new Stack<>();
    private Stack<String> snapshotRedo = new Stack<>();

    // =========================
    // Algorithm B : Command
    // =========================
    private Stack<Action> commandUndo = new Stack<>();
    private Stack<Action> commandRedo = new Stack<>();

    private int actionId = 1;

    // นับ Operation
    private long pushCount = 0;
    private long popCount = 0;
    private long comparisonCount = 0;

    public TextEditor(String text) {
        document = (text == null) ? "" : text;
    }

    // =====================================================
    // INSERT - SNAPSHOT
    // =====================================================
    public boolean insertSnapshot(int position, String text) {

        if (!validPosition(position) || text == null || text.isEmpty())
            return false;

        snapshotUndo.push(document);
        pushCount++;

        document = document.substring(0, position)
                + text
                + document.substring(position);

        snapshotRedo.clear();
        return true;
    }

    // =====================================================
    // DELETE - SNAPSHOT
    // =====================================================
    public boolean deleteSnapshot(int position, int length) {

        if (!validRange(position, length))
            return false;

        snapshotUndo.push(document);
        pushCount++;

        document = document.substring(0, position)
                + document.substring(position + length);

        snapshotRedo.clear();
        return true;
    }

    // =====================================================
    // REPLACE - SNAPSHOT
    // =====================================================
    public boolean replaceSnapshot(int position, int length,
                                   String newText) {

        if (!validRange(position, length)
                || newText == null)
            return false;

        snapshotUndo.push(document);
        pushCount++;

        document = document.substring(0, position)
                + newText
                + document.substring(position + length);

        snapshotRedo.clear();
        return true;
    }

    // =====================================================
    // UNDO - SNAPSHOT
    // =====================================================
    public boolean undoSnapshot() {

        if (snapshotUndo.isEmpty())
            return false;

        snapshotRedo.push(document);
        pushCount++;

        document = snapshotUndo.pop();
        popCount++;

        return true;
    }

    // =====================================================
    // REDO - SNAPSHOT
    // =====================================================
    public boolean redoSnapshot() {

        if (snapshotRedo.isEmpty())
            return false;

        snapshotUndo.push(document);
        pushCount++;

        document = snapshotRedo.pop();
        popCount++;

        return true;
    }

    // =====================================================
    // INSERT - COMMAND
    // =====================================================
    public boolean insertCommand(int position, String text) {

        if (!validPosition(position) || text == null || text.isEmpty())
            return false;

        document = document.substring(0, position)
                + text
                + document.substring(position);

        Action action = new Action(
                "A" + actionId++,
                "INSERT",
                position,
                "",
                text
        );

        commandUndo.push(action);
        pushCount++;

        commandRedo.clear();

        return true;
    }

    // =====================================================
    // DELETE - COMMAND
    // =====================================================
    public boolean deleteCommand(int position, int length) {

        if (!validRange(position, length))
            return false;

        String oldText =
                document.substring(position, position + length);

        document = document.substring(0, position)
                + document.substring(position + length);

        Action action = new Action(
                "A" + actionId++,
                "DELETE",
                position,
                oldText,
                ""
        );

        commandUndo.push(action);
        pushCount++;

        commandRedo.clear();

        return true;
    }

    // =====================================================
    // REPLACE - COMMAND
    // =====================================================
    public boolean replaceCommand(int position, int length,
                                   String newText) {

        if (!validRange(position, length)
                || newText == null)
            return false;

        String oldText =
                document.substring(position, position + length);

        document = document.substring(0, position)
                + newText
                + document.substring(position + length);

        Action action = new Action(
                "A" + actionId++,
                "REPLACE",
                position,
                oldText,
                newText
        );

        commandUndo.push(action);
        pushCount++;

        commandRedo.clear();

        return true;
    }

    // =====================================================
    // UNDO - COMMAND
    // =====================================================
    public boolean undoCommand() {

        if (commandUndo.isEmpty())
            return false;

        Action a = commandUndo.pop();
        popCount++;

        commandRedo.push(a);
        pushCount++;

        if (a.type.equals("INSERT")) {

            // INSERT -> DELETE
            document = document.substring(0, a.position)
                    + document.substring(
                    a.position + a.newText.length());

        } else if (a.type.equals("DELETE")) {

            // DELETE -> INSERT
            document = document.substring(0, a.position)
                    + a.oldText
                    + document.substring(a.position);

        } else {

            // REPLACE -> oldText
            document = document.substring(0, a.position)
                    + a.oldText
                    + document.substring(
                    a.position + a.newText.length());
        }

        return true;
    }

    // =====================================================
    // REDO - COMMAND
    // =====================================================
    public boolean redoCommand() {

        if (commandRedo.isEmpty())
            return false;

        Action a = commandRedo.pop();
        popCount++;

        commandUndo.push(a);
        pushCount++;

        if (a.type.equals("INSERT")) {

            document = document.substring(0, a.position)
                    + a.newText
                    + document.substring(a.position);

        } else if (a.type.equals("DELETE")) {

            document = document.substring(0, a.position)
                    + document.substring(
                    a.position + a.oldText.length());

        } else {

            document = document.substring(0, a.position)
                    + a.newText
                    + document.substring(
                    a.position + a.oldText.length());
        }

        return true;
    }

    // =====================================================
    // ตรวจสอบ Position
    // =====================================================
    private boolean validPosition(int position) {

        comparisonCount++;

        return position >= 0
                && position <= document.length();
    }

    // =====================================================
    // ตรวจสอบ Range
    // =====================================================
    private boolean validRange(int position, int length) {

        comparisonCount++;

        return position >= 0
                && length >= 0
                && position + length <= document.length();
    }

    // =====================================================
    // Getter
    // =====================================================
    public String getDocument() {
        return document;
    }

    public long getPushCount() {
        return pushCount;
    }

    public long getPopCount() {
        return popCount;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    // =====================================================
    // แสดง Stack
    // =====================================================
    public void showSnapshotStack() {

        System.out.println("Snapshot Undo : " + snapshotUndo);
        System.out.println("Snapshot Redo : " + snapshotRedo);
    }

    public void showCommandStack() {

        System.out.println("Command Undo : " + commandUndo);
        System.out.println("Command Redo : " + commandRedo);
    }

    public void resetCounter() {
        pushCount = 0;
        popCount = 0;
        comparisonCount = 0;
    }
}