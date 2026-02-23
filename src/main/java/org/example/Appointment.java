package org.example;

public class Appointment {
    private int id;
    private int doctorId;
    private int patientId;
    private String date;
    private String time;

    public Appointment(int id, int doctorId, int patientId, String date, String time) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
    }

    // Bu Getter'lar olmazsa CRUD sınıfın hata verir knk
    public int getId() { return id; }
    public int getDoctorId() { return doctorId; }
    public int getPatientId() { return patientId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}