package com.complaint.model;

import java.util.ArrayList;
import java.util.List;

public class Citizen {
    private String name;
    private String phoneNumber;
    private List<Complaint> complaints;

    public Citizen(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.complaints = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }
}