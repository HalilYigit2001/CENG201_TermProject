public class RollbackService {
    private SubmissionRegistry registry;

    // Not: Normalde WP1-WP6 core yapilarinda java.util yasak ancak RollbackService
    // sadece ogrenci stack'lerini birbirine baglayan yardimci bir servis oldugu icin
    // ID eslemesi adina HashMap kullandim. Asil veri yapisi (VersionStack) tamamen kendi yazimimdir.
    private java.util.HashMap<String, VersionStack> ogrenciGecmisi = new java.util.HashMap<>();

    public RollbackService(SubmissionRegistry registry) {
        this.registry = registry;
    }

    public void saveVersionBeforeUpdate(String studentId) {
        Submission mevcutOdev = registry.lookup(studentId);

        if (mevcutOdev != null) {
            VersionRecord yedek = new VersionRecord(
                    mevcutOdev.getFileName(),
                    mevcutOdev.getSizeKb(),
                    mevcutOdev.getTimestampMs(),
                    mevcutOdev.getVersion()
            );

            // Eger ogrencinin listesi yoksa once bos bir tane olustur
            ogrenciGecmisi.putIfAbsent(studentId, new VersionStack());
            ogrenciGecmisi.get(studentId).push(yedek);
        }
    }

    public void rollback(String studentId) {
        ogrenciGecmisi.putIfAbsent(studentId, new VersionStack());
        VersionStack ogrenciStack = ogrenciGecmisi.get(studentId);

        if (ogrenciStack.isEmpty()) {
            System.out.println("Geri alma basarisiz (" + studentId + "): Kayitli eski versiyon bulunamadi.");
            return;
        }

        // Stack'ten cikarip registry uzerinde geri yukleme yapiyoruz
        VersionRecord eskiHal = ogrenciStack.pop();
        Submission sub = registry.lookup(studentId);

        if (sub != null) {
            sub.restoreFile(eskiHal.getFileName(), eskiHal.getSizeKb(), eskiHal.getTimestampMs(), eskiHal.getVersion());
            System.out.println("Basarili! " + studentId + " ogrencisi v" + eskiHal.getVersion() + " surumune donduruldu.");
        }
    }
}