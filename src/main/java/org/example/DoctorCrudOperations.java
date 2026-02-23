package org.example;

import java.sql.*;
import java.util.Optional;

public class DoctorCrudOperations {


    public Optional<Doctor> getDoctorById(int id) {
        Doctor doctor = null;
        String query = "SELECT * FROM doctors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("clinique"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.ofNullable(doctor);
    }


    public int insertDoctor(Doctor d) {
        String query = "INSERT INTO doctors (id, name, clinique) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, d.getId());
            stmt.setString(2, d.getName());
            stmt.setString(3, d.getClinique());
            return stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); return -1; }
    }



    public int updateDoctor(Doctor d) {
        String query = "UPDATE doctors SET name = ?, clinique = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, d.getName());
            stmt.setString(2, d.getClinique());
            stmt.setInt(3, d.getId());

            return stmt.executeUpdate(); // Başarılıysa 1 döner
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public int deleteDoctorById(int id) {
        String query = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate(); // Başarılıysa 1 döner
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}