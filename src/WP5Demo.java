public class WP5Demo {
    public static void main(String[] args) {
        System.out.println(">>> WP5 AVL TREE TESTLERI BASLIYOR <<<");
        ScenarioGenerator senaryo = new ScenarioGenerator(777L);

        // 10 bin verilik yukseklik (height) testi
        SubmissionTimeline agacDenemesi = new SubmissionTimeline();

        System.out.println("Agaca sirali sekilde 10.000 veri ekleniyor...");
        long baslangicZamani = 80_000_000L;

        for (int k = 0; k < 10000; k++) {
            Submission testOdevi = new Submission("S-" + k, "test_doc.pdf", 300, baslangicZamani + k, 1, false);
            agacDenemesi.insert(testOdevi);
        }

        System.out.println("Eger bu normal bir BST olsaydi yukseklik ~10.000 olacakti.");
        System.out.println("Bizim AVL agacinin gercek yuksekligi: " + agacDenemesi.height() + " (Beklenen: 20'den kucuk olmasi)");

        // ---------------------------------------------------------

        System.out.println("\n--- Belirli bir araliktaki odevleri bulma (Range Query) ---");
        SubmissionTimeline sorguAgaci = new SubmissionTimeline();

        for (int i = 0; i < 10; i++) {
            sorguAgaci.insert(senaryo.nextUpload(i));
        }

        long basZaman = 79_200_000L + 5000L;
        long bitZaman = 79_200_000L + 15000L;

        Submission[] bulunanlar = sorguAgaci.submittedBetween(basZaman, bitZaman);
        System.out.println("T1 ve T2 araliginda bulunan toplam odev sayisi: " + bulunanlar.length);
        System.out.println("Sorgu sirasinda ziyaret edilen dugum sayisi: " + sorguAgaci.getVisitedNodeCount());
        System.out.println("WP5 Demo tamamlandi.");
    }
}