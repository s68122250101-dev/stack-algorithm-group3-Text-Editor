import java.io.File;
import java.io.PrintWriter;

public class PerformanceTest {

    static final int[] SIZES = {100, 1000, 10000, 50000};
    static final int RUNS = 5;

    public static void main(String[] args) throws Exception {

        new File("results").mkdirs();

        PrintWriter out = new PrintWriter(
                "results/experiment-results.csv");

        out.println(
                "N,Algorithm,AverageUndo(ns),AverageRedo(ns),Push,Pop,Comparison");

        for (int n : SIZES) {

            testSnapshot(n, out);
            testCommand(n, out);
        }

        out.close();

        System.out.println("ทดลองเสร็จแล้ว");
        System.out.println(
                "ไฟล์อยู่ที่ results/experiment-results.csv");
    }

    static void testSnapshot(int n, PrintWriter out) {

        long undoTotal = 0;
        long redoTotal = 0;
        long push = 0;
        long pop = 0;
        long compare = 0;

        for (int r = 0; r < RUNS; r++) {

            TextEditor editor =
                    new TextEditor(makeText(n));

            editor.insertSnapshot(n, "A");

            long start = System.nanoTime();
            editor.undoSnapshot();
            undoTotal += System.nanoTime() - start;

            start = System.nanoTime();
            editor.redoSnapshot();
            redoTotal += System.nanoTime() - start;

            push += editor.getPushCount();
            pop += editor.getPopCount();
            compare += editor.getComparisonCount();
        }

        out.println(
                n + ",Snapshot,"
                        + undoTotal / RUNS + ","
                        + redoTotal / RUNS + ","
                        + push / RUNS + ","
                        + pop / RUNS + ","
                        + compare / RUNS);
    }

    static void testCommand(int n, PrintWriter out) {

        long undoTotal = 0;
        long redoTotal = 0;
        long push = 0;
        long pop = 0;
        long compare = 0;

        for (int r = 0; r < RUNS; r++) {

            TextEditor editor =
                    new TextEditor(makeText(n));

            editor.insertCommand(n, "A");

            long start = System.nanoTime();
            editor.undoCommand();
            undoTotal += System.nanoTime() - start;

            start = System.nanoTime();
            editor.redoCommand();
            redoTotal += System.nanoTime() - start;

            push += editor.getPushCount();
            pop += editor.getPopCount();
            compare += editor.getComparisonCount();
        }

        out.println(
                n + ",Command,"
                        + undoTotal / RUNS + ","
                        + redoTotal / RUNS + ","
                        + push / RUNS + ","
                        + pop / RUNS + ","
                        + compare / RUNS);
    }

    static String makeText(int n) {

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < n; i++) {
            text.append("A");
        }

        return text.toString();
    }
}