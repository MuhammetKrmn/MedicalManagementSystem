package org.example;
import java.sql.*;
import java.util.Optional;

public class AppointmentCrudOperations {
    public int insertAppointment(Appointment a) {

        String query = "INSERT INTO appointments (id, patient_id, doctor_id, app_date, app_time) VALUES (?, ?, ?, CAST(? AS DATE), CAST(? AS TIME))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, a.getId());
            stmt.setInt(2, a.getPatientId());
            stmt.setInt(3, a.getDoctorId());
            stmt.setString(4, a.getDate());
            stmt.setString(5, a.getTime());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public Optional<Appointment> getAppointmentById(int id) {
        String query = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("patient_id"),
                        rs.getString("app_date"),
                        "Randevu Saati: " + rs.getString("app_time")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    // Randevu Silme
    public int deleteAppointment(int id) {
        String query = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }
}