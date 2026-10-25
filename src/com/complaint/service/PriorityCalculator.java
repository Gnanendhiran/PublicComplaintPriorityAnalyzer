package com.complaint.service;

import com.complaint.model.Complaint;

/**
 * Priority Calculator for Complaints
 * Computes a weighted priority score using multiple DSA-aware factors.
 * Formula: Category + Severity + Duplicates + Age + PublicImpact + Safety + Location + PeakHour + SLA
 */
public class PriorityCalculator {

    /**
     * Calculates and sets the priority score on the given complaint.
     * All individual score methods are defined in Complaint itself;
     * this class aggregates them.
     */
    public static int calculate(Complaint complaint) {
        int score = 0;

        score += complaint.getCategoryScore();       // Category urgency (20–100)
        score += complaint.getSeverityScore();        // Severity level (10–40)
        score += complaint.getDuplicateBonus();       // +2 per duplicate report
        score += complaint.getAgeBonus();             // Age escalation (0/20/50/100)
        score += complaint.getPublicImpactScore();    // People affected (5/15/30/50)
        score += complaint.getSafetyRiskScore();      // Safety hazard (+50)
        score += complaint.getLocationScore();        // Location importance (10–40)
        score += complaint.getPeakHourScore();        // Peak hour (+20)
        score += complaint.getSlaViolationScore();    // SLA breach (+100)

        complaint.setPriorityScore(score);
        return score;
    }
}