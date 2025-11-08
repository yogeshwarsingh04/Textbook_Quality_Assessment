package com.textbook.model;

public class Expert {

    // From Experts table
    private int expertId; // Same as user_id
    private String fullName;
    private String specialization;
    private String credentials;
    private String status;

    // From Users table (for display)
    private String username;
    private String email;

    // --- Getters and Setters ---
    
    public int getExpertId() { return expertId; }
    public void setExpertId(int expertId) { this.expertId = expertId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getCredentials() { return credentials; }
    public void setCredentials(String credentials) { this.credentials = credentials; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}