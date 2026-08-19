package models;

import java.time.LocalDateTime;

public class Action {

    private int actionId;
    private ActionType actionType;
    private int position;
    private String oldText;
    private String newText;
    private LocalDateTime timestamp;

    public Action(
            int actionId,
            ActionType actionType,
            int position,
            String oldText,
            String newText
    ) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.position = position;
        this.oldText = oldText == null ? "" : oldText;
        this.newText = newText == null ? "" : newText;
        this.timestamp = LocalDateTime.now();
    }

    public int getActionId() {
        return actionId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public int getPosition() {
        return position;
    }

    public String getOldText() {
        return oldText;
    }

    public String getNewText() {
        return newText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Action{" +
                "ID=" + actionId +
                ", Type=" + actionType +
                ", Position=" + position +
                ", OldText='" + oldText + '\'' +
                ", NewText='" + newText + '\'' +
                ", Timestamp=" + timestamp +
                '}';
    }
}