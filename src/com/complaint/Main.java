package com.complaint;

import com.complaint.ui.PublicMenu;
import com.complaint.ui.AdminMenu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================================");
        System.out.println("     PUBLIC COMPLAINT PRIORITY ANALYZER - DSA PROJECT");
        System.out.println("========================================================");
        System.out.println("DSA IMPLEMENTATIONS: Heap | HashMap | HashSet | Graph");
        System.out.println("ALGORITHMS: BFS | Dijkstra | Sorting | Hashing");
        System.out.println();

        while (true) {
            System.out.println("1. Public User");
            System.out.println("2. Admin / Officer");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    PublicMenu publicMenu = new PublicMenu();
                    publicMenu.showMenu();
                    break;
                case 2:
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.showMenu();
                    break;
                case 3:
                    System.out.println();
                    System.out.println("Thank you for using the system!");
                    System.out.println("DSA Features Used: Heap, HashMap, HashSet, Graph, BFS, Dijkstra, Sorting");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}