package utils;

public class OperationCounter {

    private long pushCount;
    private long popCount;
    private long comparisonCount;
    private long loopCount;
    private long moveCount;

    public OperationCounter() {
        reset();
    }

    public void incrementPush() {
        pushCount++;
    }

    public void incrementPop() {
        popCount++;
    }

    public void incrementComparison() {
        comparisonCount++;
    }

    public void incrementLoop() {
        loopCount++;
    }

    public void incrementMove() {
        moveCount++;
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

    public long getLoopCount() {
        return loopCount;
    }

    public long getMoveCount() {
        return moveCount;
    }

    public void reset() {
        pushCount = 0;
        popCount = 0;
        comparisonCount = 0;
        loopCount = 0;
        moveCount = 0;
    }

    public String summary() {
        return "Push=" + pushCount +
                ", Pop=" + popCount +
                ", Comparisons=" + comparisonCount +
                ", Loops=" + loopCount +
                ", Moves=" + moveCount;
    }
}