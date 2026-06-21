/**
 * StudentApp.java
 * Main application – menu-driven Student Management System with file persistence.
 *
 * Author      : Anuska Roy
 * Project     : File-Based Persistent Student Management System – Task 2
 * Organisation: Maincrafts Technology
 *
 * Menu options:
 *   1. Add Student          (CREATE  – saved to students.txt immediately)
 *   2. View All Students    (READ    – loaded from students.txt on startup)
 *   3. Search Student by ID (READ    – linear search on in-memory list)
 *   4. Update Student Age   (UPDATE  – file rewritten after change)  
 *   5. Delete Student       (DELETE  – file rewritten after removal) 
 *   6. Exit
 *
 * Concepts demonstrated:
 *   - File-based persistence (data survives program restart)
 *   - ArrayList as in-memory working copy
 *   - Scanner for user input
 *   - Exception handling (InputMismatchException, validation)
 *   - Clean method decomposition
 */

import java.util.*;

public class StudentApp {

    
    private static List<Student> students;

    
    public static void main(String[] args) {

        // Load any previously saved students from file
        students = FileManager.loadStudents();

        Scanner sc = new Scanner(System.in);

        printBanner();
        System.out.println("[i] Data file: " + FileManager.getFileName());
        System.out.println("[i] Records loaded from file: " + students.size());

        boolean running = true;

        while (running) {
            printMenu();

            int choice = -1;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("[!] Invalid input. Please enter a number 1–6.\n");
                continue;
            }

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> searchStudent(sc);
                case 4 -> updateStudentAge(sc);
                case 5 -> deleteStudent(sc);
                case 6 -> {
                    System.out.println("\nGoodbye! All data has been saved to '"
                            + FileManager.getFileName() + "'.");
                    running = false;
                }
                default -> System.out.println("[!] Invalid option. Choose 1–6.\n");
            }
        }

        sc.close();
    }

    
    // printBanner()
    
    static void printBanner() {
        System.out.println("=======================================================");
        System.out.println("  Maincrafts Technology – Student Management System   ");
        System.out.println("         File-Based Persistence  |  Task 2            ");
        System.out.println("=======================================================");
    }

    
    // printMenu()
    
    static void printMenu() {
        System.out.println("\n----- MAIN MENU -----");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Search Student by ID");
        System.out.println(" 4. Update Student Age");
        System.out.println(" 5. Delete Student");
        System.out.println(" 6. Exit");
        System.out.print("Enter your choice: ");
    }

    
    // 1. addStudent() – CREATE + persist to file
    
    static void addStudent(Scanner sc) {
        sc.nextLine(); // consume leftover newline

        System.out.println("\n--- Add New Student ---");

        // --- ID ---
        int id = -1;
        while (id <= 0) {
            System.out.print("Student ID (positive integer): ");
            try {
                id = Integer.parseInt(sc.nextLine().trim());
                if (id <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid ID. Enter a positive integer.");
                id = -1;
            }
        }

        // Duplicate ID check
        for (Student s : students) {
            if (s.getId() == id) {
                System.out.println("[!] Student with ID " + id + " already exists.");
                return;
            }
        }

        
        System.out.print("Name                        : ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("[!] Name cannot be blank."); return; }

        
        int age = -1;
        while (age <= 0 || age > 150) {
            System.out.print("Age                         : ");
            try {
                age = Integer.parseInt(sc.nextLine().trim());
                if (age <= 0 || age > 150) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid age. Enter a realistic positive number.");
                age = -1;
            }
        }

        Student student = new Student(id, name, age);
        students.add(student);
        FileManager.saveStudent(student);   // << persist immediately

        System.out.println("[✓] Student saved successfully to '"
                + FileManager.getFileName() + "'.");
    }

    
    // 2. viewStudents() – READ all (from in-memory list)
    
    static void viewStudents() {
        System.out.println("\n--- All Students ---");

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        printTableHeader();
        for (Student s : students) {
            System.out.println(s);
        }
        printTableFooter();
        System.out.println("Total records: " + students.size());
    }

    
    // 3. searchStudent() – READ by ID
    
    static void searchStudent(Scanner sc) {
        System.out.println("\n--- Search Student ---");
        System.out.print("Enter Student ID: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID format.");
            return;
        }

        for (Student s : students) {
            if (s.getId() == id) {
                System.out.println("\n[✓] Student Found:");
                printTableHeader();
                System.out.println(s);
                printTableFooter();
                return;
            }
        }

        System.out.println("[!] Student with ID " + id + " not found.");
    }

    
    // 4. updateStudentAge() – UPDATE + rewrite file  [bonus]
    
    static void updateStudentAge(Scanner sc) {
        System.out.println("\n--- Update Student Age ---");
        System.out.print("Enter Student ID: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID format.");
            return;
        }
        sc.nextLine();

        for (Student s : students) {
            if (s.getId() == id) {
                System.out.println("Current Age : " + s.getAge());
                int newAge = -1;
                while (newAge <= 0 || newAge > 150) {
                    System.out.print("New Age     : ");
                    try {
                        newAge = Integer.parseInt(sc.nextLine().trim());
                        if (newAge <= 0 || newAge > 150) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Invalid age.");
                        newAge = -1;
                    }
                }
                s.setAge(newAge);
                FileManager.rewriteAllStudents(students);   // << sync file
                System.out.println("[✓] Age updated and file saved.");
                return;
            }
        }

        System.out.println("[!] Student with ID " + id + " not found.");
    }

    
    // 5. deleteStudent() – DELETE + rewrite file  [bonus]
    
    static void deleteStudent(Scanner sc) {
        System.out.println("\n--- Delete Student ---");
        System.out.print("Enter Student ID to delete: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID format.");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                String name = students.get(i).getName();
                students.remove(i);
                FileManager.rewriteAllStudents(students);   // << sync file
                System.out.println("[✓] Student '" + name + "' (ID: " + id
                        + ") deleted and file updated.");
                return;
            }
        }

        System.out.println("[!] Student with ID " + id + " not found.");
    }

    
    // Table formatting helpers
    
    static void printTableHeader() {
        System.out.println("+-------+------------------------+-----+");
        System.out.println("| ID    | Name                   | Age |");
        System.out.println("+-------+------------------------+-----+");
    }

    static void printTableFooter() {
        System.out.println("+-------+------------------------+-----+");
    }
}
