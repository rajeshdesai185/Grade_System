import java.sql.*;
import java.util.*;

public class GradeManagerJDBC {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root"; // change if needed
    static final String PASS = "Raj@3280"; // change if needed

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            while (true) {
                System.out.println("\n1. Add Student\n2. View All Students\n3. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter student name: ");
                        String name = sc.nextLine();

                        int[] marks = new int[3];
                        int total = 0;
                        for (int i = 0; i < 3; i++) {
                            System.out.print("Enter mark " + (i+1) + ": ");
                            marks[i] = sc.nextInt();
                            total += marks[i];
                        }
                        double average = total / 3.0;
                        char grade;
                        if (average >= 90) grade = 'A';
                        else if (average >= 75) grade = 'B';
                        else if (average >= 60) grade = 'C';
                        else grade = 'D';

                        String sql = "INSERT INTO students (name, mark1, mark2, mark3, total, average, grade) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, name);
                            stmt.setInt(2, marks[0]);
                            stmt.setInt(3, marks[1]);
                            stmt.setInt(4, marks[2]);
                            stmt.setInt(5, total);
                            stmt.setDouble(6, average);
                            stmt.setString(7, String.valueOf(grade));
                            stmt.executeUpdate();
                            System.out.println("Student added successfully!");
                        }
                        break;

                    case 2:
                        sql = "SELECT * FROM students";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(sql)) {
                            System.out.println("\n--- Student Report ---");
                            while (rs.next()) {
                                System.out.println("ID: " + rs.getInt("id"));
                                System.out.println("Name: " + rs.getString("name"));
                                System.out.println("Marks: " +
                                    rs.getInt("mark1") + ", " +
                                    rs.getInt("mark2") + ", " +
                                    rs.getInt("mark3"));
                                System.out.println("Total: " + rs.getInt("total"));
                                System.out.printf("Average: %.2f\n", rs.getDouble("average"));
                                System.out.println("Grade: " + rs.getString("grade"));
                                System.out.println("----------------------");
                            }
                        }
                        break;

                    case 3:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
