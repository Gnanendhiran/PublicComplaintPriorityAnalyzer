# 🚨 Public Complaint Priority Analyzer

### A DSA-driven Java system for intelligently prioritizing, managing, and routing public complaints.

<p align="center">
  <b>Priority Management • Duplicate Detection • Team Dispatch • Route Optimization</b>
</p>

---

## 📌 Overview

**Public Complaint Priority Analyzer** is a terminal-based Java application that applies **Data Structures and Algorithms to a real-world complaint management problem**.

Instead of processing complaints simply in the order they are received, the system calculates a **dynamic priority score** based on factors such as severity, category, duplicate reports, complaint age, public impact, safety risk, location importance, peak hours, and SLA violations.

The system then uses the most suitable DSA for each operation — from **Max Heap-based priority processing** to **BFS team discovery and Dijkstra route optimization**.

---

## ✨ Key Features

- 🎯 **Dynamic Complaint Prioritization**
- 🔍 **Fast Complaint Search by ID**
- ♻️ **Duplicate Complaint Detection**
- 🚨 **SLA-based Priority Escalation**
- 🏆 **Highest & Top-K Priority Complaints**
- 📊 **Complaint, Area & Category Statistics**
- 👷 **Nearby Team Discovery**
- 🗺️ **Fastest Route Calculation**
- 🔄 **Complaint Status Management**
- 📋 **Sorting by Priority, Date, Area & Status**

---

## 🧠 DSA Behind the Project

| Requirement | DSA Used | Why? |
|---|---|---|
| Highest-priority complaint | **Max Heap** | Fast access to maximum priority |
| Complaint search | **HashMap** | O(1) average lookup |
| Duplicate detection | **HashSet** | O(1) average membership check |
| Nearby team | **BFS** | Minimum-hop traversal |
| Fastest route | **Dijkstra** | Weighted shortest path |
| City representation | **Graph + Adjacency List** | Efficient road-network representation |
| Complaint ranking | **Sorting / TimSort** | Complete ordered views |

### ⚡ Complexity

```text
Max Heap Peek       → O(1)
Heap Insert         → O(log n)
HashMap Search      → O(1) average
HashSet Lookup      → O(1) average
BFS                 → O(V + E)
Dijkstra            → O((V + E) log V)
Sorting             → O(n log n)
```

---

## 🎯 Dynamic Priority Engine

The complaint priority is calculated using multiple real-world factors:

```text
Priority Score =
Category
+ Severity
+ Duplicate Bonus
+ Age Bonus
+ Public Impact
+ Safety Risk
+ Location Importance
+ Peak Hour
+ SLA Violation
```

This allows the system to **re-prioritize complaints as their situation changes**, rather than treating priority as a fixed value.

---

## 🏗️ Application

### Public Side

Citizens can submit complaints with relevant information such as:

- Category
- Severity
- Area
- Description
- Public impact
- Safety risk
- Location type
- Peak-hour condition

The system performs duplicate detection, calculates priority, and stores the complaint for further processing.

### Admin Side

Officers can:

- View the highest-priority complaint
- View Top-K complaints
- Search complaints
- Sort complaints
- Analyze statistics
- Find nearby teams
- Find the fastest route
- Update complaint status

---

## 🛠️ Tech Stack

**Language:** Java  
**Concepts:** OOP, DSA, Algorithm Analysis  
**Data Structures:** Max Heap, HashMap, HashSet, Graph, Queue  
**Algorithms:** BFS, Dijkstra, Sorting  
**Interface:** Terminal / Console

---

## ▶️ Run Locally

### Prerequisites

Make sure Java is installed:

```bash
java -version
```

### Clone the Repository

```bash
git clone https://github.com/Gnanendhiran/PublicComplaintPriorityAnalyzer.git
cd PublicComplaintPriorityAnalyzer
```

### Compile

```bash
javac -d out src/com/complaint/**/*.java
```

### Run

```bash
java -cp out com.complaint.Main
```

You can also open the project in **IntelliJ IDEA, Eclipse, or VS Code** and run `Main.java`.

---

## 💡 Project Highlights

> **The core idea of this project is simple: choose the right data structure for the operation that needs to be optimized.**

**Max Heap** → What should be handled first?  
**HashMap** → Where is this complaint?  
**HashSet** → Is this complaint a duplicate?  
**BFS** → Which team is nearby?  
**Dijkstra** → Which route is fastest?

---

## 👨‍💻 Author

**Gnanendhiran**

[GitHub](https://github.com/Gnanendhiran)
