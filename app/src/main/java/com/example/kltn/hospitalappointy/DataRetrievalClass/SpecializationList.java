package com.example.kltn.hospitalappointy.DataRetrievalClass;

public class SpecializationList {
    private String Name;
    private String DoctorID;

    public SpecializationList() {
        super();
    }

    public SpecializationList(String name, String doctorID){
        this.Name=name;
        this.DoctorID=doctorID;
    }

    public String getName() {
        return Name;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }
}
