/**
 * StudentDAO.java
 * Data Access Object – encapsulates all database CRUD operations for Student.
 * Separates SQL/database logic from application/menu logic (clean architecture).
 *
 * Author      : Anuska Roy
 * Project     : JDBC-Based Student Management System – Task 3
 * Organisation: Maincrafts Technology
 *
 * Concepts demonstrated:
 *   - PreparedStatement  (parameterised queries – prevents SQL injection)
 *   - Statement          (used for simple SELECT *)
 *   - ResultSet          (iterating rows returned by SELECT)
 *   - executeUpdate()    (INSERT / UPDATE / DELETE)
 *   - executeQuery()     (SELECT)
 *   - try-with-resources (auto-closes Connection, Statement, ResultSet)
 *   - SQLException handling
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    
    public static boolean addStudent(Student student) {
        String sql = "INSERT INTO students (id, name, age) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[!] Duplicate ID: Student with ID "
                    + student.getId() + " already exists in the database.");
        } catch (SQLException e) {
            System.out.println("[!] Error inserting student: " + e.getMessage());
        }
        return false;
    }

    
    public static List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                ));
            }

        } catch (SQLException e) {
            System.out.println("[!] Error fetching students: " + e.getMessage());
        }
        return list;
    }

    
    public static Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("[!] Error searching student: " + e.getMessage());
        }
        return null;
    }

    
    public static boolean updateStudent(int id, String newName, int newAge) {
        String sql = "UPDATE students SET name = ?, age = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setInt(2, newAge);
            ps.setInt(3, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("[!] Error updating student: " + e.getMessage());
        }
        return false;
    }

    
    public static boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("[!] Error deleting student: " + e.getMessage());
        }
        return false;
    }

    
    public static int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM students";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            // silently return 0 if DB issue
        }
        return 0;
    }
}
