package org.example;

public class Doctor {
    private int id;
    private String name;
    private String clinique;

    public Doctor() {}

    public Doctor(int id, String name, String clinique) {
        this.id = id;
        this.name = name;
        this.clinique = clinique;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClinique() { return clinique; }
    public void setClinique(String clinique) { this.clinique = clinique; }
}