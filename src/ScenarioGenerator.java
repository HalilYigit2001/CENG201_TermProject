import java.util.Random;

public class ScenarioGenerator {
    public static final int STUDENT_COUNT = 800;
    public static final long WINDOW_OPENS_MS = 79_200_000L;
    private final Random rng;
    private final boolean[] accommodation;
    private long clockMs = WINDOW_OPENS_MS;

    public ScenarioGenerator(long seed) {
        this.rng = new Random(seed);
        this.accommodation = new boolean[STUDENT_COUNT];
        for (int i = 0; i < STUDENT_COUNT; i++) {
            accommodation[i] = rng.nextInt(100) < 3;
        }
    }

    public String studentId(int i) { return String.format("S-%04d", i + 1); }
    public boolean hasAccommodation(int i) { return accommodation[i]; }

    public Submission nextUpload(int i) {
        clockMs += 1 + rng.nextInt(2_000);
        int sizeKb = 200 + rng.nextInt(4_800);
        String fileName = studentId(i) + "_project.pdf";
        return new Submission(studentId(i), fileName, sizeKb, clockMs, 1, accommodation[i]);
    }

    public static void main(String[] args) {
        ScenarioGenerator gen = new ScenarioGenerator(20260725L);
        for (int i = 0; i < 5; i++) {
            System.out.println(gen.nextUpload(i));
        }
    }

    // WP7 için 3 patlama (burst) halinde toplam ~2500 yükleme olayı üreten metod
    public Submission[] generateUploadEvents(int totalEvents) {
        Submission[] events = new Submission[totalEvents];
        for (int i = 0; i < totalEvents; i++) {
            int studentIdx = rng.nextInt(STUDENT_COUNT);

            // Re-upload ihtimali %10
            boolean isReupload = rng.nextInt(100) < 10;

            if (isReupload) {
                // Zamanı biraz ilerletip aynı öğrenci için yeni versiyon simüle ediyoruz
                clockMs += 1 + rng.nextInt(1000);
                int sizeKb = 200 + rng.nextInt(4_800);
                String fName = studentId(studentIdx) + "_final.pdf";
                events[i] = new Submission(studentId(studentIdx), fName, sizeKb, clockMs, 2, accommodation[studentIdx]);
            } else {
                events[i] = nextUpload(studentIdx);
            }
        }
        return events;
    }

}