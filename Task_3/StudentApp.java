/**
 * StudentApp.java
 * Main application – menu-driven Student Management System backed by MySQL via JDBC.
 * Replaces FileManager (Task 2) with StudentDAO for all persistence operations.
 *
 * Author      : Anuska Roy
 * Project     : JDBC-Based Student Management System – Task 3
 * Organisation: Maincrafts Technology
 *
 * Menu:
 *   1. Add Student        (INSERT into MySQL)
 *   2. View All Students  (SELECT * from MySQL)
 *   3. Search by ID       (SELECT WHERE id)
 *   4. Update Student     (UPDATE name & age)
 *   5. Delete Student     (DELETE WHERE id)
 *   6. Exit
 *
 * How to run:
 *   1. Start MySQL and run setup.sql to create student_db and students table.
 *   2. Compile: javac -cp .;mysql-connector-j-*.jar *.java   (Windows)
 *               javac -cp .:mysql-connector-j-*.jar *.java   (Mac/Linux)
 *   3. Run:     java  -cp .;mysql-connector-j-*.jar StudentApp
 */

import java.util.*;

public class StudentApp {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        printBanner();

        // Verify DB connection before entering menu loop
        System.out.print("[i] Connecting to MySQL... ");
        if (!DBConnection.testConnection()) {
            System.out.println("\n[✗] Cannot connect to database. Check setup.sql and DBConnection.java.");
            System.out.println("    Exiting.");
            return;
        }
        System.out.println("Connected ✓");
        System.out.println("[i] Records in database: " + StudentDAO.getStudentCount());

        boolean running = true;

        while (running) {
            printMenu();

            int choice = -1;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("[!] Enter a number 1–6.\n");
                continue;
            }

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> {
                    System.out.println("\nGoodbye! Data is safely stored in MySQL.");
                    running = false;
                }
                default -> System.out.println("[!] Invalid option. Choose 1–6.");
            }
        }

        sc.close();
    }

    
    static void printBanner() {
        System.out.println("==========================================================");
        System.out.println("   Maincrafts Technology – Student Management System      ");
        System.out.println("      Database Integration via JDBC  |  Task 3            ");
        System.out.println("==========================================================");
    }

    static void printMenu() {
        System.out.println("\n----- MAIN MENU -----");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Search Student by ID");
        System.out.println(" 4. Update Student");
        System.out.println(" 5. Delete Student");
        System.out.println(" 6. Exit");
        System.out.print("Enter your choice: ");
    }

    static void printTableHeader() {
        System.out.println("+-------+---------------------------+-----+");
        System.out.println("| ID    | Name                      | Age |");
        System.out.println("+-------+---------------------------+-----+");
    }

    static void printTableFooter() {
        System.out.println("+-------+---------------------------+-----+");
    }

    
    static void addStudent() {
        sc.nextLine();
        System.out.println("\n--- Add New Student ---");

        // Validate ID
        int id = -1;
        while (id <= 0) {
            System.out.print("Student ID (positive integer): ");
            try {
                id = Integer.parseInt(sc.nextLine().trim());
                if (id <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid ID.");
                id = -1;
            }
        }

        // Name
        System.out.print("Name                         : ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("[!] Name cannot be blank."); return; }

        // Age
        int age = -1;
        while (age <= 0 || age > 150) {
            System.out.print("Age                          : ");
            try {
                age = Integer.parseInt(sc.nextLine().trim());
                if (age <= 0 || age > 150) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid age.");
                age = -1;
            }
        }

        Student s = new Student(id, name, age);
        if (StudentDAO.addStudent(s)) {
            System.out.println("[✓] Student added to MySQL successfully.");
        } else {
            System.out.println("[✗] Failed to add student.");
        }
    }

    
    static void viewAllStudents() {
        System.out.println("\n--- All Students (from MySQL) ---");
        List<Student> list = StudentDAO.getAllStudents();

        if (list.isEmpty()) {
            System.out.println("No student records found in database.");
            return;
        }

        printTableHeader();
        for (Student s : list) System.out.println(s);
        printTableFooter();
        System.out.println("Total records in DB: " + list.size());
    }

    
    static void searchStudent() {
        System.out.println("\n--- Search Student by ID ---");
        System.out.print("Enter Student ID: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID.");
            return;
        }

        Student s = StudentDAO.getStudentById(id);
        if (s != null) {
            System.out.println("\n[✓] Student Found:");
            printTableHeader();
            System.out.println(s);
            printTableFooter();
        } else {
            System.out.println("[!] Student with ID " + id + " not found in database.");
        }
    }

    
    static void updateStudent() {
        System.out.println("\n--- Update Student ---");
        System.out.print("Enter Student ID to update: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID.");
            return;
        }
        sc.nextLine();

        // Confirm student exists first
        Student existing = StudentDAO.getStudentById(id);
        if (existing == null) {
            System.out.println("[!] Student with ID " + id + " not found.");
            return;
        }

        System.out.println("Current Name : " + existing.getName());
        System.out.println("Current Age  : " + existing.getAge());

        System.out.print("New Name (press Enter to keep current): ");
        String newName = sc.nextLine().trim();
        if (newName.isEmpty()) newName = existing.getName();

        int newAge = -1;
        while (newAge <= 0 || newAge > 150) {
            System.out.print("New Age  (press 0 to keep current)   : ");
            try {
                newAge = Integer.parseInt(sc.nextLine().trim());
                if (newAge == 0) { newAge = existing.getAge(); break; }
                if (newAge < 0 || newAge > 150) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid age.");
                newAge = -1;
            }
        }

        if (StudentDAO.updateStudent(id, newName, newAge)) {
            System.out.println("[✓] Student updated in MySQL successfully.");
        } else {
            System.out.println("[✗] Update failed.");
        }
    }

    
    static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        System.out.print("Enter Student ID to delete: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID.");
            return;
        }

        // Show record before deleting
        Student existing = StudentDAO.getStudentById(id);
        if (existing == null) {
            System.out.println("[!] Student with ID " + id + " not found.");
            return;
        }

        System.out.println("Deleting: " + existing.getName() + " (ID: " + id + ")");
        System.out.print("Confirm delete? (yes/no): ");
        sc.nextLine();
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            if (StudentDAO.deleteStudent(id)) {
                System.out.println("[✓] Student deleted from MySQL successfully.");
            } else {
                System.out.println("[✗] Delete failed.");
            }
        } else {
            System.out.println("[i] Delete cancelled.");
        }
    }
}
