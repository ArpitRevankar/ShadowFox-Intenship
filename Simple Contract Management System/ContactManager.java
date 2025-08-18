import java.util.*;
import java.util.regex.*;
import java.io.*;

public class ContactManager {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    private boolean isValidName(String name) {
       return name.length() <= 7;
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        return Pattern.matches(regex, email);
    }

    private boolean isDuplicate(String name, String phone) {
        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(name) && c.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public void addContact() {
        System.out.print("Enter name(6 char): ");
        String name = sc.nextLine();

        if(!isValidName(name)){
            System.out.println("Invalid name!");
            return;
        }

        System.out.print("Enter phone (10 digits): ");
        String phone = sc.nextLine();

        if (!isValidPhone(phone)) {
            System.out.println("Invalid phone number!");
            return;
        }

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        if (!isValidEmail(email)) {
            System.out.println("Invalid email!");
            return;
        }

        if (isDuplicate(name, phone)) {
            System.out.println("Contact with this name & phone already exists!");
            return;
        }

        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact added successfully!");
    }

    public void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("📭 No contacts found!");
            return;
        }

        contacts.sort(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER));

        System.out.println("\n===========================================================================");
        System.out.printf("| %-20s | %-12s | %-25s | %-16s |\n", "Name", "Phone", "Email", "Created Date");
        System.out.println("---------------------------------------------------------------------------");

        for (Contact c : contacts) {
            System.out.println(c);
        }
        System.out.println("==========================================================================");
        System.out.println("Total contacts: " + contacts.size());
    }

    public void updateContact() {
        System.out.print("Enter name to update: ");
        String name = sc.nextLine();

        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter new phone: ");
                String phone = sc.nextLine();
                if (isValidPhone(phone)) c.setPhone(phone);
                else System.out.println("Invalid phone! Not updated.");

                System.out.print("Enter new email: ");
                String email = sc.nextLine();
                if (isValidEmail(email)) c.setEmail(email);
                else System.out.println("Invalid email! Not updated.");

                System.out.println("Contact updated!");
                return;
            }
        }
        System.out.println("Contact not found.");
    }

    public void deleteContact() {
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine();

        Iterator<Contact> it = contacts.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Contact c = it.next();
            if (c.getName().equalsIgnoreCase(name)) {
                System.out.print("Are you sure? (y/n): ");
                String confirm = sc.nextLine();
                if (confirm.equalsIgnoreCase("y")) {
                    it.remove();
                    System.out.println("Contact deleted.");
                }
                found = true;
                break;
            }
        }
        if (!found) System.out.println("Contact not found.");
    }

    public void searchContact() {
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(keyword) ||
                c.getPhone().contains(keyword) ||
                c.getEmail().toLowerCase().contains(keyword)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) System.out.println("No matching contact found.");
    }

    public void exportToCSV() {
        try (PrintWriter pw = new PrintWriter(new File("contacts.csv"))) {
            for (Contact c : contacts) {
                pw.println(c.toCSV());
            }
            System.out.println("Exported to contacts.csv");
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    public void importFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                Contact c = Contact.fromCSV(line);
                if (c != null) contacts.add(c);
                count++;
            }
            System.out.println("Imported " + count + " contacts.");
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }
}


