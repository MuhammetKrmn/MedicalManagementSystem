package org.example;

import java.sql.*;
import java.util.Optional;

public class PatientCrudOperations {

    // 1. Hasta Getirme (Fetch) [cite: 10, 22]
    public Optional<Patient> getPatientById(int id) {
        Patient patient = null;
        String query = "SELECT * FROM patients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("telephone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(patient);
    }

    // 2. Hasta Kaydetme (Save) [cite: 13, 22]
    public int insertPatient(Patient p) {
        String query = "INSERT INTO patients (id, name, address, telephone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getName());
            stmt.setString(3, p.getAddress());
            stmt.setString(4, p.getPhone());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int deletePatientById(int id) {
        String query = "DELETE FROM patients WHERE id = ?";
        // getConnection()'ın null dönmediğinden emin olmalısın knk
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return -1; // Bağlantı yoksa hata dön

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Silinen satır sayısı: " + rowsAffected); // Konsoldan takip et
            return rowsAffected;
        } catch (SQLException e) {
            System.err.println("SQL Silme Hatası: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    // 4. Hasta Güncelleme (Update)
    public int updatePatient(Patient p) {
        // Veritabanındaki tablo yapına göre sütun isimlerini (name, address, telephone) kontrol et knk
        String query = "UPDATE patients SET name = ?, address = ?, telephone = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getAddress());
            stmt.setString(3, p.getPhone());
            stmt.setInt(4, p.getId());

            return stmt.executeUpdate(); // Başarılıysa 1 döner
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}