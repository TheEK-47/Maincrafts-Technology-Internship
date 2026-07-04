/**
 * Student.java
 * Data model representing a single student record.
 * Reused from Task 2 – fields remain id, name, age.
 * File serialization methods removed (no longer needed – MySQL handles persistence).
 *
 * Author      : Anuska Roy
 * Project     : JDBC-Based Student Management System – Task 3
 * Organisation: Maincrafts Technology
 */
public class Student {

    private int    id;
    private String name;
    private int    age;

    public Student(int id, String name, int age) {
        this.id   = id;
        this.name = name;
        this.age  = age;
    }

    // Getters
    public int    getId()   { return id;   }
    public String getName() { return name; }
    public int    getAge()  { return age;  }

    // Setters (used during update)
    public void setName(String name) { this.name = name; }
    public void setAge(int age)      { this.age  = age;  }

    @Override
    public String toString() {
        return String.format("| %-5d | %-25s | %-3d |", id, name, age);
    }
}
