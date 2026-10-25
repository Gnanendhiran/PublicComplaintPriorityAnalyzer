package com.complaint.ui;

import com.complaint.model.Complaint;
import com.complaint.model.Citizen;
import com.complaint.service.ComplaintService;

import java.util.Scanner;

public class PublicMenu {
    private Scanner scanner;
    private ComplaintService service;
    private Citizen currentCitizen;

    public PublicMenu() {
        this.scanner = new Scanner(System.in);
        this.service = ComplaintService.getInstance();
    }

    public void showMenu() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("              PUBLIC COMPLAINT PORTAL");
        System.out.println("========================================================");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        currentCitizen = new Citizen(name, phone);

        while (true) {
            System.out.println();
            System.out.println("+---------------------------------------------------+");
            System.out.println("| 1. Submit Complaint                              |");
            System.out.println("| 2. View My Complaints                            |");
            System.out.println("| 3. Back to Main                                  |");
            System.out.println("+---------------------------------------------------+");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    submitComplaint();
                    break;
                case 2:
                    viewMyComplaints();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void submitComplaint() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("              SUBMIT NEW COMPLAINT");
        System.out.println("========================================================");

        Complaint.printAvailableAreas();

        System.out.print("Enter Area: ");
        String area = scanner.nextLine();

        System.out.println();
        System.out.println("Complaint Categories:");
        for (int i = 0; i < Complaint.CATEGORIES.length; i++) {
            System.out.printf("  %d. %-20s (Score: %d)\n",
                    i + 1, Complaint.CATEGORIES[i], Complaint.CATEGORY_SCORES[i]);
        }
        System.out.print("Choose category (1-" + Complaint.CATEGORIES.length + "): ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();
        String category = Complaint.CATEGORIES[categoryChoice - 1];

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.println();
        System.out.println("Severity Levels:");
        for (int i = 0; i < Complaint.SEVERITY_NAMES.length; i++) {
            System.out.printf("  %d. %-8s (Score: %d)\n",
                    i + 1, Complaint.SEVERITY_NAMES[i], Complaint.SEVERITY_SCORES[i]);
        }
        System.out.print("Choose severity (1-" + Complaint.SEVERITY_NAMES.length + "): ");
        int severityChoice = scanner.nextInt();
        scanner.nextLine();
        int severity = severityChoice - 1;

        System.out.print("Location Landmark: ");
        String landmark = scanner.nextLine();

        System.out.println();
        System.out.println("Additional Factors:");
        System.out.println("How many people are affected?");
        for (int i = 0; i < Complaint.IMPACT_OPTIONS.length; i++) {
            System.out.printf("  %d. %-15s (Score: %d)\n",
                    i + 1, Complaint.IMPACT_OPTIONS[i], Complaint.IMPACT_SCORES[i]);
        }
        System.out.print("Choose (1-" + Complaint.IMPACT_OPTIONS.length + "): ");
        int impactChoice = scanner.nextInt();
        scanner.nextLine();
        int publicImpact = Complaint.IMPACT_SCORES[impactChoice - 1];

        System.out.print("Does this create a safety hazard? (Y/N): ");
        boolean safetyRisk = scanner.nextLine().equalsIgnoreCase("Y");

        System.out.println();
        System.out.println("Location Type:");
        for (int i = 0; i < Complaint.LOCATION_TYPES.length; i++) {
            System.out.printf("  %d. %-18s (Score: %d)\n",
                    i + 1, Complaint.LOCATION_TYPES[i], Complaint.LOCATION_SCORES[i]);
        }
        System.out.print("Choose (1-" + Complaint.LOCATION_TYPES.length + "): ");
        int locationChoice = scanner.nextInt();
        scanner.nextLine();
        String locationType = Complaint.LOCATION_TYPES[locationChoice - 1];

        System.out.print("Is this during peak hour? (Y/N): ");
        boolean peakHour = scanner.nextLine().equalsIgnoreCase("Y");

        Complaint complaint = service.submitComplaint(
                currentCitizen, area, category, description, severity,
                landmark, publicImpact, safetyRisk, locationType, peakHour);

        if (complaint != null) {
            System.out.println();
            System.out.println("Complaint submitted successfully!");
            System.out.println("Complaint ID: " + complaint.getId());
            System.out.println("Priority Score: " + complaint.getPriorityScore());
            System.out.println(complaint.getPriorityBreakdown());
        }
    }

    private void viewMyComplaints() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("              MY COMPLAINTS");
        System.out.println("========================================================");

        boolean found = false;
        for (Complaint complaint : service.getAllComplaints()) {
            if (complaint.getPhoneNumber().equals(currentCitizen.getPhoneNumber())) {
                System.out.println(complaint);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No complaints found.");
        }
    }
}