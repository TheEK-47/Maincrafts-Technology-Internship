/**
 * FileManager.java
 * Utility class responsible for all file I/O operations.
 * Keeps file-handling logic completely separate from application logic
 * (Single Responsibility Principle).
 *
 * Author      : Anuska Roy
 * Project     : File-Based Persistent Student Management System – Task 2
 * Organisation: Maincrafts Technology
 *
 * Concepts demonstrated:
 *   - FileWriter  (character-based file writing, append mode)
 *   - FileReader + BufferedReader  (efficient line-by-line reading)
 *   - FileNotFoundException  (graceful first-run handling)
 *   - IOException  (safe I/O exception handling)
 *   - try-with-resources  (automatic stream closing)
 *   - Data serialization / deserialization via Student helper methods
 */

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String FILE_NAME = "students.txt";

    
    // saveStudent()
    // Appends a single student record to the file.
    // Uses append=true so existing records are preserved.
    
    public static void saveStudent(Student student) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(student.toFileString() + "\n");
        } catch (IOException e) {
            System.out.println("[!] Error saving student data: " + e.getMessage());
        }
    }

    
    // loadStudents()
    // Reads all lines from the file and rebuilds Student objects.
    // Returns an empty list if the file does not exist yet
    // (first-run scenario – no error shown to user).
    
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {                          // skip blank lines
                    try {
                        students.add(Student.fromFileString(line));
                    } catch (Exception e) {
                        System.out.println("[!] Skipping malformed record: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Normal on first run – file will be created when first student is saved
        } catch (IOException e) {
            System.out.println("[!] Error reading student data: " + e.getMessage());
        }

        return students;
    }

    
    // rewriteAllStudents()
    // Overwrites the entire file with the current in-memory list.
    // Used after an update or delete so the file stays in sync.
    
    public static void rewriteAllStudents(List<Student> students) {
        try (FileWriter fw = new FileWriter(FILE_NAME, false)) {  // false = overwrite
            for (Student s : students) {
                fw.write(s.toFileString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("[!] Error updating student file: " + e.getMessage());
        }
    }

    
    // getFileName()  – used in README
    
    public static String getFileName() {
        return FILE_NAME;
    }
}
