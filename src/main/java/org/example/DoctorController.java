package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.util.Optional;

public class DoctorController {
    // FXML dosyasındaki fx:id değerleri ile aynı olmalı
    @FXML private TextField doctorId;
    @FXML private TextField nameField;
    @FXML private TextField cliniqueField;

    private DoctorCrudOperations crud = new DoctorCrudOperations();

    // GET Butonu için
    @FXML
    void handleGet() {
        try {
            int id = Integer.parseInt(doctorId.getText());
            Optional<Doctor> d = crud.getDoctorById(id);
            if (d.isPresent()) {
                nameField.setText(d.get().getName());
                cliniqueField.setText(d.get().getClinique());
            } else {
                showAlert("Hata", "Doktor bulunamadı!");
            }
        } catch (NumberFormatException e) {
            showAlert("Format Hatası", "Lütfen geçerli bir sayısal ID girin.");
        }
    }

    // SAVE Butonu için
    @FXML
    void handleSave() {
        if (doctorId.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                cliniqueField.getText().trim().isEmpty()) {

            showAlert("Hata", "Lütfen tüm alanları doldurun! (ID, İsim ve Uzmanlık boş bırakılamaz)");
            return;
        }
        try {
            Doctor d = new Doctor(
                    Integer.parseInt(doctorId.getText()),
                    nameField.getText(),
                    cliniqueField.getText()
            );
            int result = crud.insertDoctor(d);
            if (result > 0) {
                showAlert("Başarılı", "Doktor sisteme kaydedildi!");
            } else {
                showAlert("Hata", "Doktor kaydedilemedi (ID çakışması olabilir).");
            }
        } catch (Exception e) {
            showAlert("Hata", "Lütfen tüm alanları doğru doldurduğunuzdan emin olun.");
        }
    }
    // UPDATE Butonu için (Scene Builder'daki #handleUpdate)
    @FXML
    void handleUpdate() {
        // 1. ADIM: Boşluk Kontrolü (Aynı Save'deki gibi)
        if (doctorId.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                cliniqueField.getText().trim().isEmpty()) {

            showAlert("Hata", "Güncelleme yapabilmek için tüm alanları doldurmalısınız!");
            return;
        }

        try {
            // 2. ADIM: Güncellenecek verileri nesneye alıyoruz
            Doctor d = new Doctor(
                    Integer.parseInt(doctorId.getText()),
                    nameField.getText(),
                    cliniqueField.getText()
            );

            // 3. ADIM: CRUD üzerinden güncelleme işlemini çağırıyoruz
            // (Bu metodun DoctorCrudOperations içinde tanımlı olduğunu varsayıyoruz)
            if (crud.updateDoctor(d) > 0) {
                showAlert("Başarılı", "Doktor bilgileri güncellendi!");
            } else {
                showAlert("Hata", "Güncelleme başarısız! Bu ID'ye sahip bir doktor bulunamadı.");
            }
        } catch (NumberFormatException e) {
            showAlert("Hata", "ID kısmına geçerli bir sayı girmelisin!");
        } catch (Exception e) {
            showAlert("Hata", "Güncelleme sırasında bir sorun oluştu: " + e.getMessage());
        }
    }

    // DELETE Butonu için (Scene Builder'daki #handleDelete)
    @FXML
    void handleDelete() {
        try {
            int id = Integer.parseInt(doctorId.getText());
            // Crud sınıfındaki silme metodunu çağırıyoruz
            if (crud.deleteDoctorById(id) > 0) {
                showAlert("Başarılı", "Doktor sistemden silindi!");
                // Silindikten sonra kutuları temizleyelim knk
                doctorId.clear();
                nameField.clear();
                cliniqueField.clear();
            } else {
                showAlert("Hata", "Silme başarısız. Bu ID ile bir doktor bulunamadı.");
            }
        } catch (Exception e) {
            showAlert("Hata", "Geçersiz ID!");
        }
    }
    // Yardımcı mesaj kutusu
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}