/**
 * Employee.java
 * Represents a single employee record in the system.
 * Demonstrates: Class definition, fields, constructors, encapsulation.
 *
 * Author: Anuska Roy
 * Project: Core Java – Employee Management System (Task 1)
 * Organisation: Maincrafts Technology
 */
public class Employee {

    // Fields (data attributes of an employee)
    private int id;
    private String name;
    private String department;

    
    public Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    
    public void setDepartment(String department) {
        this.department = department;
    }

    
    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-20s |", id, name, department);
    }
}
