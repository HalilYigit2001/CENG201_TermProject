public class WP1Demo {
    public static void main(String[] args) {
        System.out.println("--- WP1 DEMO START ---");
        SubmissionRegistry registry = new SubmissionRegistry(4);
        ScenarioGenerator gen = new ScenarioGenerator(12345L);

        for (int i = 0; i < 8; i++) {
            registry.put(gen.nextUpload(i));
        }
        System.out.println("Total submissions in registry: " + registry.size());

        String targetId = "S-0003";
        Submission target = registry.lookup(targetId);
        System.out.println("\nLooking up " + targetId + ": " + target);

        System.out.println("\nUpdating version for " + targetId + " twice...");
        registry.updateVersion(targetId, "hw_fixed.pdf", 450, target.getTimestampMs() + 1000);
        int finalVersion = registry.updateVersion(targetId, "hw_final.pdf", 460, target.getTimestampMs() + 2000);
        System.out.println("New version is: " + finalVersion);
        System.out.println("Check updated record: " + registry.lookup(targetId));

        System.out.println("\nLooking up unknown S-9999: " + registry.lookup("S-9999"));

        System.out.println("\n--- 100,000 Lookups Benchmark ---");
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            registry.lookup(gen.studentId(i % 8));
        }
        long endTime = System.nanoTime();
        System.out.println("100,000 lookups took: " + (endTime - startTime) / 1_000_000 + " ms");
    }
}