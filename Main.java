import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ContactManager cm = new ContactManager();
        try (Scanner sc = new Scanner(System.in)) {
            int choice;

            do {
                System.out.println("\n================= Contact Manager ===========================");
                System.out.println("1 - Add Contact");
                System.out.println("2 - View Contacts");
                System.out.println("3 - Update Contact");
                System.out.println("4 - Delete Contact");
                System.out.println("5 - Search Contact");
                System.out.println("6 - Export to CSV");
                System.out.println("7 - Import from CSV");
                System.out.println("0 - Exit");
                System.out.println("=============================================================");
                System.out.print("Choose an option: ");

                choice = sc.nextInt(); sc.nextLine(); 

                switch (choice) {
                    case 1: cm.addContact(); break;
                    case 2: cm.viewContacts(); break;
                    case 3: cm.updateContact(); break;
                    case 4: cm.deleteContact(); break;
                    case 5: cm.searchContact(); break;
                    case 6: cm.exportToCSV(); break;
                    case 7: cm.importFromCSV(); break;
                    case 0: System.out.println("Goodbye!"); break;
                    default: System.out.println("Invalid option!");
                }
            } while (choice != 0);
        }
    }
}
