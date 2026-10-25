package com.complaint.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Complaint {
    private int id;
    private String citizenName;
    private String phoneNumber;
    private String area;
    private String category;
    private String description;
    private int severity;
    private String landmark;
    private String status;
    private LocalDateTime createdAt;
    private int duplicateCount;
    private int publicImpact;
    private boolean safetyRisk;
    private String locationType;
    private boolean peakHour;
    private int priorityScore;
    private boolean slaViolated;
    private String assignedTeam;

    // Complete City Areas List (45 Areas)
    public static final String[] AVAILABLE_AREAS = {
            "Anna Nagar", "KK Nagar", "Velachery", "Tambaram", "Guindy",
            "T Nagar", "Mylapore", "Adyar", "Besant Nagar", "Nungambakkam",
            "Egmore", "Triplicane", "George Town", "Royapettah", "Saidapet",
            "Perungudi", "Sholinganallur", "Kodambakkam", "Alandur", "St Thomas Mount",
            "Pallikaranai", "Medavakkam", "Mogappair", "Poonamallee", "Ambattur",
            "Avadi", "Chromepet", "Pallavaram", "Tambaram East", "Tambaram West",
            "Selaiyur", "Chitlapakkam", "Hastinapuram", "Kovilambakkam", "Madipakkam",
            "Kottivakkam", "Palavakkam", "Neelankarai", "Uthandi", "Kovalam",
            "Vandalur", "Guduvanchery", "Urapakkam", "Potheri", "Kattankulathur"
    };

    // Complaint Categories
    public static final String[] CATEGORIES = {
            "Medical Emergency", "Traffic Signal Failure", "Water Leakage",
            "Electrical Issue", "Road Damage", "Garbage Issue",
            "Street Light Issue", "Others"
    };

    // Category Scores
    public static final int[] CATEGORY_SCORES = {
            100, 90, 70, 85, 60, 30, 40, 20
    };

    // Severity Levels
    public static final String[] SEVERITY_NAMES = {
            "Low", "Medium", "High", "Critical"
    };
    public static final int[] SEVERITY_SCORES = {
            10, 20, 30, 40
    };

    // Location Importance
    public static final String[] LOCATION_TYPES = {
            "Residential", "Hospital", "School", "Bus Stand",
            "Railway Station", "Market", "Industrial"
    };
    public static final int[] LOCATION_SCORES = {
            10, 40, 30, 25, 40, 20, 35
    };

    // Public Impact Options
    public static final String[] IMPACT_OPTIONS = {
            "Individual", "Street", "Local Area", "Entire Area"
    };
    public static final int[] IMPACT_SCORES = {
            5, 15, 30, 50
    };

    public Complaint() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
        this.duplicateCount = 1;
        this.priorityScore = 0;
        this.slaViolated = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(int duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    public void incrementDuplicateCount() {
        this.duplicateCount++;
    }

    public int getPublicImpact() {
        return publicImpact;
    }

    public void setPublicImpact(int publicImpact) {
        this.publicImpact = publicImpact;
    }

    public boolean isSafetyRisk() {
        return safetyRisk;
    }

    public void setSafetyRisk(boolean safetyRisk) {
        this.safetyRisk = safetyRisk;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public boolean isPeakHour() {
        return peakHour;
    }

    public void setPeakHour(boolean peakHour) {
        this.peakHour = peakHour;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(int priorityScore) {
        this.priorityScore = priorityScore;
    }

    public boolean isSlaViolated() {
        return slaViolated;
    }

    public void setSlaViolated(boolean slaViolated) {
        this.slaViolated = slaViolated;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    public int getCategoryScore() {
        for (int i = 0; i < CATEGORIES.length; i++) {
            if (CATEGORIES[i].equals(this.category)) {
                return CATEGORY_SCORES[i];
            }
        }
        return 20;
    }

    public int getSeverityScore() {
        if (this.severity >= 0 && this.severity < SEVERITY_SCORES.length) {
            return SEVERITY_SCORES[this.severity];
        }
        return 10;
    }

    public int getLocationScore() {
        for (int i = 0; i < LOCATION_TYPES.length; i++) {
            if (LOCATION_TYPES[i].equals(this.locationType)) {
                return LOCATION_SCORES[i];
            }
        }
        return 10;
    }

    public long getDaysPending() {
        return ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
    }

    public int getAgeBonus() {
        long days = getDaysPending();
        if (days >= 10)
            return 100;
        if (days >= 6)
            return 50;
        if (days >= 3)
            return 20;
        return 0;
    }

    public int getDuplicateBonus() {
        return duplicateCount * 2;
    }

    public int getPublicImpactScore() {
        return publicImpact;
    }

    public int getSafetyRiskScore() {
        return safetyRisk ? 50 : 0;
    }

    public int getPeakHourScore() {
        return peakHour ? 20 : 0;
    }

    public int getSlaViolationScore() {
        return slaViolated ? 100 : 0;
    }

    public static void printAvailableAreas() {
        System.out.println();
        System.out.println("AVAILABLE AREAS (45 Areas):");
        System.out.println("================================================================");
        int count = 0;
        for (String area : AVAILABLE_AREAS) {
            System.out.printf("%-30s", area);
            count++;
            if (count % 2 == 0) {
                System.out.println();
            }
        }
        if (count % 2 != 0) {
            System.out.println();
        }
        System.out.println("================================================================");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+---------------------------------------------------+\n");
        sb.append(String.format("| Complaint ID: %-26d |\n", id));
        sb.append(String.format("| Citizen: %-27s |\n", citizenName));
        sb.append(String.format("| Area: %-29s |\n", area));
        sb.append(String.format("| Category: %-25s |\n", category));
        sb.append(String.format("| Severity: %-26s |\n", SEVERITY_NAMES[severity]));
        sb.append(String.format("| Status: %-27s |\n", status));
        sb.append(String.format("| Priority Score: %-20d |\n", priorityScore));
        sb.append(String.format("| Duplicates: %-23d |\n", duplicateCount));
        sb.append(String.format("| Days Pending: %-21d |\n", getDaysPending()));
        if (assignedTeam != null) {
            sb.append(String.format("| Team: %-28s |\n", assignedTeam));
        }
        sb.append("+---------------------------------------------------+\n");
        return sb.toString();
    }

    public String getPriorityBreakdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+---------------------------------------------------+\n");
        sb.append("|           PRIORITY BREAKDOWN                      |\n");
        sb.append("+---------------------------------------------------+\n");
        sb.append(String.format("| Category Score      : %3d %-20s|\n", getCategoryScore(), ""));
        sb.append(String.format("| Severity Score      : %3d %-20s|\n", getSeverityScore(), ""));
        sb.append(String.format("| Duplicate Bonus     : %3d %-20s|\n", getDuplicateBonus(), ""));
        sb.append(String.format("| Age Bonus           : %3d %-20s|\n", getAgeBonus(), ""));
        sb.append(String.format("| Public Impact       : %3d %-20s|\n", getPublicImpactScore(), ""));
        sb.append(String.format("| Safety Risk         : %3d %-20s|\n", getSafetyRiskScore(), ""));
        sb.append(String.format("| Location Importance : %3d %-20s|\n", getLocationScore(), ""));
        sb.append(String.format("| Peak Hour           : %3d %-20s|\n", getPeakHourScore(), ""));
        sb.append(String.format("| SLA Violation       : %3d %-20s|\n", getSlaViolationScore(), ""));
        sb.append("+---------------------------------------------------+\n");
        sb.append(String.format("| TOTAL PRIORITY      : %3d %-20s|\n", priorityScore, ""));
        sb.append("+---------------------------------------------------+\n");
        return sb.toString();
    }
}