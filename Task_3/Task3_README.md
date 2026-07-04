# JDBC-Based Student Management System
**Core Java Project – Task 3 | Maincrafts Technology**
**Author:** Anuska Roy | Java Developer Intern

---

## Overview
This project upgrades the file-based persistence from Task 2 to **real database-driven persistence** using **JDBC (Java Database Connectivity)** with **MySQL**. All student data is stored in a relational database, fetched dynamically using SQL queries, and modified via `PreparedStatement` — the industry-standard pattern for safe database access in Java.

---

## Project Structure
```
Task3_JDBC/
├── src/
│   ├── Student.java        # Data model (reused from Task 2, serialization removed)
│   ├── DBConnection.java   # JDBC connection manager (DriverManager)
│   ├── StudentDAO.java     # Data Access Object – all SQL CRUD operations
│   └── StudentApp.java     # Main application – menu + user interaction
├── sql/
│   └── setup.sql           # MySQL: CREATE DATABASE, CREATE TABLE, sample INSERT
├── screenshots/
│   └── output_transcript.txt  # Full console output across all operations
└── README.md
```

---

## Prerequisites & Setup

### 1. Install MySQL
Download from https://dev.mysql.com/downloads/  
Default port: **3306**

### 2. Download MySQL JDBC Connector (Driver)
Download `mysql-connector-j-*.jar` from:  
https://dev.mysql.com/downloads/connector/j/  
Place the `.jar` in the same folder as your `.java` files.

### 3. Create the Database and Table
```bash
# Option A – MySQL CLI
mysql -u root -p < sql/setup.sql

# Option B – MySQL Workbench
# File → Open SQL Script → setup.sql → Execute All
```

### 4. Update Credentials in DBConnection.java
```java
private static final String DB_USER     = "root";
private static final String DB_PASSWORD = "your_password";  // change this
```

### 5. Compile and Run
```bash
# Windows
javac -cp .;mysql-connector-j-8.3.0.jar src/*.java -d out/
java  -cp .;mysql-connector-j-8.3.0.jar;out/ StudentApp

# Mac / Linux
javac -cp .:mysql-connector-j-8.3.0.jar src/*.java -d out/
java  -cp .:mysql-connector-j-8.3.0.jar:out/ StudentApp
```

---

## JDBC Flow Explained

```
Java Application
      │
      ▼
DBConnection.getConnection()
      │   uses DriverManager.getConnection(URL, USER, PASS)
      │   returns java.sql.Connection
      ▼
StudentDAO (Data Access Object)
      │
      ├── addStudent()      → PreparedStatement → INSERT INTO students
      ├── getAllStudents()   → Statement         → SELECT * FROM students
      ├── getStudentById()  → PreparedStatement → SELECT WHERE id = ?
      ├── updateStudent()   → PreparedStatement → UPDATE students SET ...
      └── deleteStudent()   → PreparedStatement → DELETE FROM students WHERE id = ?
      │
      ▼
MySQL Database (student_db → students table)
```

### Step-by-step JDBC process for every operation:
1. `DBConnection.getConnection()` — loads the MySQL driver, opens a `Connection`
2. `con.prepareStatement(sql)` — compiles the parameterised SQL query
3. `ps.setInt()` / `ps.setString()` — binds actual values into `?` placeholders
4. `ps.executeUpdate()` (for INSERT/UPDATE/DELETE) or `ps.executeQuery()` (for SELECT)
5. For SELECT: iterate `ResultSet` with `rs.next()`, read columns with `rs.getInt()` / `rs.getString()`
6. try-with-resources — automatically closes `Connection`, `PreparedStatement`, `ResultSet`

---

## Class Design

### `DBConnection.java`

| Method | Purpose |
|--------|---------|
| `getConnection()` | Returns a `Connection` using `DriverManager`; returns `null` on failure |
| `testConnection()` | Called on startup to verify DB is reachable before entering menu |

**Connection URL:**
```
jdbc:mysql://localhost:3306/student_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

### `StudentDAO.java`

| Method | SQL Operation | Statement Type |
|--------|--------------|---------------|
| `addStudent(Student)` | `INSERT INTO students VALUES (?,?,?)` | `PreparedStatement` |
| `getAllStudents()` | `SELECT * FROM students ORDER BY id` | `Statement` |
| `getStudentById(int)` | `SELECT * FROM students WHERE id = ?` | `PreparedStatement` |
| `updateStudent(id, name, age)` | `UPDATE students SET name=?, age=? WHERE id=?` | `PreparedStatement` |
| `deleteStudent(int)` | `DELETE FROM students WHERE id = ?` | `PreparedStatement` |
| `getStudentCount()` | `SELECT COUNT(*) FROM students` | `Statement` |

### `StudentApp.java`

| Menu Option | Method | DAO Call |
|------------|--------|----------|
| 1. Add Student | `addStudent()` | `StudentDAO.addStudent()` |
| 2. View All | `viewAllStudents()` | `StudentDAO.getAllStudents()` |
| 3. Search by ID | `searchStudent()` | `StudentDAO.getStudentById()` |
| 4. Update | `updateStudent()` | `StudentDAO.updateStudent()` |
| 5. Delete | `deleteStudent()` | `StudentDAO.deleteStudent()` |

---

## Database Schema

```sql
CREATE DATABASE IF NOT EXISTS student_db;

CREATE TABLE students (
    id   INT          NOT NULL,
    name VARCHAR(100) NOT NULL,
    age  INT          NOT NULL,
    CONSTRAINT pk_student PRIMARY KEY (id),
    CONSTRAINT chk_age    CHECK (age > 0 AND age <= 150)
);
```

---

## Key Concepts Applied

| Concept | Where Used |
|---------|-----------|
| **JDBC** | Full connectivity via `java.sql.*` |
| **DriverManager** | `DBConnection.getConnection()` |
| **Connection** | Returned by `getConnection()`, used in every DAO method |
| **PreparedStatement** | All INSERT / UPDATE / DELETE / parameterised SELECT |
| **Statement** | Simple SELECT * and COUNT(*) |
| **ResultSet** | `getAllStudents()`, `getStudentById()`, `getStudentCount()` |
| **SQLException** | Caught in every DAO method with meaningful messages |
| **SQLIntegrityConstraintViolationException** | Duplicate ID detection in `addStudent()` |
| **try-with-resources** | Auto-closes all DB resources in every DAO method |
| **DAO Pattern** | Separates SQL logic from application/menu logic |
| **Input validation** | Scanner input guarded with try-catch throughout |

---

## Task 2 vs Task 3 Comparison

| Aspect | Task 2 (File-Based) | Task 3 (JDBC / MySQL) |
|--------|--------------------|-----------------------|
| Storage | `students.txt` (flat file) | MySQL `students` table |
| Write | `FileWriter.write()` | `PreparedStatement.executeUpdate()` |
| Read | `BufferedReader.readLine()` | `ResultSet.next()` |
| Query | Full file scan | SQL `WHERE` clause (indexed) |
| Update | Rewrite entire file | `UPDATE` single row |
| Delete | Rewrite entire file | `DELETE` single row |
| Scalability | Poor (grows with file) | High (DB handles millions of rows) |
| Prepares for | File I/O patterns | JDBC, Spring Data, ORM (Hibernate) |

---

## Common Errors & Fixes

| Error | Cause | Fix |
|-------|-------|-----|
| `ClassNotFoundException` | JDBC driver jar not in classpath | Add `-cp .:mysql-connector-j-*.jar` |
| `Access denied for user 'root'` | Wrong password | Update `DB_PASSWORD` in `DBConnection.java` |
| `Unknown database 'student_db'` | setup.sql not run | Run `setup.sql` first |
| `Communications link failure` | MySQL not running | Start MySQL service |
| `Duplicate entry for key PRIMARY` | Duplicate student ID | Caught and handled in `addStudent()` |

---

## Bonus Features (Beyond Core Task)
- `getStudentById()` for search — not in the starter code
- Delete confirmation prompt before executing DELETE
- Update preserves current value if user presses Enter / 0
- Startup connection test before entering menu loop
- `SQLIntegrityConstraintViolationException` caught separately for clear duplicate-ID messaging
- DB-level `CHECK` constraint on age in `setup.sql`
