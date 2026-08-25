public class WP3Demo {
    public static void main(String[] args) {
        System.out.println("--- WP3 DEMO START ---");
        ScenarioGenerator gen = new ScenarioGenerator(999L);

        Submission[] sekizOdev = new Submission[8];
        for (int i = 0; i < 8; i++) {
            sekizOdev[i] = gen.nextUpload(i);
        }

        NaiveDispatcher naive = new NaiveDispatcher(100);
        HeapDispatcher heap = new HeapDispatcher(100);

        for (Submission s : sekizOdev) {
            naive.submit(s);
            heap.submit(s);
        }

        System.out.println("--- Naive Dispatcher Ciktisi ---");
        for (int i = 0; i < 8; i++) {
            System.out.println(naive.next());
        }

        System.out.println("\n--- Heap Dispatcher Ciktisi ---");
        for (int i = 0; i < 8; i++) {
            System.out.println(heap.next());
        }

        System.out.println("\n--- 10,000 Burst Benchmark ---");
        int burstSize = 10000;
        Submission[] burst = new Submission[burstSize];
        // WP2'deki hatayı önlemek için 800 sınırına (mod 800) dikkat ediyoruz
        for(int i = 0; i < burstSize; i++) burst[i] = gen.nextUpload((i + 15) % 800);

        NaiveDispatcher perfNaive = new NaiveDispatcher(burstSize);
        long startNaive = System.nanoTime();
        for(Submission s : burst) perfNaive.submit(s);
        long endNaive = System.nanoTime();

        HeapDispatcher perfHeap = new HeapDispatcher(burstSize);
        long startHeap = System.nanoTime();
        perfHeap.loadBurst(burst);
        long endHeap = System.nanoTime();

        System.out.println("Naive (O(n) tek tek ekleme) took: " + (endNaive - startNaive) / 1_000_000 + " ms");
        System.out.println("Heap (O(n) bottom-up loadBurst) took: " + (endHeap - startHeap) / 1_000_000 + " ms");
    }
}