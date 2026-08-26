public class TestCases {

    static void test(String id, boolean result) {
        System.out.println(
                id + " : "
                + (result ? "PASS" : "FAIL"));
    }

    public static void main(String[] args) {

        // TC01 Insert แล้ว Undo
        TextEditor a =
                new TextEditor("Hello");

        a.insertCommand(5, " AI");
        test("TC01 Insert + Undo",
                a.undoCommand()
                && a.getDocument().equals("Hello"));

        // TC02 Delete แล้ว Undo
        TextEditor b =
                new TextEditor("HelloWorld");

        b.deleteCommand(5, 5);
        test("TC02 Delete + Undo",
                b.undoCommand()
                && b.getDocument().equals("HelloWorld"));

        // TC03 Replace แล้ว Undo
        TextEditor c =
                new TextEditor("HelloWorld");

        c.replaceCommand(5, 5, "Java");
        test("TC03 Replace + Undo",
                c.undoCommand()
                && c.getDocument().equals("HelloWorld"));

        // TC04 Undo แล้ว Redo
        TextEditor d =
                new TextEditor("Hello");

        d.insertCommand(5, "!");
        d.undoCommand();

        test("TC04 Undo + Redo",
                d.redoCommand()
                && d.getDocument().equals("Hello!"));

        // TC05 Undo หลายครั้ง
        TextEditor e =
                new TextEditor("A");

        e.insertCommand(1, "B");
        e.insertCommand(2, "C");

        e.undoCommand();
        e.undoCommand();

        test("TC05 Multiple Undo",
                e.getDocument().equals("A"));

        // TC06 Undo แล้ว Action ใหม่
        TextEditor f =
                new TextEditor("Hello");

        f.insertCommand(5, "A");
        f.undoCommand();
        f.insertCommand(5, "B");

        test("TC06 New Action",
                !f.redoCommand());

        // TC07 Position ผิด
        TextEditor g =
                new TextEditor("Hello");

        test("TC07 Invalid Position",
                !g.insertCommand(100, "A"));

        // TC08 ข้อความว่าง
        TextEditor h =
                new TextEditor("");

        test("TC08 Empty Text",
                h.getDocument().equals(""));
    }
}