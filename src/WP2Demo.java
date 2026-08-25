public class WP2Demo {
    public static void main(String[] args) {
        System.out.println("--- WP2 DEMO START ---");
        ScenarioGenerator gen = new ScenarioGenerator(54321L);
        CircularUploadQueue circular = new CircularUploadQueue(5);

        System.out.println("1. Filling capacity (5)...");
        for (int i = 0; i < 5; i++) circular.enqueue(gen.nextUpload(i));
        System.out.println("Size: " + circular.size());

        System.out.println("2. Dequeue 2 elements...");
        circular.dequeue();
        circular.dequeue();
        System.out.println("Size: " + circular.size());

        System.out.println("3. Enqueue 3 elements (tail wraps)...");
        circular.enqueue(gen.nextUpload(5));
        circular.enqueue(gen.nextUpload(6));
        System.out.println("Trying 3rd element (Trigger full policy): " + circular.enqueue(gen.nextUpload(7)));
        System.out.println("Final Size: " + circular.size());

        System.out.println("\n--- 10,000 Burst Benchmark ---");
        int burstSize = 10000;
        Submission[] burst = new Submission[burstSize];
        for (int i = 0; i < burstSize; i++) burst[i] = gen.nextUpload((i + 10) % 800);

        NaiveUploadQueue naiveQueue = new NaiveUploadQueue(burstSize);
        long startNaive = System.nanoTime();
        for (Submission s : burst) naiveQueue.enqueue(s);
        for (int i = 0; i < burstSize; i++) naiveQueue.dequeue();
        long endNaive = System.nanoTime();

        CircularUploadQueue circularQueue = new CircularUploadQueue(burstSize);
        long startCircular = System.nanoTime();
        for (Submission s : burst) circularQueue.enqueue(s);
        for (int i = 0; i < burstSize; i++) circularQueue.dequeue();
        long endCircular = System.nanoTime();

        System.out.println("Naive Queue (O(n)) took: " + (endNaive - startNaive) / 1_000_000 + " ms");
        System.out.println("Circular Queue (O(1)) took: " + (endCircular - startCircular) / 1_000_000 + " ms");
    }
}