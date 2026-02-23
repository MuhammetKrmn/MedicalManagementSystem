package org.example;
import java.sql.*;
import java.util.Optional;

public class AppointmentCrudOperations {
    public int insertAppointment(Appointment a) {
        // Sütun isimlerini veritabanındakilerle (app_date, app_time) birebir eşledik
        String query = "INSERT INTO appointments (id, patient_id, doctor_id, app_date, app_time) VALUES (?, ?, ?, CAST(? AS DATE), CAST(? AS TIME))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, a.getId());
            stmt.setInt(2, a.getPatientId());
            stmt.setInt(3, a.getDoctorId());
            stmt.setString(4, a.getDate()); // app_date sütunu için
            stmt.setString(5, a.getTime()); // app_time sütunu için (geçici olarak sabit verdik)

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
                        rs.getInt("doctor_id"), // Burası veritabanıyla aynı
                        rs.getInt("patient_id"), // Burası veritabanıyla aynı
                        rs.getString("app_date"), // "appointment_date" -> "app_date" oldu
                        "Randevu Saati: " + rs.getString("app_time") // "description" yerine app_time bilgisini alıyoruz
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