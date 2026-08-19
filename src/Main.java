import java.util.Scanner;

import algorithms.CommandEditor;
import algorithms.SnapshotEditor;

public class Main {

    private static Scanner scanner =
            new Scanner(System.in);

    private static SnapshotEditor snapshotEditor =
            new SnapshotEditor();

    private static CommandEditor commandEditor =
            new CommandEditor();

    private static int selectedAlgorithm = 1;

    public static void main(String[] args) {

        boolean running = true;

        System.out.println("====================================");
        System.out.println("       TEXT EDITOR - GROUP 03");
        System.out.println("====================================");

        while (running) {

            showMainMenu();

            int choice = readInt("เลือกเมนู: ");

            switch (choice) {

                case 1:
                    selectAlgorithm();
                    break;

                case 2:
                    insert();
                    break;

                case 3:
                    delete();
                    break;

                case 4:
                    replace();
                    break;

                case 5:
                    undo();
                    break;

                case 6:
                    redo();
                    break;

                case 7:
                    showDocument();
                    break;

                case 8:
                    showStacks();
                    break;

                case 9:
                    showOperationCounter();
                    break;

                case 0:
                    running = false;
                    System.out.println("จบการทำงาน");
                    break;

                default:
                    System.out.println("เมนูไม่ถูกต้อง");
            }
        }

        scanner.close();
    }

    private static void showMainMenu() {

        System.out.println();
        System.out.println("========== TEXT EDITOR ==========");
        System.out.println("Algorithm: " +
                (selectedAlgorithm == 1
                        ? "Snapshot Method"
                        : "Command / Delta Method"));

        System.out.println("Document: " +
                getCurrentText());

        System.out.println("---------------------------------");
        System.out.println("1. เลือก Algorithm");
        System.out.println("2. INSERT");
        System.out.println("3. DELETE");
        System.out.println("4. REPLACE");
        System.out.println("5. UNDO");
        System.out.println("6. REDO");
        System.out.println("7. แสดง Document");
        System.out.println("8. แสดง Undo / Redo Stack");
        System.out.println("9. แสดง Operation Counter");
        System.out.println("0. Exit");
        System.out.println("---------------------------------");
    }

    private static void selectAlgorithm() {

        System.out.println();
        System.out.println("========== SELECT ALGORITHM ==========");
        System.out.println("1. Snapshot Method");
        System.out.println("2. Command / Delta Method");

        int choice =
                readInt("เลือก Algorithm: ");

        if (choice == 1 || choice == 2) {

            selectedAlgorithm = choice;

            System.out.println(
                    "เปลี่ยน Algorithm สำเร็จ"
            );

        } else {

            System.out.println(
                    "เลือกได้เฉพาะ 1 หรือ 2"
            );
        }
    }

    private static void insert() {

        int position =
                readInt("Position: ");

        System.out.print("ข้อความที่ต้องการเพิ่ม: ");
        String text = scanner.nextLine();

        boolean success;

        if (selectedAlgorithm == 1) {

            success =
                    snapshotEditor.insert(
                            position,
                            text
                    );

        } else {

            success =
                    commandEditor.insert(
                            position,
                            text
                    );
        }

        if (success) {

            System.out.println(
                    "INSERT สำเร็จ"
            );

        } else {

            System.out.println(
                    "INSERT ไม่สำเร็จ: Position ไม่ถูกต้อง"
            );
        }
    }

    private static void delete() {

        int position =
                readInt("Position: ");

        int length =
                readInt("จำนวนตัวอักษรที่ต้องการลบ: ");

        boolean success;

        if (selectedAlgorithm == 1) {

            success =
                    snapshotEditor.delete(
                            position,
                            length
                    );

        } else {

            success =
                    commandEditor.delete(
                            position,
                            length
                    );
        }

        if (success) {

            System.out.println(
                    "DELETE สำเร็จ"
            );

        } else {

            System.out.println(
                    "DELETE ไม่สำเร็จ: Position หรือ Length ไม่ถูกต้อง"
            );
        }
    }

    private static void replace() {

        int position =
                readInt("Position: ");

        int length =
                readInt("จำนวนตัวอักษรเดิม: ");

        System.out.print(
                "ข้อความใหม่: "
        );

        String newText =
                scanner.nextLine();

        boolean success;

        if (selectedAlgorithm == 1) {

            success =
                    snapshotEditor.replace(
                            position,
                            length,
                            newText
                    );

        } else {

            success =
                    commandEditor.replace(
                            position,
                            length,
                            newText
                    );
        }

        if (success) {

            System.out.println(
                    "REPLACE สำเร็จ"
            );

        } else {

            System.out.println(
                    "REPLACE ไม่สำเร็จ"
            );
        }
    }

    private static void undo() {

        boolean success;

        if (selectedAlgorithm == 1) {

            success =
                    snapshotEditor.undo();

        } else {

            success =
                    commandEditor.undo();
        }

        if (success) {

            System.out.println(
                    "UNDO สำเร็จ"
            );

        } else {

            System.out.println(
                    "ไม่สามารถ UNDO ได้ เพราะ Undo Stack ว่าง"
            );
        }
    }

    private static void redo() {

        boolean success;

        if (selectedAlgorithm == 1) {

            success =
                    snapshotEditor.redo();

        } else {

            success =
                    commandEditor.redo();
        }

        if (success) {

            System.out.println(
                    "REDO สำเร็จ"
            );

        } else {

            System.out.println(
                    "ไม่สามารถ REDO ได้ เพราะ Redo Stack ว่าง"
            );
        }
    }

    private static void showDocument() {

        System.out.println();
        System.out.println("========== DOCUMENT ==========");
        System.out.println(getCurrentText());
        System.out.println("==============================");
    }

    private static void showStacks() {

        if (selectedAlgorithm == 1) {

            snapshotEditor.showStacks();

        } else {

            commandEditor.showStacks();
        }
    }

    private static void showOperationCounter() {

        System.out.println();
        System.out.println(
                "========== OPERATION COUNTER =========="
        );

        if (selectedAlgorithm == 1) {

            System.out.println(
                    snapshotEditor
                            .getCounter()
                            .summary()
            );

        } else {

            System.out.println(
                    commandEditor
                            .getCounter()
                            .summary()
            );
        }
    }

    private static String getCurrentText() {

        if (selectedAlgorithm == 1) {

            return snapshotEditor.getText();

        } else {

            return commandEditor.getText();
        }
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        input.trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "กรุณาป้อนตัวเลขจำนวนเต็มเท่านั้น"
                );
            }
        }
    }
}