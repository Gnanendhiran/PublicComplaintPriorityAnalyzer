package com.complaint.service;

import com.complaint.dsa.Graph;
import com.complaint.dsa.MaxHeap;
import com.complaint.model.Citizen;
import com.complaint.model.Complaint;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * ComplaintService - Core Business Logic (Singleton)
 *
 * DSA Structures Used:
 *   - MaxHeap        : Priority Queue for highest-priority complaint (O(1) peek, O(log n) insert)
 *   - HashMap        : O(1) lookup by complaint ID and area statistics
 *   - HashSet        : Duplicate detection by area+category key
 *   - Graph (BFS)    : Nearest team search in O(V+E)
 *   - Graph (Dijkstra): Fastest route in O((V+E) log V)
 *   - Merge Sort     : Complaint sorting via Collections.sort() – TimSort O(n log n)
 */
public class ComplaintService {

    // ─── Singleton ────────────────────────────────────────────────────────────
    private static ComplaintService instance;

    public static ComplaintService getInstance() {
        if (instance == null) {
            instance = new ComplaintService();
        }
        return instance;
    }

    // ─── DSA Data Structures ──────────────────────────────────────────────────
    private final MaxHeap priorityHeap;                    // Max-Heap for priority queue
    private final Map<Integer, Complaint> complaintMap;    // HashMap: ID → Complaint  O(1)
    private final Map<String, Integer> areaStats;          // HashMap: Area → Count     O(1)
    private final Set<String> duplicateSet;                // HashSet: duplicate detection
    private final Graph cityGraph;                         // Graph for BFS / Dijkstra
    private final List<TeamInfo> teams;                    // Response teams
    private final AtomicInteger idCounter;

    // ─── SLA thresholds (hours) ───────────────────────────────────────────────
    private static final Map<String, Integer> SLA_HOURS = new HashMap<>();
    static {
        SLA_HOURS.put("Medical Emergency",     2);
        SLA_HOURS.put("Traffic Signal Failure", 4);
        SLA_HOURS.put("Electrical Issue",       6);
        SLA_HOURS.put("Water Leakage",          8);
        SLA_HOURS.put("Road Damage",           24);
        SLA_HOURS.put("Street Light Issue",    48);
        SLA_HOURS.put("Garbage Issue",         72);
        SLA_HOURS.put("Others",                96);
    }

    // ─── Constructor ──────────────────────────────────────────────────────────
    private ComplaintService() {
        this.priorityHeap  = new MaxHeap();
        this.complaintMap  = new HashMap<>();
        this.areaStats     = new HashMap<>();
        this.duplicateSet  = new HashSet<>();
        this.cityGraph     = new Graph();
        this.teams         = new ArrayList<>();
        this.idCounter     = new AtomicInteger(1001);

        initializeCityGraph();
        initializeTeams();
    }

    // ─── City Graph Setup ─────────────────────────────────────────────────────
    private void initializeCityGraph() {
        // Weighted edges: travel time in minutes between areas
        cityGraph.addEdge("Anna Nagar",       "KK Nagar",         10);
        cityGraph.addEdge("Anna Nagar",       "Mogappair",        15);
        cityGraph.addEdge("Anna Nagar",       "Nungambakkam",     12);
        cityGraph.addEdge("KK Nagar",         "Kodambakkam",      8);
        cityGraph.addEdge("KK Nagar",         "Saidapet",         14);
        cityGraph.addEdge("Nungambakkam",     "Egmore",           10);
        cityGraph.addEdge("Nungambakkam",     "T Nagar",          12);
        cityGraph.addEdge("T Nagar",          "Saidapet",         10);
        cityGraph.addEdge("T Nagar",          "Mylapore",         15);
        cityGraph.addEdge("T Nagar",          "Kodambakkam",      10);
        cityGraph.addEdge("Egmore",           "George Town",      10);
        cityGraph.addEdge("Egmore",           "Triplicane",       12);
        cityGraph.addEdge("George Town",      "Royapettah",       8);
        cityGraph.addEdge("Mylapore",         "Adyar",            10);
        cityGraph.addEdge("Mylapore",         "Royapettah",       8);
        cityGraph.addEdge("Adyar",            "Besant Nagar",     8);
        cityGraph.addEdge("Adyar",            "Velachery",        15);
        cityGraph.addEdge("Adyar",            "Perungudi",        12);
        cityGraph.addEdge("Velachery",        "Pallikaranai",     10);
        cityGraph.addEdge("Velachery",        "Guindy",           15);
        cityGraph.addEdge("Guindy",           "Saidapet",         8);
        cityGraph.addEdge("Guindy",           "Alandur",          10);
        cityGraph.addEdge("Guindy",           "St Thomas Mount",  8);
        cityGraph.addEdge("Alandur",          "Tambaram",         20);
        cityGraph.addEdge("St Thomas Mount",  "Chromepet",        12);
        cityGraph.addEdge("Chromepet",        "Pallavaram",       8);
        cityGraph.addEdge("Pallavaram",       "Tambaram",         10);
        cityGraph.addEdge("Tambaram",         "Tambaram East",    5);
        cityGraph.addEdge("Tambaram",         "Tambaram West",    5);
        cityGraph.addEdge("Tambaram",         "Selaiyur",         10);
        cityGraph.addEdge("Selaiyur",         "Chitlapakkam",     8);
        cityGraph.addEdge("Chitlapakkam",     "Hastinapuram",     8);
        cityGraph.addEdge("Hastinapuram",     "Kovilambakkam",    6);
        cityGraph.addEdge("Kovilambakkam",    "Madipakkam",       8);
        cityGraph.addEdge("Perungudi",        "Sholinganallur",   12);
        cityGraph.addEdge("Sholinganallur",   "Pallikaranai",     8);
        cityGraph.addEdge("Pallikaranai",     "Medavakkam",       10);
        cityGraph.addEdge("Medavakkam",       "Madipakkam",       8);
        cityGraph.addEdge("Besant Nagar",     "Kottivakkam",      10);
        cityGraph.addEdge("Kottivakkam",      "Palavakkam",       5);
        cityGraph.addEdge("Palavakkam",       "Neelankarai",      6);
        cityGraph.addEdge("Neelankarai",      "Uthandi",          8);
        cityGraph.addEdge("Uthandi",          "Kovalam",          10);
        cityGraph.addEdge("Mogappair",        "Ambattur",         15);
        cityGraph.addEdge("Ambattur",         "Avadi",            20);
        cityGraph.addEdge("Avadi",            "Poonamallee",      15);
        cityGraph.addEdge("Tambaram",         "Vandalur",         15);
        cityGraph.addEdge("Vandalur",         "Guduvanchery",     10);
        cityGraph.addEdge("Guduvanchery",     "Urapakkam",        8);
        cityGraph.addEdge("Urapakkam",        "Potheri",          6);
        cityGraph.addEdge("Potheri",          "Kattankulathur",   5);
    }

    // ─── Teams Setup ──────────────────────────────────────────────────────────
    private void initializeTeams() {
        teams.add(new TeamInfo("North Zone Team",    "Anna Nagar"));
        teams.add(new TeamInfo("Central Zone Team",  "Egmore"));
        teams.add(new TeamInfo("South Zone Team",    "Adyar"));
        teams.add(new TeamInfo("West Zone Team",     "Guindy"));
        teams.add(new TeamInfo("East Zone Team",     "Mylapore"));
        teams.add(new TeamInfo("Suburban Team",      "Tambaram"));
        teams.add(new TeamInfo("Far North Team",     "Ambattur"));
        teams.add(new TeamInfo("Coastal Team",       "Besant Nagar"));
    }

    // ─── Submit Complaint ─────────────────────────────────────────────────────
    /**
     * Submits a new complaint, detects duplicates via HashSet, calculates priority,
     * inserts into MaxHeap and HashMap.
     */
    public Complaint submitComplaint(Citizen citizen, String area, String category,
                                     String description, int severity, String landmark,
                                     int publicImpact, boolean safetyRisk,
                                     String locationType, boolean peakHour) {
        // Duplicate detection using HashSet O(1)
        String dupKey = area.toLowerCase() + "|" + category.toLowerCase();
        boolean isDuplicate = duplicateSet.contains(dupKey);
        duplicateSet.add(dupKey);

        // If duplicate exists, increment existing complaint's counter
        if (isDuplicate) {
            for (Complaint existing : complaintMap.values()) {
                if (existing.getArea().equalsIgnoreCase(area)
                        && existing.getCategory().equalsIgnoreCase(category)
                        && existing.getStatus().equals("PENDING")) {
                    existing.incrementDuplicateCount();
                    PriorityCalculator.calculate(existing);
                    priorityHeap.updatePriority(existing);
                    System.out.println("\n[INFO] Similar complaint already exists (ID: "
                            + existing.getId() + "). Duplicate count updated to "
                            + existing.getDuplicateCount() + ".");
                    return existing;
                }
            }
        }

        // Create new complaint
        Complaint complaint = new Complaint();
        complaint.setId(idCounter.getAndIncrement());
        complaint.setCitizenName(citizen.getName());
        complaint.setPhoneNumber(citizen.getPhoneNumber());
        complaint.setArea(area);
        complaint.setCategory(category);
        complaint.setDescription(description);
        complaint.setSeverity(severity);
        complaint.setLandmark(landmark);
        complaint.setPublicImpact(publicImpact);
        complaint.setSafetyRisk(safetyRisk);
        complaint.setLocationType(locationType);
        complaint.setPeakHour(peakHour);

        // Calculate priority score
        PriorityCalculator.calculate(complaint);

        // Insert into DSA structures
        complaintMap.put(complaint.getId(), complaint);     // HashMap O(1)
        priorityHeap.insert(complaint);                     // MaxHeap O(log n)

        // Update area statistics HashMap
        areaStats.merge(area, 1, Integer::sum);

        citizen.addComplaint(complaint);
        return complaint;
    }

    // ─── Heap Operations ──────────────────────────────────────────────────────

    /** Returns highest priority complaint without removing it. O(1) */
    public Complaint getHighestPriority() {
        return priorityHeap.peek();
    }

    /** Returns top-K complaints. O(k log n) */
    public List<Complaint> getTopKComplaints(int k) {
        return priorityHeap.getTopK(k);
    }

    // ─── HashMap Operations ───────────────────────────────────────────────────

    /** Searches complaint by ID using HashMap. O(1) */
    public Complaint searchComplaint(int id) {
        return complaintMap.get(id);
    }

    /** Returns all complaints as a list. */
    public List<Complaint> getAllComplaints() {
        return new ArrayList<>(complaintMap.values());
    }

    /** Returns area → complaint count map. */
    public Map<String, Integer> getAreaStatistics() {
        return new HashMap<>(areaStats);
    }

    // ─── Sorting (Merge Sort / TimSort) ───────────────────────────────────────

    /** Sorts complaints using Collections.sort() (TimSort – O(n log n)). */
    public List<Complaint> sortComplaints(String sortBy) {
        List<Complaint> list = new ArrayList<>(complaintMap.values());
        switch (sortBy.toLowerCase()) {
            case "priority":
                list.sort((a, b) -> b.getPriorityScore() - a.getPriorityScore());
                break;
            case "date":
                list.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
                break;
            case "area":
                list.sort(Comparator.comparing(Complaint::getArea));
                break;
            case "status":
                list.sort(Comparator.comparing(c -> statusOrder(c.getStatus())));
                break;
            default:
                list.sort((a, b) -> b.getPriorityScore() - a.getPriorityScore());
        }
        return list;
    }

    private int statusOrder(String status) {
        switch (status) {
            case "PENDING":     return 0;
            case "IN_PROGRESS": return 1;
            case "RESOLVED":    return 2;
            default:            return 3;
        }
    }

    // ─── Status Update ────────────────────────────────────────────────────────

    /** Updates a complaint's status and recalculates priority. */
    public boolean updateStatus(int id, String newStatus) {
        Complaint complaint = complaintMap.get(id);
        if (complaint == null) return false;

        complaint.setStatus(newStatus);

        // Recalculate priority after status change
        PriorityCalculator.calculate(complaint);
        priorityHeap.updatePriority(complaint);
        return true;
    }

    // ─── SLA Check ────────────────────────────────────────────────────────────

    /**
     * Checks all PENDING complaints against their SLA threshold.
     * Marks violators and boosts their priority score.
     */
    public void checkAndUpdateSLA() {
        int violations = 0;
        for (Complaint c : complaintMap.values()) {
            if (c.getStatus().equals("RESOLVED")) continue;

            int slaHours = SLA_HOURS.getOrDefault(c.getCategory(), 96);
            long hoursElapsed = java.time.temporal.ChronoUnit.HOURS
                    .between(c.getCreatedAt(), LocalDateTime.now());

            if (hoursElapsed > slaHours && !c.isSlaViolated()) {
                c.setSlaViolated(true);
                PriorityCalculator.calculate(c);
                priorityHeap.updatePriority(c);
                violations++;
            }
        }
        if (violations > 0) {
            System.out.println("[SLA] " + violations
                    + " complaint(s) SLA violated – priority updated.");
        }
    }

    // ─── Graph: BFS – Find Nearest Team ───────────────────────────────────────

    /**
     * Uses BFS on city graph to find the nearest response team.
     * Returns: team, bfsResult (path + visited), minHops
     */
    public Map<String, Object> findNearestTeam(String targetArea) {
        Map<String, Object> result = new HashMap<>();
        result.put("team", null);
        result.put("bfsResult", null);
        result.put("minHops", Integer.MAX_VALUE);

        TeamInfo bestTeam   = null;
        Map<String, Object> bestBfs = null;
        int minHops         = Integer.MAX_VALUE;

        for (TeamInfo team : teams) {
            Map<String, Object> bfsResult = cityGraph.bfs(team.area, targetArea);
            int hops = (int) bfsResult.get("hops");
            if (hops >= 0 && hops < minHops) {
                minHops  = hops;
                bestTeam = team;
                bestBfs  = bfsResult;
            }
        }

        result.put("team",      bestTeam);
        result.put("bfsResult", bestBfs);
        result.put("minHops",   minHops);
        return result;
    }

    // ─── Graph: Dijkstra – Find Fastest Route ────────────────────────────────

    /**
     * Uses Dijkstra on city graph to find the fastest weighted route.
     * Returns: path, distance, visited, distances
     */
    public Map<String, Object> findFastestRoute(String start, String target) {
        return cityGraph.dijkstra(start, target);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public Graph getCityGraph() {
        return cityGraph;
    }

    public int getTotalComplaints() {
        return complaintMap.size();
    }

    public long getResolvedCount() {
        return complaintMap.values().stream()
                .filter(c -> "RESOLVED".equals(c.getStatus()))
                .count();
    }

    public long getPendingCount() {
        return complaintMap.values().stream()
                .filter(c -> "PENDING".equals(c.getStatus()))
                .count();
    }

    // ─── Inner Class: TeamInfo ────────────────────────────────────────────────

    /**
     * Represents a response team with a name and base area.
     */
    public static class TeamInfo {
        public final String name;
        public final String area;

        public TeamInfo(String name, String area) {
            this.name = name;
            this.area = area;
        }

        @Override
        public String toString() {
            return name + " (Base: " + area + ")";
        }
    }
}