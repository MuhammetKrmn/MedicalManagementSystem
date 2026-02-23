package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.time.LocalDate;

public class AppointmentController {
    @FXML private TextField appId;
    @FXML private TextField doctorId;
    @FXML private TextField patientId;
    @FXML private TextField dateField;
    @FXML private TextField timeField;

    private AppointmentCrudOperations crud = new AppointmentCrudOperations();

    @FXML
    void handleSave() {
        try {
            // FXML Bağlantı Kontrolü
            if (appId == null || dateField == null || timeField == null) {
                System.out.println("HATA: FXML bağlantıları kopuk!");
                return;
            }

            // Tarih Kontrolü
            String inputDate = dateField.getText();
            LocalDate selectedDate = LocalDate.parse(inputDate);
            LocalDate today = LocalDate.now();
            if (selectedDate.isBefore(today)) {
                showAlert("Hata", "Geçmiş bir tarihe randevu veremezsiniz!");
                return;
            }

            // Nesne Oluşturma
            Appointment a = new Appointment(
                    Integer.parseInt(appId.getText()),
                    Integer.parseInt(doctorId.getText()),
                    Integer.parseInt(patientId.getText()),
                    dateField.getText(),
                    timeField.getText()
            );

            // Veritabanına Kayıt
            int result = crud.insertAppointment(a);
            if (result > 0) {
                showAlert("Başarılı", "Randevu başarıyla kaydedildi!");
                handleClear(); // Kayıttan sonra ekranı temizle
            } else {
                showAlert("Hata", "Veritabanı kaydı başarısız. ID çakışması veya Foreign Key hatası olabilir.");
            }
        } catch (java.time.format.DateTimeParseException e) {
            showAlert("Hata", "Tarih formatı YYYY-MM-DD olmalı!");
        } catch (NumberFormatException e) {
            showAlert("Hata", "ID alanlarına sayı girmelisin!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Hata", "Bir sorun oluştu: " + e.getMessage());
        }
        // BURADAKİ HATALI SİLME KODUNU KALDIRDIK KRAL!
    }

    @FXML
    void handleDelete() {
        try {
            int id = Integer.parseInt(appId.getText());
            if (crud.deleteAppointment(id) > 0) {
                showAlert("Başarılı", "Randevu silindi!");
                handleClear(); // Silme işleminden sonra kutuları boşaltır
            } else {
                showAlert("Hata", "Randevu bulunamadı veya silinemedi!");
            }
        } catch (Exception e) {
            showAlert("Hata", "Geçersiz ID!");
        }
    }

    @FXML
    void handleClear() {
        appId.clear();
        doctorId.clear();
        patientId.clear();
        dateField.clear();
        timeField.clear();
    }

    @FXML
    void handleGet() {
        try {
            crud.getAppointmentById(Integer.parseInt(appId.getText())).ifPresentOrElse(a -> {
                doctorId.setText(String.valueOf(a.getDoctorId()));
                patientId.setText(String.valueOf(a.getPatientId()));
                dateField.setText(a.getDate());
                timeField.setText(a.getTime());
            }, () -> showAlert("Hata", "Randevu bulunamadı!"));
        } catch (Exception e) { showAlert("Hata", "Geçersiz ID!"); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}