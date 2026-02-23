package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.util.Optional;

public class PatientController {
    @FXML private TextField patientId;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    private PatientCrudOperations crud = new PatientCrudOperations();

    @FXML
    void handleSave() {
        try {
            // 1. Alanları al ve boşlukları temizle
            String pId = patientId.getText().trim();
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            // 2. Kısıtlama Kontrolleri (Constraints)
            if (pId.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                showAlert("Hata", "Lütfen tüm alanları doldurun!");
                return;
            }

            if (!name.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ\\s]+$")) {
                showAlert("Hata", "İsim sadece harflerden oluşmalıdır!");
                return;
            }

            if (!phone.matches("^[0-9]+$")) {
                showAlert("Hata", "Telefon numarası sadece rakamlardan oluşmalıdır!");
                return;
            }

            // 3. Nesne Oluşturma ve Kayıt
            Patient p = new Patient(Integer.parseInt(pId), name, address, phone);

            int result = crud.insertPatient(p);
            if (result > 0) {
                showAlert("Başarılı", "Hasta başarıyla kaydedildi!");
                handleClear(); // Sadece kayıt başarılıysa ekranı temizler
            } else {
                showAlert("Hata", "Veritabanı kaydı başarısız. Bu ID zaten kullanımda olabilir.");
            }

        } catch (NumberFormatException e) {
            showAlert("Hata", "ID kısmına sadece sayı girmelisin!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Hata", "Bir sorun oluştu: " + e.getMessage());
        }
        // DİKKAT: Metodun sonunda başka hiçbir kod (delete vs.) olmamalı!
    }
    @FXML
    void handleGet() {
        try {
            Optional<Patient> p = crud.getPatientById(Integer.parseInt(patientId.getText()));
            if (p.isPresent()) {
                nameField.setText(p.get().getName());
                addressField.setText(p.get().getAddress());
                phoneField.setText(p.get().getPhone());

                // ID alanını yazılamaz yapıyoruz ki güncelleme yaparken değişmesin
                patientId.setEditable(false);
                patientId.setStyle("-fx-background-color: #e0e0e0;"); // Görsel geri bildirim
            } else {
                showAlert("Hata", "Hasta bulunamadı!");
            }
        } catch (Exception e) {
            showAlert("Hata", "Geçersiz ID!");
        }
    }

    @FXML
    void handleUpdate() {
        try {
            // Kutulardaki mevcut verileri alıyoruz
            int id = Integer.parseInt(patientId.getText()); // Mevcut ID (Değiştirilmemeli!)
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            // Kısıtlama: Boş alan kalmasın
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                showAlert("Hata", "Güncelleme için ID dışındaki tüm alanlar dolu olmalıdır!");
                return;
            }

            // Kısıtlama: İsimde sayı olmasın
            if (!name.matches("^[a-zA-ZğüşıöçĞÜŞİÖÇ\\s]+$")) {
                showAlert("Hata", "İsim kısmında sayı kullanamazsınız!");
                return;
            }

            Patient p = new Patient(id, name, address, phone);

            if (crud.updatePatient(p) > 0) {
                showAlert("Başarılı", "Hasta bilgileri güncellendi!");
            } else {
                showAlert("Hata", "Veritabanında " + id + " numaralı bir hasta bulunamadı!");
            }
        } catch (NumberFormatException e) {
            showAlert("Hata", "Lütfen geçerli bir ID girin!");
        } catch (Exception e) {
            showAlert("Hata", "Bir sorun oluştu: " + e.getMessage());
        }
    }
    @FXML
    void handleDelete() {
        try {
            String idText = patientId.getText().trim();
            if (idText.isEmpty()) {
                showAlert("Hata", "Silmek için bir ID girmeniz gerekiyor!");
                return;
            }

            int id = Integer.parseInt(idText);

            if (crud.deletePatientById(id) > 0) {
                showAlert("Başarılı", "Hasta başarıyla silindi!");

                // Burası kritik: Silme bitince kutuyu eski haline döndürür
                handleClear();

            } else {
                showAlert("Hata", "Bu ID'ye sahip bir hasta bulunamadı!");
            }
        } catch (Exception e) {
            showAlert("Hata", "Silme işlemi sırasında bir hata oluştu!");
        }
    }
    @FXML
    void handleClear() {
        patientId.clear();
        patientId.setEditable(true); // Tekrar yazılabilir yap
        patientId.setStyle(null);    // Rengi sıfırla
        nameField.clear();
        addressField.clear();
        phoneField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}