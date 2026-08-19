import algorithms.CommandEditor;
import algorithms.SnapshotEditor;

public class TestCases {

    private static int passed = 0;
    private static int total = 0;

    public static void main(String[] args) {

        run("TC01 Insert then Undo",
                TestCases::tc01);

        run("TC02 Delete then Undo",
                TestCases::tc02);

        run("TC03 Replace then Undo",
                TestCases::tc03);

        run("TC04 Undo then Redo",
                TestCases::tc04);

        run("TC05 Multiple Undo",
                TestCases::tc05);

        run("TC06 Undo then New Action clears Redo",
                TestCases::tc06);

        run("TC07 Invalid Position",
                TestCases::tc07);

        run("TC08 Empty Text",
                TestCases::tc08);

        run("TC09 Empty Undo/Redo Stack",
                TestCases::tc09);

        run("TC10 Boundary Position",
                TestCases::tc10);

        run("TC11 Invalid Range",
                TestCases::tc11);

        run("TC12 Both Algorithms Same Result",
                TestCases::tc12);

        System.out.println();
        System.out.println("==============================");
        System.out.println(
                "Passed: " + passed + "/" + total
        );
        System.out.println("==============================");
    }

    private static boolean tc01() {

        SnapshotEditor editor =
                new SnapshotEditor("HelloWorld");

        editor.insert(5, "AI");

        if (!editor.getText().equals("HelloAIWorld")) {
            return false;
        }

        editor.undo();

        return editor.getText().equals("HelloWorld");
    }

    private static boolean tc02() {

        CommandEditor editor =
                new CommandEditor("HelloWorld");

        editor.delete(5, 5);

        if (!editor.getText().equals("Hello")) {
            return false;
        }

        editor.undo();

        return editor.getText().equals("HelloWorld");
    }

    private static boolean tc03() {

        CommandEditor editor =
                new CommandEditor("HelloWorld");

        editor.replace(5, 5, "Java");

        if (!editor.getText().equals("HelloJava")) {
            return false;
        }

        editor.undo();

        return editor.getText().equals("HelloWorld");
    }

    private static boolean tc04() {

        SnapshotEditor editor =
                new SnapshotEditor("HelloWorld");

        editor.insert(5, "AI");

        editor.undo();

        if (!editor.getText().equals("HelloWorld")) {
            return false;
        }

        editor.redo();

        return editor.getText().equals("HelloAIWorld");
    }

    private static boolean tc05() {

        CommandEditor editor =
                new CommandEditor("A");

        editor.insert(1, "B");
        editor.insert(2, "C");
        editor.insert(3, "D");

        if (!editor.getText().equals("ABCD")) {
            return false;
        }

        editor.undo();

        if (!editor.getText().equals("ABC")) {
            return false;
        }

        editor.undo();

        if (!editor.getText().equals("AB")) {
            return false;
        }

        editor.undo();

        return editor.getText().equals("A");
    }

    private static boolean tc06() {

        CommandEditor editor =
                new CommandEditor("Hello");

        editor.insert(5, "World");

        editor.undo();

        if (editor.isRedoEmpty()) {
            return false;
        }

        editor.insert(5, "!");

        return editor.isRedoEmpty()
                && editor.getText().equals("Hello!");
    }

    private static boolean tc07() {

        SnapshotEditor editor =
                new SnapshotEditor("Hello");

        boolean result =
                editor.insert(100, "A");

        return !result
                && editor.getText().equals("Hello");
    }

    private static boolean tc08() {

        CommandEditor editor =
                new CommandEditor("");

        if (!editor.getText().equals("")) {
            return false;
        }

        editor.insert(0, "Hello");

        return editor.getText().equals("Hello");
    }

    private static boolean tc09() {

        SnapshotEditor editor =
                new SnapshotEditor("Hello");

        boolean undoResult =
                editor.undo();

        boolean redoResult =
                editor.redo();

        return !undoResult
                && !redoResult
                && editor.getText().equals("Hello");
    }

    private static boolean tc10() {

        CommandEditor editor =
                new CommandEditor("Hello");

        boolean first =
                editor.insert(0, "A");

        boolean second =
                editor.insert(
                        editor.getTextLength(),
                        "B"
                );

        return first
                && second
                && editor.getText().equals("AHelloB");
    }

    private static boolean tc11() {

        CommandEditor editor =
                new CommandEditor("Hello");

        boolean result =
                editor.delete(3, 10);

        return !result
                && editor.getText().equals("Hello");
    }

    private static boolean tc12() {

        SnapshotEditor snapshot =
                new SnapshotEditor("HelloWorld");

        CommandEditor command =
                new CommandEditor("HelloWorld");

        snapshot.insert(5, "AI");
        command.insert(5, "AI");

        snapshot.replace(0, 5, "Hi");
        command.replace(0, 5, "Hi");

        snapshot.delete(2, 1);
        command.delete(2, 1);

        return snapshot.getText()
                .equals(command.getText());
    }

    private static void run(
            String name,
            TestFunction test
    ) {

        total++;

        try {

            boolean result = test.run();

            if (result) {

                passed++;

                System.out.println(
                        "[PASS] " + name
                );

            } else {

                System.out.println(
                        "[FAIL] " + name
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "[ERROR] " + name +
                            " : " +
                            e.getMessage()
            );
        }
    }

    @FunctionalInterface
    interface TestFunction {

        boolean run();
    }
}