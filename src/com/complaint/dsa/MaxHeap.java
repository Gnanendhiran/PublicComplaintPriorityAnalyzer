package com.complaint.dsa;

import com.complaint.model.Complaint;
import java.util.ArrayList;
import java.util.List;

/**
 * Max Heap Implementation for Priority Queue
 * Used for: Getting highest priority complaint in O(1)
 * Insert: O(log n) | Remove: O(log n) | Peek: O(1)
 */
public class MaxHeap {
    private List<Complaint> heap;

    public MaxHeap() {
        this.heap = new ArrayList<>();
    }

    public void insert(Complaint complaint) {
        heap.add(complaint);
        heapifyUp(heap.size() - 1);
    }

    public Complaint extractMax() {
        if (heap.isEmpty())
            return null;
        Complaint max = heap.get(0);
        Complaint last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return max;
    }

    public Complaint peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public List<Complaint> getTopK(int k) {
        List<Complaint> result = new ArrayList<>();
        if (heap.isEmpty())
            return result;

        List<Complaint> tempHeap = new ArrayList<>(heap);
        MaxHeap temp = new MaxHeap();
        temp.heap = tempHeap;

        for (int i = 0; i < Math.min(k, heap.size()); i++) {
            Complaint c = temp.extractMax();
            if (c != null)
                result.add(c);
        }
        return result;
    }

    public void updatePriority(Complaint complaint) {
        int index = heap.indexOf(complaint);
        if (index == -1)
            return;

        Complaint last = heap.remove(heap.size() - 1);
        if (index < heap.size()) {
            heap.set(index, last);
            heapifyDown(index);
            heapifyUp(index);
        }
        insert(complaint);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public List<Complaint> getAll() {
        return new ArrayList<>(heap);
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).getPriorityScore() <= heap.get(parent).getPriorityScore()) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < size && heap.get(left).getPriorityScore() > heap.get(largest).getPriorityScore()) {
                largest = left;
            }
            if (right < size && heap.get(right).getPriorityScore() > heap.get(largest).getPriorityScore()) {
                largest = right;
            }
            if (largest == index)
                break;
            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        Complaint temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}