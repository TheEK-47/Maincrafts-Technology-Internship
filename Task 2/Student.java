/**
 * Student.java
 * Data model for a single student record.
 * Includes file serialization (toFileString) and deserialization (fromFileString)
 * to support persistent storage in students.txt.
 *
 * Author      : Anuska Roy
 * Project     : File-Based Persistent Student Management System – Task 2
 * Organisation: Maincrafts Technology
 *
 * Concepts demonstrated:
 *   - Encapsulation (private fields + public accessors)
 *   - Static factory method (fromFileString)
 *   - Basic data serialization / deserialization
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

    
    public int    getId()   { return id;   }
    public String getName() { return name; }
    public int    getAge()  { return age;  }

    
    public void setAge(int age) { this.age = age; }

    
    // Serialization : Object  →  "id,name,age"  (one line in file)
    
    public String toFileString() {
        return id + "," + name + "," + age;
    }

    
    // Deserialization : "id,name,age"  →  Student object
    // Static factory method; used by FileManager when loading.
    
    public static Student fromFileString(String line) {
        String[] parts = line.split(",", 3);   // limit=3 handles names with commas
        int    id   = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        int    age  = Integer.parseInt(parts[2].trim());
        return new Student(id, name, age);
    }

    
    // Human-readable display format (used by view & search)
    
    @Override
    public String toString() {
        return String.format("| %-5d | %-22s | %-3d |", id, name, age);
    }
}
