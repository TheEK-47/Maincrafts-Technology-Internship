# File-Based Persistent Student Management System
**Core Java Project – Task 2 | Maincrafts Technology**
**Author:** Anuska Roy | Java Developer Intern

---

## Overview
An enhancement of the Task 1 console-based system — this application adds **file-based data persistence** using Java I/O classes. Student records are saved to `students.txt` and automatically reloaded every time the program starts, meaning **data survives program restarts**.

---

## Project Structure
```
StudentManagementSystem/
├── src/
│   ├── Student.java          # Data model + serialization methods
│   ├── FileManager.java      # All file I/O logic (read / write / rewrite)
│   └── StudentApp.java       # Main application + menu
├── data/
│   └── students.txt          # Persistent data file (auto-created on first add)
├── screenshots/
│   └── output_transcript.txt # Full console output across 2 program runs
└── README.md
```

---

## How to Compile and Run

### Prerequisites
- JDK 8 or higher
- Terminal / Command Prompt / Any IDE (IntelliJ, Eclipse, VS Code)

### Steps
```bash
# 1. Navigate to the src directory
cd StudentManagementSystem/src

# 2. Compile all three source files
javac Student.java FileManager.java StudentApp.java

# 3. Run the application
java StudentApp
```

> `students.txt` will be created automatically in the same directory on the first `Add Student` operation.

---

## How File Persistence Works

### Writing (Save)
When a student is added via option 1, `FileManager.saveStudent()` is called immediately:

```java
FileWriter fw = new FileWriter("students.txt", true)  // append=true
fw.write(student.toFileString() + "\n");
// e.g. writes: "201,Anuska Roy,21"
```

The `true` flag means **existing records are preserved** — only the new line is appended.

### Reading (Load on startup)
Every time the program launches, `FileManager.loadStudents()` reads the file line by line:

```java
BufferedReader br = new BufferedReader(new FileReader("students.txt"));
String line;
while ((line = br.readLine()) != null) {
    students.add(Student.fromFileString(line));
}
```

`BufferedReader` wraps `FileReader` for **efficient, buffered reading** — this is the standard pattern for text file input in Java.

### File Format
Each student is stored as a single comma-separated line:

```
id,name,age
201,Anuska Roy,21
202,Rahul Sharma,22
203,Priya Mehta,20
```

This is **basic data serialization** — converting an object to a string format that can be stored and reconstructed later.

### Rewriting (Update / Delete)
When a student is updated or deleted, the entire file is rewritten from the current in-memory list:

```java
FileWriter fw = new FileWriter("students.txt", false)  // false = overwrite
for (Student s : students) {
    fw.write(s.toFileString() + "\n");
}
```

This keeps the file perfectly in sync with in-memory state after every modification.

---

## Class Design

### `Student.java`

| Member | Purpose |
|--------|---------|
| `id`, `name`, `age` | Private fields (encapsulation) |
| `Student(id, name, age)` | Constructor |
| `toFileString()` | Converts object → `"id,name,age"` string for file storage |
| `fromFileString(line)` | Static factory: parses a file line → `Student` object |
| `toString()` | Formatted table-row display |

### `FileManager.java`

| Method | Purpose |
|--------|---------|
| `saveStudent(Student)` | Appends one record to file (`FileWriter`, append mode) |
| `loadStudents()` | Reads all records from file (`BufferedReader` + `FileReader`) |
| `rewriteAllStudents(List)` | Overwrites file with full list (used after update/delete) |

### `StudentApp.java`

| Method | CRUD | File Operation |
|--------|------|---------------|
| `addStudent()` | Create | `saveStudent()` — append |
| `viewStudents()` | Read | None (uses in-memory list) |
| `searchStudent()` | Read | None (linear search) |
| `updateStudentAge()` | Update | `rewriteAllStudents()` — overwrite |
| `deleteStudent()` | Delete | `rewriteAllStudents()` — overwrite |

---

## Key Java Concepts Applied

| Concept | Class / Location |
|---------|-----------------|
| **FileWriter** (append) | `FileManager.saveStudent()` |
| **FileReader + BufferedReader** | `FileManager.loadStudents()` |
| **try-with-resources** | All file operations — ensures streams close automatically |
| **FileNotFoundException** | Caught in `loadStudents()` — handles first-run gracefully |
| **IOException** | Caught in all file methods — safe error handling |
| **Data Serialization** | `Student.toFileString()` |
| **Data Deserialization** | `Student.fromFileString()` |
| **Static factory method** | `Student.fromFileString()` |
| **ArrayList** | In-memory working copy of student list |
| **Encapsulation** | Private fields + getters/setter in `Student` |
| **InputMismatchException** | Menu and ID input validation |

---

## Persistence Proof (2-Run Demonstration)

The `output_transcript.txt` in `/screenshots` demonstrates:

**Run 1:** Program starts fresh (0 records). Three students added → saved to file → exit.

**Run 2:** Program restarts → **3 records automatically loaded from `students.txt`** → new student added, search performed, age updated, student deleted → exit.

This confirms data persists correctly across restarts.

---

## Bonus Features (Beyond Core Task Requirements)

- **Update Student Age** (option 4) — file rewritten after each update
- **Delete Student** (option 5) — file rewritten after each deletion, completing full CRUD
- Input validation on all fields (ID type, duplicate ID, blank name, age range)
- Malformed file-line handling (skips bad records with a warning instead of crashing)
- Startup banner showing file name and record count loaded

---

## In-Memory vs File Storage — Key Difference

| Aspect | Task 1 (In-Memory) | Task 2 (File-Based) |
|--------|-------------------|---------------------|
| Storage | `ArrayList` only | `ArrayList` + `students.txt` |
| After restart | All data lost | All data reloaded from file |
| Write operation | Instant (RAM) | `FileWriter` to disk |
| Read operation | Instant (RAM) | `BufferedReader` from disk |
| Represents | Temporary state | Persistent storage |
| Prepares for | — | JDBC & database-driven apps |
