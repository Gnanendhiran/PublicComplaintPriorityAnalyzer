package com.complaint.dsa;

import java.util.*;

/**
 * Graph Implementation for City Map
 * Used for: BFS (Nearest Team) and Dijkstra (Fastest Route)
 * BFS: O(V + E) | Dijkstra: O((V+E)logV)
 */
public class Graph {
    private Map<String, List<Edge>> adjacencyList;
    private Map<String, Integer> nodeIndex;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.nodeIndex = new HashMap<>();
    }

    public void addNode(String node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
        nodeIndex.putIfAbsent(node, nodeIndex.size());
    }

    public void addEdge(String source, String destination, int weight) {
        addNode(source);
        addNode(destination);
        adjacencyList.get(source).add(new Edge(destination, weight));
        adjacencyList.get(destination).add(new Edge(source, weight));
    }

    public List<String> getNeighbors(String node) {
        if (!adjacencyList.containsKey(node))
            return new ArrayList<>();
        List<String> neighbors = new ArrayList<>();
        for (Edge edge : adjacencyList.get(node)) {
            neighbors.add(edge.destination);
        }
        return neighbors;
    }

    public int getEdgeWeight(String source, String destination) {
        if (!adjacencyList.containsKey(source))
            return -1;
        for (Edge edge : adjacencyList.get(source)) {
            if (edge.destination.equals(destination)) {
                return edge.weight;
            }
        }
        return -1;
    }

    /**
     * BFS Algorithm - Finds shortest path in terms of number of hops
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public Map<String, Object> bfs(String start, String target) {
        Map<String, Object> result = new HashMap<>();
        result.put("path", new ArrayList<String>());
        result.put("hops", -1);
        result.put("visited", new ArrayList<String>());

        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(target)) {
            return result;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        List<String> visitedOrder = new ArrayList<>();

        queue.offer(start);
        visited.add(start);
        visitedOrder.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                List<String> path = reconstructPath(parent, start, target);
                result.put("path", path);
                result.put("hops", path.size() - 1);
                result.put("visited", visitedOrder);
                return result;
            }

            for (Edge edge : adjacencyList.get(current)) {
                String neighbor = edge.destination;
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    visitedOrder.add(neighbor);
                    parent.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        return result;
    }

    /**
     * Dijkstra's Algorithm - Finds shortest path with weighted edges
     * Time Complexity: O((V + E) log V)
     * Space Complexity: O(V)
     */
    public Map<String, Object> dijkstra(String start, String target) {
        Map<String, Object> result = new HashMap<>();
        result.put("path", new ArrayList<String>());
        result.put("distance", -1);
        result.put("visited", new ArrayList<String>());
        result.put("distances", new HashMap<String, Integer>());

        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(target)) {
            return result;
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        List<String> visitedOrder = new ArrayList<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        for (String node : adjacencyList.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.offer(new NodeDistance(start, 0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            String currentNode = current.node;

            if (visited.contains(currentNode))
                continue;
            visited.add(currentNode);
            visitedOrder.add(currentNode);

            if (currentNode.equals(target))
                break;

            for (Edge edge : adjacencyList.get(currentNode)) {
                String neighbor = edge.destination;
                if (visited.contains(neighbor))
                    continue;

                int newDist = distances.get(currentNode) + edge.weight;
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    parent.put(neighbor, currentNode);
                    pq.offer(new NodeDistance(neighbor, newDist));
                }
            }
        }

        result.put("visited", visitedOrder);
        result.put("distances", distances);

        if (distances.get(target) != Integer.MAX_VALUE) {
            List<String> path = reconstructPath(parent, start, target);
            result.put("path", path);
            result.put("distance", distances.get(target));
        }

        return result;
    }

    private List<String> reconstructPath(Map<String, String> parent, String start, String target) {
        List<String> path = new ArrayList<>();
        String current = target;
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
            if (current != null && current.equals(start)) {
                path.add(0, start);
                break;
            }
        }
        return path;
    }

    public Set<String> getAllNodes() {
        return adjacencyList.keySet();
    }

    public void printGraphVisualization() {
        System.out.println();
        System.out.println("CITY GRAPH VISUALIZATION:");
        System.out.println("================================================================");

        for (String node : adjacencyList.keySet()) {
            System.out.printf("%-15s -> ", node);
            List<Edge> edges = adjacencyList.get(node);
            for (int i = 0; i < edges.size(); i++) {
                Edge e = edges.get(i);
                System.out.printf("%s(%dm)", e.destination, e.weight);
                if (i < edges.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();
        }
        System.out.println("================================================================");
    }

    static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class NodeDistance implements Comparable<NodeDistance> {
        String node;
        int distance;

        NodeDistance(String node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}