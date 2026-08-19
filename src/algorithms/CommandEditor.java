package algorithms;

import java.util.ArrayDeque;
import java.util.Deque;

import models.Action;
import models.ActionType;
import models.TextDocument;
import utils.OperationCounter;

public class CommandEditor {

    private TextDocument document;

    private Deque<Action> undoStack;
    private Deque<Action> redoStack;

    private OperationCounter counter;

    private int nextActionId;

    public CommandEditor() {
        this("");
    }

    public CommandEditor(String initialText) {

        document = new TextDocument(initialText);

        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();

        counter = new OperationCounter();

        nextActionId = 1;
    }

    public boolean insert(int position, String newText) {

        if (!document.isValidInsertPosition(position)) {
            counter.incrementComparison();
            return false;
        }

        if (newText == null) {
            return false;
        }

        boolean success = document.insert(position, newText);

        if (!success) {
            return false;
        }

        Action action = new Action(
                nextActionId++,
                ActionType.INSERT,
                position,
                "",
                newText
        );

        undoStack.push(action);
        counter.incrementPush();

        // Action ใหม่ต้องล้าง Redo
        redoStack.clear();

        counter.incrementMove();

        return true;
    }

    public boolean delete(int position, int length) {

        if (!document.isValidRange(position, length)) {
            counter.incrementComparison();
            return false;
        }

        String oldText =
                document.getSubstring(position, length);

        boolean success =
                document.delete(position, length);

        if (!success) {
            return false;
        }

        Action action = new Action(
                nextActionId++,
                ActionType.DELETE,
                position,
                oldText,
                ""
        );

        undoStack.push(action);
        counter.incrementPush();

        // Action ใหม่ต้องล้าง Redo
        redoStack.clear();

        counter.incrementMove();

        return true;
    }

    public boolean replace(
            int position,
            int length,
            String newText
    ) {

        if (!document.isValidRange(position, length)) {
            counter.incrementComparison();
            return false;
        }

        if (newText == null) {
            return false;
        }

        String oldText =
                document.getSubstring(position, length);

        boolean success =
                document.replace(
                        position,
                        length,
                        newText
                );

        if (!success) {
            return false;
        }

        Action action = new Action(
                nextActionId++,
                ActionType.REPLACE,
                position,
                oldText,
                newText
        );

        undoStack.push(action);
        counter.incrementPush();

        // Action ใหม่ต้องล้าง Redo
        redoStack.clear();

        counter.incrementMove();

        return true;
    }

    public boolean undo() {

        if (undoStack.isEmpty()) {
            counter.incrementComparison();
            return false;
        }

        Action action = undoStack.pop();
        counter.incrementPop();

        boolean success =
                applyInverseOperation(action);

        if (!success) {
            // หากเกิดข้อผิดพลาด ให้ Action กลับเข้า Undo
            undoStack.push(action);
            counter.incrementPush();

            return false;
        }

        redoStack.push(action);
        counter.incrementPush();

        counter.incrementMove();

        return true;
    }

    public boolean redo() {

        if (redoStack.isEmpty()) {
            counter.incrementComparison();
            return false;
        }

        Action action = redoStack.pop();
        counter.incrementPop();

        boolean success =
                applyForwardOperation(action);

        if (!success) {

            redoStack.push(action);
            counter.incrementPush();

            return false;
        }

        undoStack.push(action);
        counter.incrementPush();

        counter.incrementMove();

        return true;
    }

    private boolean applyInverseOperation(Action action) {

        ActionType type = action.getActionType();

        switch (type) {

            case INSERT:

                // INSERT → DELETE
                return document.delete(
                        action.getPosition(),
                        action.getNewText().length()
                );

            case DELETE:

                // DELETE → INSERT Old Text
                return document.insert(
                        action.getPosition(),
                        action.getOldText()
                );

            case REPLACE:

                // REPLACE New → Old
                return document.replace(
                        action.getPosition(),
                        action.getNewText().length(),
                        action.getOldText()
                );

            default:
                return false;
        }
    }

    private boolean applyForwardOperation(Action action) {

        ActionType type = action.getActionType();

        switch (type) {

            case INSERT:

                // INSERT New Text
                return document.insert(
                        action.getPosition(),
                        action.getNewText()
                );

            case DELETE:

                // DELETE Old Text
                return document.delete(
                        action.getPosition(),
                        action.getOldText().length()
                );

            case REPLACE:

                // REPLACE Old → New
                return document.replace(
                        action.getPosition(),
                        action.getOldText().length(),
                        action.getNewText()
                );

            default:
                return false;
        }
    }

    public String getText() {
        return document.getText();
    }

    public int getTextLength() {
        return document.length();
    }

    public boolean isUndoEmpty() {
        return undoStack.isEmpty();
    }

    public boolean isRedoEmpty() {
        return redoStack.isEmpty();
    }

    public int getUndoSize() {
        return undoStack.size();
    }

    public int getRedoSize() {
        return redoStack.size();
    }

    public OperationCounter getCounter() {
        return counter;
    }

    public void resetCounter() {
        counter.reset();
    }

    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
    }

    public void showStacks() {

        System.out.println("\n===== COMMAND UNDO STACK =====");

        if (undoStack.isEmpty()) {
            System.out.println("(empty)");
        } else {

            for (Action action : undoStack) {
                System.out.println(action);
            }
        }

        System.out.println("\n===== COMMAND REDO STACK =====");

        if (redoStack.isEmpty()) {
            System.out.println("(empty)");
        } else {

            for (Action action : redoStack) {
                System.out.println(action);
            }
        }
    }
}