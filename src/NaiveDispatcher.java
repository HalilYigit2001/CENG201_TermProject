public class NaiveDispatcher {
    private Submission[] dizi;
    private int boyut;

    public NaiveDispatcher(int kapasite) {
        dizi = new Submission[kapasite];
        boyut = 0;
    }

    // İlk parametre (a), ikinciden (b) daha mı öncelikli?
    private boolean dahaOncelikli(Submission a, Submission b) {
        if (a.hasAccommodation() && !b.hasAccommodation()) return true;
        if (!a.hasAccommodation() && b.hasAccommodation()) return false;

        // Bayraklar aynıysa, saati KÜÇÜK olan (erken yükleyen) önceliklidir
        return a.getTimestampMs() < b.getTimestampMs();
    }

    public void submit(Submission s) {
        if (boyut == dizi.length) return;

        int i;
        // Dizinin sonu en yüksek önceliklidir. Yeni geleni araya sokmak için
        // daha yüksek önceliklileri sağa kaydırıyoruz.
        for (i = boyut - 1; i >= 0; i--) {
            if (dahaOncelikli(s, dizi[i])) {
                break; // s, dizi[i]'den daha öncelikliyse burada durmalı
            } else {
                dizi[i + 1] = dizi[i]; // dizi[i] daha öncelikli, sağa kaydır
            }
        }
        dizi[i + 1] = s;
        boyut++;
    }

    public Submission next() {
        if (boyut == 0) return null;

        // En öncelikli eleman her zaman dizinin en sonundadır
        Submission enIyi = dizi[boyut - 1];
        dizi[boyut - 1] = null;
        boyut--;
        return enIyi;
    }
}