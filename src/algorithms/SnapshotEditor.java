package algorithms;

import java.util.ArrayDeque;
import java.util.Deque;

import models.TextDocument;
import utils.OperationCounter;

public class SnapshotEditor {

    private TextDocument document;

    private Deque<String> undoStack;
    private Deque<String> redoStack;

    private OperationCounter counter;

    public SnapshotEditor() {
        this("");
    }

    public SnapshotEditor(String initialText) {

        document = new TextDocument(initialText);

        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();

        counter = new OperationCounter();
    }

    public boolean insert(int position, String newText) {

        if (!document.isValidInsertPosition(position)) {
            counter.incrementComparison();
            return false;
        }

        if (newText == null) {
            return false;
        }

        // เก็บ Snapshot ก่อนแก้ไข
        undoStack.push(document.getText());
        counter.incrementPush();

        // Action ใหม่ต้องล้าง Redo
        redoStack.clear();

        document.insert(position, newText);

        counter.incrementMove();

        return true;
    }

    public boolean delete(int position, int length) {

        if (!document.isValidRange(position, length)) {
            counter.incrementComparison();
            return false;
        }

        // เก็บ Snapshot ก่อนแก้ไข
        undoStack.push(document.getText());
        counter.incrementPush();

        // Action ใหม่ต้องล้าง Redo
        redoStack.clear();

        document.delete(position, length);

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

        // เก็บ Snapshot ก่อนแก้ไข
        undoStack.push(document.getText());
        counter.incrementPush();

        // Action ใหม่ต้องล้าง Redo
        redoStack.clear();

        document.replace(position, length, newText);

        counter.incrementMove();

        return true;
    }

    public boolean undo() {

        if (undoStack.isEmpty()) {
            counter.incrementComparison();
            return false;
        }

        // เก็บสถานะปัจจุบันไว้ใน Redo
        redoStack.push(document.getText());
        counter.incrementPush();

        // เอา Snapshot ก่อนหน้าออกมา
        String previousState = undoStack.pop();
        counter.incrementPop();

        // คืนข้อความเดิม
        document.setText(previousState);

        counter.incrementMove();

        return true;
    }

    public boolean redo() {

        if (redoStack.isEmpty()) {
            counter.incrementComparison();
            return false;
        }

        // เก็บสถานะปัจจุบันไว้ใน Undo
        undoStack.push(document.getText());
        counter.incrementPush();

        // เอาสถานะที่ต้อง Redo ออกมา
        String nextState = redoStack.pop();
        counter.incrementPop();

        // คืนสถานะหลัง Action
        document.setText(nextState);

        counter.incrementMove();

        return true;
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

        System.out.println("\n===== SNAPSHOT UNDO STACK =====");

        if (undoStack.isEmpty()) {
            System.out.println("(empty)");
        } else {

            for (String snapshot : undoStack) {
                System.out.println("[ " + snapshot + " ]");
            }
        }

        System.out.println("\n===== SNAPSHOT REDO STACK =====");

        if (redoStack.isEmpty()) {
            System.out.println("(empty)");
        } else {

            for (String snapshot : redoStack) {
                System.out.println("[ " + snapshot + " ]");
            }
        }
    }
}
