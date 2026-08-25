public class ReportService {

    // 1. Top-K Largest using size-K Min-Heap - O(N log K)
    public static Submission[] topKLargest(Submission[] all, int k) {
        if (all == null || all.length == 0 || k <= 0) return new Submission[0];

        int heapSize = Math.min(k, all.length);
        Submission[] minHeap = new Submission[heapSize];
        int currentSize = 0;

        for (Submission s : all) {
            if (currentSize < heapSize) {
                minHeap[currentSize] = s;
                currentSize++;
                siftUpMin(minHeap, currentSize - 1);
            } else {
                // Eğer yeni eleman heap'in en küçüğünden (kökten) büyükse, kökü çıkar ve yeni elemanı ekle
                if (s.getSizeKb() > minHeap[0].getSizeKb()) {
                    minHeap[0] = s;
                    siftDownMin(minHeap, 0, currentSize);
                }
            }
        }

        // Min-heap içerisindeki elemanları büyükten küçüğe sıralı hale getirmek için
        Submission[] result = new Submission[currentSize];
        for (int i = currentSize - 1; i >= 0; i--) {
            result[i] = minHeap[0];
            minHeap[0] = minHeap[currentSize - 1];
            currentSize--;
            siftDownMin(minHeap, 0, currentSize);
        }
        return result;
    }

    private static void siftUpMin(Submission[] heap, int idx) {
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (heap[idx].getSizeKb() < heap[parent].getSizeKb()) {
                Submission temp = heap[idx];
                heap[idx] = heap[parent];
                heap[parent] = temp;
                idx = parent;
            } else {
                break;
            }
        }
    }

    private static void siftDownMin(Submission[] heap, int idx, int size) {
        while (2 * idx + 1 < size) {
            int left = 2 * idx + 1;
            int right = 2 * idx + 2;
            int smallest = idx;

            if (left < size && heap[left].getSizeKb() < heap[smallest].getSizeKb()) {
                smallest = left;
            }
            if (right < size && heap[right].getSizeKb() < heap[smallest].getSizeKb()) {
                smallest = right;
            }
            if (smallest != idx) {
                Submission temp = heap[idx];
                heap[idx] = heap[smallest];
                heap[smallest] = temp;
                idx = smallest;
            } else {
                break;
            }
        }
    }

    // 2. Fast Sort by Timestamp - Merge Sort O(N log N)
    public static Submission[] sortByTimeFast(Submission[] all) {
        if (all == null) return new Submission[0];
        Submission[] clone = all.clone();
        mergeSort(clone, 0, clone.length - 1);
        return clone;
    }

    private static void mergeSort(Submission[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(Submission[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Submission[] L = new Submission[n1];
        Submission[] R = new Submission[n2];

        for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].getTimestampMs() <= R[j].getTimestampMs()) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) { arr[k] = L[i]; i++; k++; }
        while (j < n2) { arr[k] = R[j]; j++; k++; }
    }

    // Cross-check baseline: Insertion Sort
    public static Submission[] sortByTimeInsertion(Submission[] all) {
        Submission[] clone = all.clone();
        for (int i = 1; i < clone.length; i++) {
            Submission key = clone[i];
            int j = i - 1;
            while (j >= 0 && clone[j].getTimestampMs() > key.getTimestampMs()) {
                clone[j + 1] = clone[j];
                j = j - 1;
            }
            clone[j + 1] = key;
        }
        return clone;
    }

    // 3. Binary Search for FIRST item strictly after deadline - O(log N)
    public static int findFirstAfter(Submission[] ascending, long deadlineMs) {
        int low = 0;
        int high = ascending.length - 1;
        int resultIndex = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (ascending[mid].getTimestampMs() > deadlineMs) {
                resultIndex = mid; // Aday bulundu, daha erken bir ilk geç kalma var mı diye sola bak
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return resultIndex;
    }

    // 4. Fixed-width Console Table Print
    public static void printSheet(Submission[] ascending) {
        System.out.println(String.format("%-10s | %-22s | %-7s | %-15s | %-10s",
                "StudentId", "FileName", "Version", "Time", "Late Flag"));
        System.out.println("------------------------------------------------------------------");
        for (Submission s : ascending) {
            System.out.println(String.format("%-10s | %-22s | v%-6d | %-15s | %-10s",
                    s.getStudentId(),
                    s.getFileName(),
                    s.getVersion(),
                    s.clock(),
                    s.isLate() ? "LATE" : "ON-TIME"));
        }
    }
}