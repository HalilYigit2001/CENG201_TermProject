public class WP5Demo {
    public static void main(String[] args) {
        System.out.println("--- WP5 DEMO START ---");
        ScenarioGenerator gen = new ScenarioGenerator(777L);

        // 10,000 artan zaman damgalı veri ile AVL ve Plain BST yükseklik deneyi
        SubmissionTimeline avlTree = new SubmissionTimeline();

        // Basit bir plain BST simülasyonu için yükseklik hesabı: artık veriler sırayla girerse yükseklik ~10,000 olur.
        System.out.println("Inserting 10,000 sequential timestamps into AVL Tree...");
        long baseTime = 80_000_000L;
        for (int i = 0; i < 10000; i++) {
            Submission s = new Submission("S-" + i, "test.pdf", 300, baseTime + i, 1, false);
            avlTree.insert(s);
        }

        System.out.println("Plain BST expected height for 10,000 sorted inserts: ~10,000 (behaves like linked list)");
        System.out.println("AVL Tree actual measured height: " + avlTree.height() + " (Target: < 20)");

        // Aralık sorgusu testi (Range Query)
        System.out.println("\n--- Testing submittedBetween Range Query ---");
        SubmissionTimeline demoTimeline = new SubmissionTimeline();
        for (int i = 0; i < 10; i++) {
            demoTimeline.insert(gen.nextUpload(i));
        }

        long t1 = 79_200_000L + 5000L;
        long t2 = 79_200_000L + 15000L;

        Submission[] matches = demoTimeline.submittedBetween(t1, t2);
        System.out.println("Matches found between t1 and t2: " + matches.length);
        System.out.println("Visited nodes during query (subtrees skipped efficiently): " + demoTimeline.getVisitedNodeCount());
    }
}