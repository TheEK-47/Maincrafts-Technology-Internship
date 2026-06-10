# Employee Management System
**Core Java Project – Task 1 | Maincrafts Technology**
**Author:** Anuska Roy | Java Developer Intern

---

## Overview
A console-based Employee Management System built entirely with Core Java. The application mimics real-world internal HR tooling, supporting full **CRUD** (Create, Read, Update, Delete) operations on employee records through an interactive menu.

---

## Project Structure
```
EmployeeManagementSystem/
├── src/
│   ├── Employee.java        # Data model – represents a single employee
│   └── EmployeeApp.java     # Main application – menu, logic, all methods
├── screenshots/
│   └── output_transcript.txt  # Console output demonstrating all features
└── README.md
```

---

## How to Compile and Run

### Prerequisites
- JDK 11 or higher installed
- Terminal / Command Prompt

### Steps
```bash
# 1. Navigate to the src directory
cd EmployeeManagementSystem/src

# 2. Compile both source files
javac Employee.java EmployeeApp.java

# 3. Run the application
java EmployeeApp
```

---

## Program Flow

### On Launch
The app pre-loads 3 demo employee records and displays the main menu in a continuous loop.

```
=================================================
   Maincrafts Technology – Employee Management   
=================================================

----- MAIN MENU -----
 1. Add Employee
 2. View All Employees
 3. Search Employee by ID
 4. Update Employee Department
 5. Delete Employee
 6. Exit
Enter your choice: _
```

### Menu Options Explained

| Option | Feature | Method | Operation |
|--------|---------|--------|-----------|
| 1 | Add Employee | `addEmployee()` | CREATE |
| 2 | View All Employees | `viewEmployees()` | READ (all) |
| 3 | Search by ID | `searchEmployee()` | READ (single) |
| 4 | Update Department | `updateDepartment()` | UPDATE |
| 5 | Delete Employee | `deleteEmployee()` | DELETE |
| 6 | Exit | `System.exit()` | — |

---

## Class Design

### `Employee.java`
Represents a single employee record.

| Member | Type | Purpose |
|--------|------|---------|
| `id` | `int` | Unique identifier |
| `name` | `String` | Full name |
| `department` | `String` | Department assignment |
| `Employee(id, name, dept)` | Constructor | Initialises all fields |
| `getId()`, `getName()`, `getDepartment()` | Getters | Controlled read access |
| `setDepartment(String)` | Setter | Allows department update |
| `toString()` | Override | Formatted table-row output |

Private fields + public getters/setter = **encapsulation** in action.

### `EmployeeApp.java`
Main application class containing all business logic.

| Method | Responsibility |
|--------|---------------|
| `main()` | Entry point; runs the while-loop menu |
| `printMenu()` | Renders the 6-option menu |
| `addEmployee()` | Validates and adds a new Employee object to the ArrayList |
| `viewEmployees()` | Iterates the ArrayList and prints a formatted table |
| `searchEmployee()` | Linear search by ID; prints result or "not found" |
| `updateDepartment()` | Finds employee by ID, updates `department` field |
| `deleteEmployee()` | Finds by ID and removes from ArrayList |

---

## Key Java Concepts Applied

| Concept | Where Used |
|---------|-----------|
| **Class & Object** | `Employee` class, instantiated in `addEmployee()` |
| **Constructor** | `Employee(int, String, String)` |
| **Encapsulation** | Private fields with getters/setter in `Employee` |
| **ArrayList** | Dynamic storage of all employee records |
| **Scanner** | Reading all user input from `System.in` |
| **while loop** | Keeps menu running until user exits |
| **switch expression** | Routes menu choice to correct method (Java 14+ style) |
| **for-each loop** | Iterating employees in view, search, update, delete |
| **Input validation** | `try-catch` for `InputMismatchException`, empty string checks |
| **@Override** | Custom `toString()` for formatted output |

---

## Input Validation & Edge Cases Handled

- Non-integer input at menu → caught with `InputMismatchException`, prompts retry
- Duplicate Employee ID → rejected with a clear message
- Blank name or department → rejected with a clear message
- Searching / updating / deleting a non-existent ID → informs user gracefully
- Negative or zero ID → rejected with validation loop

---

## Sample Output

```
--- Add New Employee ---
Employee ID (positive integer): 104
Name                : Siddharth Nair
Department          : Cybersecurity
[✓] Employee added successfully.

--- All Employees ---
+-------+----------------------+----------------------+
| ID    | Name                 | Department           |
+-------+----------------------+----------------------+
| 101   | Anuska Roy           | Engineering          |
| 102   | Rahul Sharma         | Human Resources      |
| 103   | Priya Mehta          | Finance              |
| 104   | Siddharth Nair       | Cybersecurity        |
+-------+----------------------+----------------------+
Total records: 4
```

---

## Bonus Feature
Option **5 – Delete Employee** was added beyond the core task requirements, completing the full CRUD cycle and making the system closer to a production-grade tool.

---

## Learning Outcomes Achieved
- Confident application of Java OOP: classes, objects, constructors, encapsulation
- `ArrayList` for dynamic, type-safe collections
- `Scanner` for robust user input handling
- Method decomposition for clean, readable code structure
- Real-world pattern: menu-driven, loop-based console application
