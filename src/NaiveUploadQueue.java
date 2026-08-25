public class NaiveUploadQueue {
    private Submission[] kuyrukDizisi;
    private int elemanSayisi;

    public NaiveUploadQueue(int capacity) {
        kuyrukDizisi = new Submission[capacity];
        elemanSayisi = 0;
    }

    public boolean enqueue(Submission s) {
        if (elemanSayisi == kuyrukDizisi.length) {
            return false; // Kapasite doluysa reddet
        }
        kuyrukDizisi[elemanSayisi] = s;
        elemanSayisi++;
        return true;
    }

    public Submission dequeue() {
        if (elemanSayisi == 0) {
            return null;
        }
        Submission ilkEleman = kuyrukDizisi[0];

        // $O(n)$ maliyet yaratan kaydırma işlemi
        for (int i = 1; i < elemanSayisi; i++) {
            kuyrukDizisi[i - 1] = kuyrukDizisi[i];
        }

        kuyrukDizisi[elemanSayisi - 1] = null;
        elemanSayisi--;
        return ilkEleman;
    }

    public int size() {
        return elemanSayisi;
    }
}