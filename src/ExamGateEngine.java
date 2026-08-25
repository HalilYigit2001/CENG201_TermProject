public class ExamGateEngine {
    public static void main(String[] args) {
        System.out.println("=== EXAM GATE ENGINE STARTING (WP7) ===");
        long seed = 20260725L;
        ScenarioGenerator generator = new ScenarioGenerator(seed);

        SubmissionRegistry registry = new SubmissionRegistry(1000);
        CircularUploadQueue intakeQueue = new CircularUploadQueue(3000);
        HeapDispatcher dispatcher = new HeapDispatcher(3000);
        RollbackService rollbackService = new RollbackService(registry);
        SubmissionTimeline timeline = new SubmissionTimeline();

        // Yaklaşık 2500 yükleme olayı üret
        Submission[] allEvents = generator.generateUploadEvents(2500);
        System.out.println("Generated total upload events: " + allEvents.length);

        int burstSize = allEvents.length / 3;
        int eventIndex = 0;

        // --- CHECKPOINT 1: After Burst 1 ---
        for (int i = 0; i < burstSize; i++) {
            intakeQueue.enqueue(allEvents[eventIndex++]);
        }
        processQueueToDispatcher(intakeQueue, dispatcher);

        System.out.println("\n>>> CHECKPOINT 1: After Burst 1 <<<");
        printCheckpointStatus(intakeQueue, dispatcher, registry, timeline);

        // --- CHECKPOINT 2: At 23:59 ---
        for (int i = 0; i < burstSize; i++) {
            if(eventIndex < allEvents.length) intakeQueue.enqueue(allEvents[eventIndex++]);
        }
        processQueueToDispatcher(intakeQueue, dispatcher);

        System.out.println("\n>>> CHECKPOINT 2: At 23:59 <<<");
        printCheckpointStatus(intakeQueue, dispatcher, registry, timeline);

        // --- CHECKPOINT 3: Final (Remaining bursts & deadline processing) ---
        while (eventIndex < allEvents.length) {
            intakeQueue.enqueue(allEvents[eventIndex++]);
        }
        processQueueToDispatcher(intakeQueue, dispatcher);

        // Dispatcher'dan çıkanları Registry, Timeline ve Rollback sistemine işle
        int processedCount = 0;
        Submission nextSub;
        while ((nextSub = dispatcher.next()) != null) {
            Submission existing = registry.lookup(nextSub.getStudentId());
            if (existing != null) {
                // Re-upload durumunda önce eski versiyonu stack'e kaydet (Rollback servisi)
                rollbackService.saveVersionBeforeUpdate(nextSub.getStudentId());
                registry.updateVersion(nextSub.getStudentId(), nextSub.getFileName(), nextSub.getSizeKb(), nextSub.getTimestampMs());
            } else {
                registry.put(nextSub);
            }
            timeline.insert(nextSub);
            processedCount++;
        }

        System.out.println("\n>>> CHECKPOINT 3: Final End-to-End Simulation Results <<<");
        printCheckpointStatus(intakeQueue, dispatcher, registry, timeline);
        System.out.println("Total processed active submissions in registry: " + registry.size());
    }

    private static void processQueueToDispatcher(CircularUploadQueue queue, HeapDispatcher dispatcher) {
        Submission s;
        while ((s = queue.dequeue()) != null) {
            dispatcher.submit(s);
        }
    }

    private static void printCheckpointStatus(CircularUploadQueue queue, HeapDispatcher dispatcher, SubmissionRegistry registry, SubmissionTimeline timeline) {
        System.out.println("- Intake Queue Occupancy (Size): " + queue.size());
        System.out.println("- Registry Unique Active Students: " + registry.size());
        System.out.println("- Timeline Tree Height: " + timeline.height());
    }
}