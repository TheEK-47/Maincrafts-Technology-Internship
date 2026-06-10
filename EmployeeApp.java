/**
 * EmployeeApp.java
 * Main application class – menu-driven Employee Management System.
 * Demonstrates: ArrayList, Scanner, loops, conditionals, method decomposition.
 *
 * Author: Anuska Roy
 * Project: Core Java – Employee Management System (Task 1)
 * Organisation: Maincrafts Technology
 *
 * Features:
 *   1. Add a new employee
 *   2. View all employees
 *   3. Search employee by ID
 *   4. Update employee department
 *   5. Delete an employee          
 *   6. Exit
 */

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EmployeeApp {
    static ArrayList<Employee> employees = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=================================================");
        System.out.println("   Maincrafts Technology – Employee Management   ");
        System.out.println("=================================================");

        // Pre-added records of system
        employees.add(new Employee(101, "Anuska Roy",     "Engineering"));
        employees.add(new Employee(102, "Rahul Sharma",   "Human Resources"));
        employees.add(new Employee(103, "Priya Mehta",    "Finance"));

        boolean running = true;

        while (running) {
            printMenu();

            int choice = -1;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine(); // flush bad token
                System.out.println("[!] Please enter a number between 1 and 6.\n");
                continue;
            }

            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> viewEmployees();
                case 3 -> searchEmployee();
                case 4 -> updateDepartment();
                case 5 -> deleteEmployee();
                case 6 -> {
                    System.out.println("\nGoodbye! Exiting Employee Management System.");
                    running = false;
                }
                default -> System.out.println("[!] Invalid option. Please choose 1–6.\n");
            }
        }

        sc.close();
    }

    static void printMenu() {
        System.out.println("\n----- MAIN MENU -----");
        System.out.println(" 1. Add Employee");
        System.out.println(" 2. View All Employees");
        System.out.println(" 3. Search Employee by ID");
        System.out.println(" 4. Update Employee Department");
        System.out.println(" 5. Delete Employee");
        System.out.println(" 6. Exit");
        System.out.print("Enter your choice: ");
    }

    static void addEmployee() {
        sc.nextLine(); 

        System.out.println("\n--- Add New Employee ---");

        // Validate ID input
        int id = -1;
        while (id <= 0) {
            System.out.print("Employee ID (positive integer): ");
            try {
                id = Integer.parseInt(sc.nextLine().trim());
                if (id <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid ID. Please enter a positive integer.");
                id = -1;
            }
        }

        // Check for duplicate ID
        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.println("[!] An employee with ID " + id + " already exists.");
                return;
            }
        }

        System.out.print("Name                : ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[!] Name cannot be blank.");
            return;
        }

        System.out.print("Department          : ");
        String dept = sc.nextLine().trim();
        if (dept.isEmpty()) {
            System.out.println("[!] Department cannot be blank.");
            return;
        }

        employees.add(new Employee(id, name, dept));
        System.out.println("[✓] Employee added successfully.");
    }

    
    static void viewEmployees() {
        System.out.println("\n--- All Employees ---");

        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }

        // Table header
        System.out.println("+-------+----------------------+----------------------+");
        System.out.println("| ID    | Name                 | Department           |");
        System.out.println("+-------+----------------------+----------------------+");

        for (Employee e : employees) {
            System.out.println(e); // uses Employee.toString()
        }

        System.out.println("+-------+----------------------+----------------------+");
        System.out.println("Total records: " + employees.size());
    }

    static void searchEmployee() {
        System.out.println("\n--- Search Employee ---");
        System.out.print("Enter Employee ID: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID format.");
            return;
        }

        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.println("\n[✓] Employee Found:");
                System.out.println("+-------+----------------------+----------------------+");
                System.out.println("| ID    | Name                 | Department           |");
                System.out.println("+-------+----------------------+----------------------+");
                System.out.println(e);
                System.out.println("+-------+----------------------+----------------------+");
                return;
            }
        }

        System.out.println("[!] Employee with ID " + id + " not found.");
    }

    static void updateDepartment() {
        System.out.println("\n--- Update Department ---");
        System.out.print("Enter Employee ID: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID format.");
            return;
        }
        sc.nextLine(); 

        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.println("Current Department : " + e.getDepartment());
                System.out.print("New Department     : ");
                String newDept = sc.nextLine().trim();

                if (newDept.isEmpty()) {
                    System.out.println("[!] Department cannot be blank. No changes made.");
                    return;
                }

                e.setDepartment(newDept);
                System.out.println("[✓] Department updated successfully.");
                return;
            }
        }

        System.out.println("[!] Employee with ID " + id + " not found.");
    }

    static void deleteEmployee() {
        System.out.println("\n--- Delete Employee ---");
        System.out.print("Enter Employee ID to delete: ");

        int id;
        try {
            id = sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("[!] Invalid ID format.");
            return;
        }

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                String name = employees.get(i).getName();
                employees.remove(i);
                System.out.println("[✓] Employee '" + name + "' (ID: " + id + ") deleted.");
                return;
            }
        }

        System.out.println("[!] Employee with ID " + id + " not found.");
    }
}
