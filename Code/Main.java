import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== TEXT EDITOR =====");

        System.out.print("ข้อความเริ่มต้น: ");
        TextEditor editor =
                new TextEditor(sc.nextLine());

        System.out.print("เลือก Algorithm (1=Snapshot, 2=Command): ");
        int algorithm = sc.nextInt();

        while (true) {

            System.out.println("\n----------------------");
            System.out.println("ข้อความปัจจุบัน: "
                    + editor.getDocument());
            System.out.println("----------------------");
            System.out.println("1. INSERT");
            System.out.println("2. DELETE");
            System.out.println("3. REPLACE");
            System.out.println("4. UNDO");
            System.out.println("5. REDO");
            System.out.println("6. แสดง Stack");
            System.out.println("0. EXIT");
            System.out.print("เลือก: ");

            int choice = sc.nextInt();

            if (choice == 0)
                break;

            boolean result = false;

            try {

                if (choice == 1) {

                    System.out.print("Position: ");
                    int p = sc.nextInt();

                    sc.nextLine();

                    System.out.print("ข้อความ: ");
                    String text = sc.nextLine();

                    if (algorithm == 1)
                        result = editor.insertSnapshot(p, text);
                    else
                        result = editor.insertCommand(p, text);

                } else if (choice == 2) {

                    System.out.print("Position: ");
                    int p = sc.nextInt();

                    System.out.print("Length: ");
                    int length = sc.nextInt();

                    if (algorithm == 1)
                        result = editor.deleteSnapshot(p, length);
                    else
                        result = editor.deleteCommand(p, length);

                } else if (choice == 3) {

                    System.out.print("Position: ");
                    int p = sc.nextInt();

                    System.out.print("Length: ");
                    int length = sc.nextInt();

                    sc.nextLine();

                    System.out.print("ข้อความใหม่: ");
                    String text = sc.nextLine();

                    if (algorithm == 1)
                        result =
                                editor.replaceSnapshot(
                                        p, length, text);
                    else
                        result =
                                editor.replaceCommand(
                                        p, length, text);

                } else if (choice == 4) {

                    if (algorithm == 1)
                        result = editor.undoSnapshot();
                    else
                        result = editor.undoCommand();

                } else if (choice == 5) {

                    if (algorithm == 1)
                        result = editor.redoSnapshot();
                    else
                        result = editor.redoCommand();

                } else if (choice == 6) {

                    if (algorithm == 1)
                        editor.showSnapshotStack();
                    else
                        editor.showCommandStack();

                    continue;

                } else {

                    System.out.println("เมนูไม่ถูกต้อง");
                    continue;
                }

                System.out.println(
                        result ? "ทำรายการสำเร็จ"
                               : "ทำรายการไม่สำเร็จ");

            } catch (Exception e) {

                System.out.println("Input ไม่ถูกต้อง");
                sc.nextLine();
            }
        }

        sc.close();

        System.out.println("จบโปรแกรม");
    }
}