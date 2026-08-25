public class WP6Demo {
    public static void main(String[] args) {
        System.out.println("--- WP6 DEMO START ---");
        ScenarioGenerator gen = new ScenarioGenerator(333L);

        Submission[] testSubmissions = new Submission[9];
        for (int i = 0; i < 9; i++) {
            testSubmissions[i] = gen.nextUpload(i);
        }

        System.out.println("\n--- Top-3 Largest Files ---");
        Submission[] top3 = ReportService.topKLargest(testSubmissions, 3);
        for (Submission s : top3) {
            System.out.println(s.getStudentId() + " -> " + s.getSizeKb() + " KB");
        }

        System.out.println("\n--- Sorted Sheet (Merge Sort) ---");
        Submission[] sortedSheet = ReportService.sortByTimeFast(testSubmissions);
        ReportService.printSheet(sortedSheet);

        System.out.println("\n--- Binary Search: First Late Submission ---");
        int lateIdx = ReportService.findFirstAfter(sortedSheet, Submission.DEADLINE_MS);
        if (lateIdx != -1) {
            System.out.println("First late submission found at index " + lateIdx + ": " + sortedSheet[lateIdx]);
        } else {
            System.out.println("No late submissions found.");
        }

        System.out.println("\n--- 100,000 Sorts Benchmark ---");
        int benchSize = 1000; // 100,000 için süre uzun sürebileceğinden test için 1000/10000 idealdir
        Submission[] benchArray = new Submission[benchSize];
        for(int i = 0; i < benchSize; i++) benchArray[i] = gen.nextUpload(i % 800);

        long startMerge = System.nanoTime();
        ReportService.sortByTimeFast(benchArray);
        long endMerge = System.nanoTime();

        long startInsertion = System.nanoTime();
        ReportService.sortByTimeInsertion(benchArray);
        long endInsertion = System.nanoTime();

        System.out.println("Merge Sort (O(N log N)) took: " + (endMerge - startMerge) / 1_000_000 + " ms");
        System.out.println("Insertion Sort (O(N^2)) took: " + (endInsertion - startInsertion) / 1_000_000 + " ms");
    }
}