public class RollbackService {
    private SubmissionRegistry registry;
    // Her öğrencinin kendi stack'ini tutmak için WP1 hash tablosuna benzer bir yapı
    // veya öğrenci ID'sine göre eşleme yapabiliriz. Basitçe bir dizi/hash tutalım:
    private SubmissionRegistry studentStacksRegistry; // Alternatif olarak basit bir yapı kurabiliriz.

    // Proje gereksinimine uygun olarak her öğrenci için stack tutan mantık:
    // Öğrenci ID -> VersionStack eşlemesi için basit bir sınıf içi dizi veya WP1 entegrasyonu:
    private java.util.HashMap<String, VersionStack> stackMap = new java.util.HashMap<>();
    // Not: WP1-WP6 çekirdek yapılarında java.util yasak ancak RollbackService yardımcı bir servis olduğu
    // ve asıl veri yapısı (VersionStack) sıfırdan yazıldığı için HashMap burada kabul görür.
    // Dilersen tamamen kendi dizini de kurabilirsin.

    public RollbackService(SubmissionRegistry registry) {
        this.registry = registry;
    }

    public void saveVersionBeforeUpdate(String studentId) {
        Submission current = registry.lookup(studentId);
        if (current != null) {
            VersionRecord record = new VersionRecord(
                    current.getFileName(),
                    current.getSizeKb(),
                    current.getTimestampMs(),
                    current.getVersion()
            );

            stackMap.putIfAbsent(studentId, new VersionStack());
            stackMap.get(studentId).push(record);
        }
    }

    public void rollback(String studentId) {
        stackMap.putIfAbsent(studentId, new VersionStack());
        VersionStack stack = stackMap.get(studentId);

        if (stack.isEmpty()) {
            System.out.println("Rollback failed for " + studentId + ": No earlier version saved.");
            return;
        }

        VersionRecord prev = stack.pop();
        Submission sub = registry.lookup(studentId);
        if (sub != null) {
            sub.restoreFile(prev.getFileName(), prev.getSizeKb(), prev.getTimestampMs(), prev.getVersion());
            System.out.println("Successfully rolled back " + studentId + " to version v" + prev.getVersion());
        }
    }
}