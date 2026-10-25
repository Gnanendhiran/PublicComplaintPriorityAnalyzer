package com.complaint.ui;

import com.complaint.model.Complaint;
import com.complaint.service.ComplaintService;
import com.complaint.dsa.Graph;

import java.util.*;

public class AdminMenu {
    private Scanner scanner;
    private ComplaintService service;

    public AdminMenu() {
        this.scanner = new Scanner(System.in);
        this.service = ComplaintService.getInstance();
    }

    public void showMenu() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("              ADMIN / OFFICER DASHBOARD");
        System.out.println("========================================================");

        service.checkAndUpdateSLA();

        while (true) {
            System.out.println();
            System.out.println("+---------------------------------------------------+");
            System.out.println("| 1.  View Highest Priority Complaint (Heap)        |");
            System.out.println("| 2.  View Top 5 Critical Complaints (Heap)         |");
            System.out.println("| 3.  Search Complaint (HashMap - O(1))             |");
            System.out.println("| 4.  View All Complaints                          |");
            System.out.println("| 5.  Sort Complaints (Merge Sort - O(n log n))    |");
            System.out.println("| 6.  Find Nearest Team (BFS - O(V+E))             |");
            System.out.println("| 7.  Find Fastest Route (Dijkstra - O((V+E)logV)) |");
            System.out.println("| 8.  View Area Statistics (HashMap)                |");
            System.out.println("| 9.  Update Complaint Status                      |");
            System.out.println("| 10. Exit                                         |");
            System.out.println("+---------------------------------------------------+");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewHighestPriority();
                    break;
                case 2:
                    viewTopK(5);
                    break;
                case 3:
                    searchComplaint();
                    break;
                case 4:
                    viewAllComplaints();
                    break;
                case 5:
                    sortComplaints();
                    break;
                case 6:
                    findNearestTeam();
                    break;
                case 7:
                    findFastestRoute();
                    break;
                case 8:
                    viewAreaStatistics();
                    break;
                case 9:
                    updateStatus();
                    break;
                case 10:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void viewHighestPriority() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("           HIGHEST PRIORITY COMPLAINT (Heap)");
        System.out.println("           Peek: O(1) | Insert: O(log n)");
        System.out.println("========================================================");

        Complaint complaint = service.getHighestPriority();
        if (complaint != null) {
            System.out.println(complaint);
            System.out.println(complaint.getPriorityBreakdown());
            System.out.println();
            System.out.println("Heap Size: " + service.getAllComplaints().size());
        } else {
            System.out.println("No complaints found.");
        }
    }

    private void viewTopK(int k) {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("           TOP " + k + " CRITICAL COMPLAINTS (Heap)");
        System.out.println("           O(k log n) for Top-K extraction");
        System.out.println("========================================================");

        List<Complaint> topK = service.getTopKComplaints(k);
        if (topK.isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }

        System.out.println();
        System.out.println("Top " + k + " Critical Complaints:");
        for (int i = 0; i < topK.size(); i++) {
            Complaint c = topK.get(i);
            System.out.printf("\n#%d. ID: %d | Priority: %d\n", i + 1, c.getId(), c.getPriorityScore());
            System.out.printf("    Area: %s | Category: %s\n", c.getArea(), c.getCategory());
            System.out.printf("    Status: %s | Duplicates: %d | Days: %d\n",
                    c.getStatus(), c.getDuplicateCount(), c.getDaysPending());
        }
    }

    private void searchComplaint() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("           SEARCH COMPLAINT (HashMap - O(1))");
        System.out.println("========================================================");

        System.out.print("Enter Complaint ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Complaint complaint = service.searchComplaint(id);
        if (complaint != null) {
            System.out.println(complaint);
            System.out.println(complaint.getPriorityBreakdown());
        } else {
            System.out.println("Complaint not found.");
        }
    }

    private void viewAllComplaints() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("              ALL COMPLAINTS");
        System.out.println("========================================================");

        List<Complaint> complaints = service.getAllComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }

        System.out.printf("\nTotal: %d | Resolved: %d | Pending: %d\n",
                service.getTotalComplaints(), service.getResolvedCount(), service.getPendingCount());
        System.out.printf("Resolution Rate: %.1f%%\n",
                (service.getResolvedCount() * 100.0 / complaints.size()));

        System.out.println();
        System.out.println("Recent Complaints (Last 10):");
        for (Complaint c : complaints.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(10)
                .toList()) {
            System.out.printf("  ID: %d | %s | Priority: %d | %s\n",
                    c.getId(), c.getArea(), c.getPriorityScore(), c.getStatus());
        }
    }

    private void sortComplaints() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("           SORT COMPLAINTS (Merge Sort - O(n log n))");
        System.out.println("           Using Collections.sort() - TimSort");
        System.out.println("========================================================");

        System.out.println();
        System.out.println("Sort by:");
        System.out.println("1. Priority (Highest to Lowest)");
        System.out.println("2. Date (Newest to Oldest)");
        System.out.println("3. Area (A-Z)");
        System.out.println("4. Status (PENDING -> IN_PROGRESS -> RESOLVED)");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        String sortBy = "";
        switch (choice) {
            case 1:
                sortBy = "priority";
                break;
            case 2:
                sortBy = "date";
                break;
            case 3:
                sortBy = "area";
                break;
            case 4:
                sortBy = "status";
                break;
            default:
                sortBy = "priority";
        }

        List<Complaint> sorted = service.sortComplaints(sortBy);
        System.out.println();
        System.out.println("Sorted Complaints:");
        for (Complaint c : sorted) {
            System.out.printf("  ID: %d | %s | Priority: %d | %s\n",
                    c.getId(), c.getArea(), c.getPriorityScore(), c.getStatus());
        }
    }

    @SuppressWarnings("unchecked")
    private void findNearestTeam() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("           FIND NEAREST TEAM (BFS - O(V+E))");
        System.out.println("           Shortest path in terms of hops");
        System.out.println("========================================================");

        service.getCityGraph().printGraphVisualization();
        Complaint.printAvailableAreas();

        System.out.print("Enter Complaint Area: ");
        String area = scanner.nextLine();

        Map<String, Object> result = service.findNearestTeam(area);
        Object teamObj = result.get("team");
        Map<String, Object> bfsResult = (Map<String, Object>) result.get("bfsResult");
        int minHops = (int) result.get("minHops");

        if (teamObj == null || bfsResult == null) {
            System.out.println("No team found for this area.");
            return;
        }

        ComplaintService.TeamInfo team = (ComplaintService.TeamInfo) teamObj;
        List<String> path = (List<String>) bfsResult.get("path");
        List<String> visited = (List<String>) bfsResult.get("visited");

        System.out.println();
        System.out.println("========================================================");
        System.out.println("              BFS SEARCH RESULT");
        System.out.println("========================================================");

        System.out.println();
        System.out.println("BFS Exploration Order:");
        for (int i = 0; i < visited.size(); i++) {
            System.out.printf("  %d. %s\n", i + 1, visited.get(i));
        }

        System.out.println();
        System.out.println("Nearest Team Found!");
        System.out.printf("  Team: %s\n", team.name);
        System.out.printf("  Starting Area: %s\n", team.area);
        System.out.printf("  Target Area: %s\n", area);
        System.out.printf("  Shortest Path: %s\n", String.join(" -> ", path));
        System.out.printf("  Total Hops: %d\n", minHops);
        System.out.println("  Algorithm: BFS - O(V+E)");
    }

    @SuppressWarnings("unchecked")
    private void findFastestRoute() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("        FIND FASTEST ROUTE (Dijkstra - O((V+E)logV))");
        System.out.println("        Shortest path with weighted edges");
        System.out.println("========================================================");

        service.getCityGraph().printGraphVisualization();
        Complaint.printAvailableAreas();

        System.out.print("Enter Starting Area (Team Location): ");
        String start = scanner.nextLine();
        System.out.print("Enter Target Area (Complaint Location): ");
        String target = scanner.nextLine();

        Map<String, Object> result = service.findFastestRoute(start, target);

        if (result.isEmpty() || (int) result.get("distance") == -1) {
            System.out.println("No route found between these areas.");
            return;
        }

        List<String> path = (List<String>) result.get("path");
        int distance = (int) result.get("distance");
        List<String> visited = (List<String>) result.get("visited");
        Map<String, Integer> distances = (Map<String, Integer>) result.get("distances");

        System.out.println();
        System.out.println("========================================================");
        System.out.println("           DIJKSTRA ALGORITHM RESULT");
        System.out.println("========================================================");

        System.out.println();
        System.out.println("Dijkstra Exploration Order:");
        for (int i = 0; i < visited.size(); i++) {
            System.out.printf("  %d. %s\n", i + 1, visited.get(i));
        }

        System.out.println();
        System.out.println("Shortest Distances from " + start + ":");
        for (Map.Entry<String, Integer> entry : distances.entrySet()) {
            String node = entry.getKey();
            Integer dist = entry.getValue();
            if (dist < Integer.MAX_VALUE) {
                System.out.printf("  %-20s : %d mins\n", node, dist);
            }
        }

        System.out.println();
        System.out.println("Fastest Route Found!");
        System.out.printf("  Path: %s\n", String.join(" -> ", path));
        System.out.printf("  Total Travel Time: %d minutes\n", distance);
        System.out.println("  Algorithm: Dijkstra - O((V+E)logV)");
    }

    private void viewAreaStatistics() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("        AREA STATISTICS (HashMap - O(1))");
        System.out.println("========================================================");

        Map<String, Integer> stats = service.getAreaStatistics();
        if (stats.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        System.out.println();
        System.out.println("Area Wise Complaint Count:");
        stats.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    System.out.printf("  %-25s : %3d complaints\n", entry.getKey(), entry.getValue());
                });
    }

    private void updateStatus() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("           UPDATE COMPLAINT STATUS");
        System.out.println("========================================================");

        System.out.print("Enter Complaint ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Complaint complaint = service.searchComplaint(id);
        if (complaint == null) {
            System.out.println("Complaint not found.");
            return;
        }

        System.out.println();
        System.out.println("Current Complaint:");
        System.out.println(complaint);

        System.out.println();
        System.out.println("Select New Status:");
        System.out.println("1. IN_PROGRESS");
        System.out.println("2. RESOLVED");
        System.out.print("Choose: ");
        int statusChoice = scanner.nextInt();
        scanner.nextLine();

        String newStatus = statusChoice == 1 ? "IN_PROGRESS" : "RESOLVED";

        if (service.updateStatus(id, newStatus)) {
            System.out.println();
            System.out.println("Complaint status updated successfully!");
            System.out.println("New Status: " + newStatus);
            System.out.println("Updated Priority: " + complaint.getPriorityScore());
            System.out.println(complaint.getPriorityBreakdown());
        } else {
            System.out.println("Failed to update status.");
        }
    }
}