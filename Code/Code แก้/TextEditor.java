import java.time.LocalDateTime;
import java.util.Stack;

public class TextEditor {

    // =========================
    // Exception
    // =========================
    static class EditorException extends Exception {
        public EditorException(String message) {
            super(message);
        }
    }

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

    // =========================
    // Constructor
    // =========================
    public TextEditor(String text) throws EditorException {

    if (text == null) {
        throw new EditorException("ข้อความเริ่มต้นห้ามเป็น null");
    } else {
        document = text;
    }
}

    // =====================================================
    // INSERT - SNAPSHOT
    // =====================================================
    public boolean insertSnapshot(int position, String text)
            throws EditorException {

        if (!validPosition(position))
            throw new EditorException("Position ไม่ถูกต้อง");

        if (text == null || text.trim().isEmpty())
            throw new EditorException("ข้อความห้ามว่าง");

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
    public boolean deleteSnapshot(int position, int length)
            throws EditorException {

        if (!validRange(position, length))
            throw new EditorException(
                    "Position หรือ Length ไม่ถูกต้อง");

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
                                   String newText)
            throws EditorException {

        if (!validRange(position, length))
            throw new EditorException(
                    "Position หรือ Length ไม่ถูกต้อง");

        if (newText == null || newText.trim().isEmpty())
            throw new EditorException("ข้อความใหม่ห้ามว่าง");

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
    public boolean undoSnapshot()
            throws EditorException {

        if (snapshotUndo.isEmpty())
            throw new EditorException(
                    "ไม่สามารถ Undo ได้ เพราะ Stack ว่าง");

        snapshotRedo.push(document);
        pushCount++;

        document = snapshotUndo.pop();
        popCount++;

        return true;
    }

    // =====================================================
    // REDO - SNAPSHOT
    // =====================================================
    public boolean redoSnapshot()
            throws EditorException {

        if (snapshotRedo.isEmpty())
            throw new EditorException(
                    "ไม่สามารถ Redo ได้ เพราะ Stack ว่าง");

        snapshotUndo.push(document);
        pushCount++;

        document = snapshotRedo.pop();
        popCount++;

        return true;
    }

    // =====================================================
    // INSERT - COMMAND
    // =====================================================
    public boolean insertCommand(int position, String text)
            throws EditorException {

        if (!validPosition(position))
            throw new EditorException("Position ไม่ถูกต้อง");

        if (text == null || text.trim().isEmpty())
            throw new EditorException("ข้อความห้ามว่าง");

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
    public boolean deleteCommand(int position, int length)
            throws EditorException {

        if (!validRange(position, length))
            throw new EditorException(
                    "Position หรือ Length ไม่ถูกต้อง");

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
                                  String newText)
            throws EditorException {

        if (!validRange(position, length))
            throw new EditorException(
                    "Position หรือ Length ไม่ถูกต้อง");

        if (newText == null || newText.trim().isEmpty())
            throw new EditorException("ข้อความใหม่ห้ามว่าง");

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
    public boolean undoCommand()
            throws EditorException {

        if (commandUndo.isEmpty())
            throw new EditorException(
                    "ไม่สามารถ Undo ได้ เพราะ Stack ว่าง");

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
    public boolean redoCommand()
            throws EditorException {

        if (commandRedo.isEmpty())
            throw new EditorException(
                    "ไม่สามารถ Redo ได้ เพราะ Stack ว่าง");

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

        System.out.println("Snapshot Undo : "
                + snapshotUndo);

        System.out.println("Snapshot Redo : "
                + snapshotRedo);
    }

    public void showCommandStack() {

        System.out.println("Command Undo : "
                + commandUndo);

        System.out.println("Command Redo : "
                + commandRedo);
    }

    public void resetCounter() {

        pushCount = 0;
        popCount = 0;
        comparisonCount = 0;
    }
}