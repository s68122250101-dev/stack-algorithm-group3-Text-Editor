import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TextEditor editor;

        try {
            editor = new TextEditor("HelloWorld");
        } catch (TextEditor.EditorException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        while (true) {

            System.out.println("\n==============================");
            System.out.println("         TEXT EDITOR");
            System.out.println("==============================");
            System.out.println("Document : " + editor.getDocument());

            System.out.println("\nเลือก Algorithm");
            System.out.println("1. Algorithm A : Snapshot");
            System.out.println("2. Algorithm B : Command");
            System.out.println("0. Exit");
            System.out.print("เลือก Algorithm: ");

            try {

                int algorithm = sc.nextInt();
                sc.nextLine();

                if (algorithm == 0) {
                    System.out.println("จบโปรแกรม");
                    break;
                }

                if (algorithm == 1) {
                    snapshotMenu(sc, editor);

                } else if (algorithm == 2) {
                    commandMenu(sc, editor);

                } else {
                    throw new TextEditor.EditorException(
                            "กรุณาเลือก Algorithm 0-2"
                    );
                }

            } catch (InputMismatchException e) {

                System.out.println(
                        "Error: กรุณากรอกตัวเลขเท่านั้น"
                );

                sc.nextLine();

            } catch (TextEditor.EditorException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }

        sc.close();
    }

    // =====================================================
    // Algorithm A : Snapshot Menu
    // =====================================================
    public static void snapshotMenu(
            Scanner sc,
            TextEditor editor)
            throws TextEditor.EditorException {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("    Algorithm A : Snapshot");
            System.out.println("==============================");
            System.out.println("Document : " + editor.getDocument());

            System.out.println("\n1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Replace");
            System.out.println("4. Undo");
            System.out.println("5. Redo");
            System.out.println("6. Show Stack");
            System.out.println("0. Back");
            System.out.print("เลือก: ");

            try {

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 0)
                    return;

                switch (choice) {

                    case 1:

                        System.out.print("Position: ");
                        int insertPos = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Text: ");
                        String insertText = sc.nextLine();

                        editor.insertSnapshot(
                                insertPos,
                                insertText
                        );

                        System.out.println("Insert สำเร็จ");
                        break;

                    case 2:

                        System.out.print("Position: ");
                        int deletePos = sc.nextInt();

                        System.out.print("Length: ");
                        int deleteLength = sc.nextInt();
                        sc.nextLine();

                        editor.deleteSnapshot(
                                deletePos,
                                deleteLength
                        );

                        System.out.println("Delete สำเร็จ");
                        break;

                    case 3:

                        System.out.print("Position: ");
                        int replacePos = sc.nextInt();

                        System.out.print("Length: ");
                        int replaceLength = sc.nextInt();
                        sc.nextLine();

                        System.out.print("New Text: ");
                        String replaceText = sc.nextLine();

                        editor.replaceSnapshot(
                                replacePos,
                                replaceLength,
                                replaceText
                        );

                        System.out.println("Replace สำเร็จ");
                        break;

                    case 4:

                        editor.undoSnapshot();

                        System.out.println("Undo สำเร็จ");
                        break;

                    case 5:

                        editor.redoSnapshot();

                        System.out.println("Redo สำเร็จ");
                        break;

                    case 6:

                        editor.showSnapshotStack();
                        break;

                    default:

                        throw new TextEditor.EditorException(
                                "ไม่มีเมนูนี้ กรุณาเลือก 0-6"
                        );
                }

            } catch (InputMismatchException e) {

                System.out.println(
                        "Error: กรุณากรอกตัวเลขเท่านั้น"
                );

                sc.nextLine();

            } catch (TextEditor.EditorException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // Algorithm B : Command Menu
    // =====================================================
    public static void commandMenu(
            Scanner sc,
            TextEditor editor)
            throws TextEditor.EditorException {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("     Algorithm B : Command");
            System.out.println("==============================");
            System.out.println("Document : " + editor.getDocument());

            System.out.println("\n1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Replace");
            System.out.println("4. Undo");
            System.out.println("5. Redo");
            System.out.println("6. Show Stack");
            System.out.println("0. Back");
            System.out.print("เลือก: ");

            try {

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 0)
                    return;

                switch (choice) {

                    case 1:

                        System.out.print("Position: ");
                        int insertPos = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Text: ");
                        String insertText = sc.nextLine();

                        editor.insertCommand(
                                insertPos,
                                insertText
                        );

                        System.out.println("Insert สำเร็จ");
                        break;

                    case 2:

                        System.out.print("Position: ");
                        int deletePos = sc.nextInt();

                        System.out.print("Length: ");
                        int deleteLength = sc.nextInt();
                        sc.nextLine();

                        editor.deleteCommand(
                                deletePos,
                                deleteLength
                        );

                        System.out.println("Delete สำเร็จ");
                        break;

                    case 3:

                        System.out.print("Position: ");
                        int replacePos = sc.nextInt();

                        System.out.print("Length: ");
                        int replaceLength = sc.nextInt();
                        sc.nextLine();

                        System.out.print("New Text: ");
                        String replaceText = sc.nextLine();

                        editor.replaceCommand(
                                replacePos,
                                replaceLength,
                                replaceText
                        );

                        System.out.println("Replace สำเร็จ");
                        break;

                    case 4:

                        editor.undoCommand();

                        System.out.println("Undo สำเร็จ");
                        break;

                    case 5:

                        editor.redoCommand();

                        System.out.println("Redo สำเร็จ");
                        break;

                    case 6:

                        editor.showCommandStack();
                        break;

                    default:

                        throw new TextEditor.EditorException(
                                "ไม่มีเมนูนี้ กรุณาเลือก 0-6"
                        );
                }

            } catch (InputMismatchException e) {

                System.out.println(
                        "Error: กรุณากรอกตัวเลขเท่านั้น"
                );

                sc.nextLine();

            } catch (TextEditor.EditorException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }
}