package experiments;

import algorithms.CommandEditor;
import algorithms.SnapshotEditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class PerformanceTest {

    // จำนวนรอบการทดลองตามโจทย์
    private static final int RUNS = 5;

    // ขนาดข้อความที่ต้องทดลอง
    private static final int[] TEXT_SIZES = {
            100,
            1000,
            10000,
            100000
    };

    // จำนวน Action ที่ต้องทดลอง
    private static final int[] ACTION_COUNTS = {
            100,
            1000,
            10000
    };

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       TEXT EDITOR PERFORMANCE TEST");
        System.out.println("==============================================");

        System.out.println("จำนวนรอบทดลอง: " + RUNS);
        System.out.println();

        String outputFile =
                "results/experiment-results.csv";

        createResultsFolder();

        try {
            runExperiment(outputFile);

            System.out.println();
            System.out.println("==============================================");
            System.out.println("การทดลองเสร็จสิ้น");
            System.out.println("บันทึกผลไว้ที่:");
            System.out.println(outputFile);
            System.out.println("==============================================");

        } catch (IOException e) {

            System.out.println(
                    "ไม่สามารถบันทึกผลการทดลองได้: "
                            + e.getMessage()
            );
        }
    }

    /**
     * สร้างโฟลเดอร์ results
     * ถ้ายังไม่มี
     */
    private static void createResultsFolder() {

        File folder = new File("results");

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * เริ่มการทดลองทั้งหมด
     */
    private static void runExperiment(
            String outputFile
    ) throws IOException {

        try (PrintWriter writer =
                     new PrintWriter(
                             new FileWriter(outputFile)
                     )) {

            // Header ของ CSV
            writer.println(
                    "TextSize,ActionCount,Algorithm," +
                    "AverageUndoTimeNs,AverageRedoTimeNs," +
                    "AveragePush,AveragePop," +
                    "AverageComparisons,AverageLoops," +
                    "AverageMoves,AverageStoredData"
            );

            // ทดลองทุกขนาดข้อความ
            for (int textSize : TEXT_SIZES) {

                // ทดลองทุกจำนวน Action
                for (int actionCount : ACTION_COUNTS) {

                    System.out.println();
                    System.out.println(
                            "----------------------------------------------"
                    );

                    System.out.println(
                            "Text Size = "
                                    + textSize
                                    + " characters"
                    );

                    System.out.println(
                            "Actions = "
                                    + actionCount
                    );

                    System.out.println(
                            "----------------------------------------------"
                    );

                    // Algorithm A
                    ExperimentResult snapshotResult =
                            testSnapshot(
                                    textSize,
                                    actionCount
                            );

                    printResult(
                            "Snapshot",
                            snapshotResult
                    );

                    writeResult(
                            writer,
                            textSize,
                            actionCount,
                            "Snapshot",
                            snapshotResult
                    );

                    // Algorithm B
                    ExperimentResult commandResult =
                            testCommand(
                                    textSize,
                                    actionCount
                            );

                    printResult(
                            "Command",
                            commandResult
                    );

                    writeResult(
                            writer,
                            textSize,
                            actionCount,
                            "Command",
                            commandResult
                    );
                }
            }
        }
    }

    /**
     * ทดสอบ Algorithm A
     * Snapshot Method
     */
    private static ExperimentResult testSnapshot(
            int textSize,
            int actionCount
    ) {

        long totalUndoTime = 0;
        long totalRedoTime = 0;

        long totalPush = 0;
        long totalPop = 0;
        long totalComparisons = 0;
        long totalLoops = 0;
        long totalMoves = 0;
        long totalStoredData = 0;

        for (int run = 0; run < RUNS; run++) {

            String initialText =
                    createText(textSize);

            SnapshotEditor editor =
                    new SnapshotEditor(initialText);

            // สร้าง Action ก่อนการวัด Undo/Redo
            performSnapshotActions(
                    editor,
                    actionCount
            );

            editor.resetCounter();

            // -------------------------
            // วัด Undo
            // -------------------------

            long startUndo =
                    System.nanoTime();

            editor.undo();

            long endUndo =
                    System.nanoTime();

            long undoTime =
                    endUndo - startUndo;

            totalUndoTime += undoTime;

            // -------------------------
            // วัด Redo
            // -------------------------

            long startRedo =
                    System.nanoTime();

            editor.redo();

            long endRedo =
                    System.nanoTime();

            long redoTime =
                    endRedo - startRedo;

            totalRedoTime += redoTime;

            // -------------------------
            // เก็บ Operation
            // -------------------------

            totalPush +=
                    editor.getCounter()
                            .getPushCount();

            totalPop +=
                    editor.getCounter()
                            .getPopCount();

            totalComparisons +=
                    editor.getCounter()
                            .getComparisonCount();

            totalLoops +=
                    editor.getCounter()
                            .getLoopCount();

            totalMoves +=
                    editor.getCounter()
                            .getMoveCount();

            // Snapshot เก็บข้อความทั้งฉบับ
            totalStoredData +=
                    calculateSnapshotStorage(
                            editor
                    );
        }

        return new ExperimentResult(

                totalUndoTime / (double) RUNS,

                totalRedoTime / (double) RUNS,

                totalPush / (double) RUNS,

                totalPop / (double) RUNS,

                totalComparisons / (double) RUNS,

                totalLoops / (double) RUNS,

                totalMoves / (double) RUNS,

                totalStoredData / (double) RUNS
        );
    }

    /**
     * สร้าง Action สำหรับ Snapshot
     */
    private static void performSnapshotActions(
            SnapshotEditor editor,
            int actionCount
    ) {

        for (int i = 0; i < actionCount; i++) {

            int position =
                    editor.getTextLength();

            editor.insert(
                    position,
                    "A"
            );
        }
    }

    /**
     * ทดสอบ Algorithm B
     * Command / Delta Method
     */
    private static ExperimentResult testCommand(
            int textSize,
            int actionCount
    ) {

        long totalUndoTime = 0;
        long totalRedoTime = 0;

        long totalPush = 0;
        long totalPop = 0;
        long totalComparisons = 0;
        long totalLoops = 0;
        long totalMoves = 0;
        long totalStoredData = 0;

        for (int run = 0; run < RUNS; run++) {

            String initialText =
                    createText(textSize);

            CommandEditor editor =
                    new CommandEditor(initialText);

            // สร้าง Action ก่อนการวัด
            performCommandActions(
                    editor,
                    actionCount
            );

            editor.resetCounter();

            // -------------------------
            // วัด Undo
            // -------------------------

            long startUndo =
                    System.nanoTime();

            editor.undo();

            long endUndo =
                    System.nanoTime();

            long undoTime =
                    endUndo - startUndo;

            totalUndoTime += undoTime;

            // -------------------------
            // วัด Redo
            // -------------------------

            long startRedo =
                    System.nanoTime();

            editor.redo();

            long endRedo =
                    System.nanoTime();

            long redoTime =
                    endRedo - startRedo;

            totalRedoTime += redoTime;

            // -------------------------
            // เก็บ Operation
            // -------------------------

            totalPush +=
                    editor.getCounter()
                            .getPushCount();

            totalPop +=
                    editor.getCounter()
                            .getPopCount();

            totalComparisons +=
                    editor.getCounter()
                            .getComparisonCount();

            totalLoops +=
                    editor.getCounter()
                            .getLoopCount();

            totalMoves +=
                    editor.getCounter()
                            .getMoveCount();

            // Command เก็บเฉพาะข้อมูลของ Action
            totalStoredData +=
                    calculateCommandStorage(
                            editor,
                            actionCount
                    );
        }

        return new ExperimentResult(

                totalUndoTime / (double) RUNS,

                totalRedoTime / (double) RUNS,

                totalPush / (double) RUNS,

                totalPop / (double) RUNS,

                totalComparisons / (double) RUNS,

                totalLoops / (double) RUNS,

                totalMoves / (double) RUNS,

                totalStoredData / (double) RUNS
        );
    }

    /**
     * สร้าง Action สำหรับ Command
     */
    private static void performCommandActions(
            CommandEditor editor,
            int actionCount
    ) {

        for (int i = 0; i < actionCount; i++) {

            int position =
                    editor.getTextLength();

            editor.insert(
                    position,
                    "A"
            );
        }
    }

    /**
     * สร้างข้อความตามขนาดที่กำหนด
     */
    private static String createText(
            int size
    ) {

        StringBuilder builder =
                new StringBuilder(size);

        for (int i = 0; i < size; i++) {

            builder.append('A');
        }

        return builder.toString();
    }

    /**
     * ประมาณพื้นที่ของ Snapshot
     *
     * Snapshot เก็บข้อความทั้งฉบับ
     *
     * จำนวน Snapshot =
     * จำนวน Action
     *
     * ดังนั้นโดยประมาณคือ
     *
     * O(m * n)
     */
    private static long calculateSnapshotStorage(
            SnapshotEditor editor
    ) {

        int currentLength =
                editor.getTextLength();

        int undoSize =
                editor.getUndoSize();

        int redoSize =
                editor.getRedoSize();

        long totalSnapshots =
                undoSize + redoSize;

        return totalSnapshots *
                currentLength;
    }

    /**
     * ประมาณพื้นที่ของ Command
     *
     * Command เก็บเฉพาะข้อมูล
     * ของ Action
     */
    private static long calculateCommandStorage(
            CommandEditor editor,
            int actionCount
    ) {

        /*
         * ในการทดลองนี้แต่ละ Action
         * เพิ่มข้อความ "A" เพียง 1 ตัว
         *
         * ดังนั้นพื้นที่ข้อมูลของแต่ละ
         * Command จะมีขนาดเล็กมาก
         *
         * ประมาณเป็นจำนวน Action
         */

        long undoSize =
                editor.getUndoSize();

        long redoSize =
                editor.getRedoSize();

        return undoSize + redoSize;
    }

    /**
     * แสดงผลการทดลอง
     */
    private static void printResult(
            String algorithm,
            ExperimentResult result
    ) {

        System.out.println();

        System.out.println(
                "Algorithm: "
                        + algorithm
        );

        System.out.printf(
                Locale.US,
                "Average Undo Time : %.2f ns%n",
                result.averageUndoTime
        );

        System.out.printf(
                Locale.US,
                "Average Redo Time : %.2f ns%n",
                result.averageRedoTime
        );

        System.out.printf(
                Locale.US,
                "Average Push      : %.2f%n",
                result.averagePush
        );

        System.out.printf(
                Locale.US,
                "Average Pop       : %.2f%n",
                result.averagePop
        );

        System.out.printf(
                Locale.US,
                "Average Comparison: %.2f%n",
                result.averageComparisons
        );

        System.out.printf(
                Locale.US,
                "Average Loop      : %.2f%n",
                result.averageLoops
        );

        System.out.printf(
                Locale.US,
                "Average Move      : %.2f%n",
                result.averageMoves
        );

        System.out.printf(
                Locale.US,
                "Average Stored Data: %.2f%n",
                result.averageStoredData
        );
    }

    /**
     * เขียนผลลง CSV
     */
    private static void writeResult(
            PrintWriter writer,
            int textSize,
            int actionCount,
            String algorithm,
            ExperimentResult result
    ) {

        writer.printf(
                Locale.US,
                "%d,%d,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",

                textSize,

                actionCount,

                algorithm,

                result.averageUndoTime,

                result.averageRedoTime,

                result.averagePush,

                result.averagePop,

                result.averageComparisons,

                result.averageLoops,

                result.averageMoves,

                result.averageStoredData
        );
    }

    /**
     * Class สำหรับเก็บผลการทดลอง
     */
    private static class ExperimentResult {

        private double averageUndoTime;
        private double averageRedoTime;

        private double averagePush;
        private double averagePop;

        private double averageComparisons;
        private double averageLoops;

        private double averageMoves;

        private double averageStoredData;

        public ExperimentResult(
                double averageUndoTime,
                double averageRedoTime,
                double averagePush,
                double averagePop,
                double averageComparisons,
                double averageLoops,
                double averageMoves,
                double averageStoredData
        ) {

            this.averageUndoTime =
                    averageUndoTime;

            this.averageRedoTime =
                    averageRedoTime;

            this.averagePush =
                    averagePush;

            this.averagePop =
                    averagePop;

            this.averageComparisons =
                    averageComparisons;

            this.averageLoops =
                    averageLoops;

            this.averageMoves =
                    averageMoves;

            this.averageStoredData =
                    averageStoredData;
        }
    }
}