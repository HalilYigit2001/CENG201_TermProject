public class WP4Demo {
    public static void main(String[] args) {
        System.out.println("--- WP4 DEMO START ---");
        SubmissionRegistry registry = new SubmissionRegistry(10);
        RollbackService rollbackService = new RollbackService(registry);

        String sId = "S-0042";

        // 1. Upload v1
        System.out.println("1. Uploading v1...");
        Submission sub = new Submission(sId, "hw3.pdf", 300, 79_800_000L, 1, false);
        registry.put(sub);
        System.out.println(registry.lookup(sId));

        // 2. Upload v2 (v1 stack'e kaydedilir)
        System.out.println("2. Uploading v2 (re-upload)...");
        rollbackService.saveVersionBeforeUpdate(sId);
        registry.updateVersion(sId, "hw3_final.pdf", 320, 84_000_000L);
        System.out.println(registry.lookup(sId));

        // 3. Upload v3 (v2 stack'e kaydedilir)
        System.out.println("3. Uploading v3 (panic upload)...");
        rollbackService.saveVersionBeforeUpdate(sId);
        registry.updateVersion(sId, "chemistry_lab.pdf", 500, 86_200_000L);
        System.out.println(registry.lookup(sId));

        // 4. Rollback 1 (v3 giter, v2 geri gelir)
        System.out.println("\n--- Performing Rollback 1 ---");
        rollbackService.rollback(sId);
        System.out.println("Active now: " + registry.lookup(sId));

        // 5. Rollback 2 (v2 gider, v1 geri gelir)
        System.out.println("\n--- Performing Rollback 2 ---");
        rollbackService.rollback(sId);
        System.out.println("Active now: " + registry.lookup(sId));

        // 6. Rollback 3 (Stack boş, hata/uyarı mesajı vermeli)
        System.out.println("\n--- Performing Rollback 3 (Empty Stack Test) ---");
        rollbackService.rollback(sId);
    }
}