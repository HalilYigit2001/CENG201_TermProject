public class CircularUploadQueue {
    private Submission[] daireselDizi;
    private int bas, son, elemanSayisi, kapasite;

    public CircularUploadQueue(int capacity) {
        this.kapasite = capacity;
        daireselDizi = new Submission[capacity];
        bas = 0;
        son = 0;
        elemanSayisi = 0;
    }

    public boolean enqueue(Submission s) {
        if (elemanSayisi == kapasite) {
            return false; // Reject new policy: Kapasite dolu, yeniyi reddet
        }
        daireselDizi[son] = s;
        son = (son + 1) % kapasite;
        elemanSayisi++;
        return true;
    }

    public Submission dequeue() {
        if (elemanSayisi == 0) {
            return null;
        }
        Submission ilkEleman = daireselDizi[bas];
        daireselDizi[bas] = null;
        bas = (bas + 1) % kapasite;
        elemanSayisi--;
        return ilkEleman;
    }

    public int size() {
        return elemanSayisi;
    }
}