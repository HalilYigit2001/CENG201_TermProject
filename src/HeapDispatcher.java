public class HeapDispatcher {
    private Submission[] heap;
    private int boyut;

    public HeapDispatcher(int kapasite) {
        heap = new Submission[kapasite];
        boyut = 0;
    }

    private boolean dahaOncelikli(Submission a, Submission b) {
        if (a.hasAccommodation() != b.hasAccommodation()) {
            return a.hasAccommodation();
        }
        return a.getTimestampMs() < b.getTimestampMs();
    }

    public void submit(Submission s) {
        if (boyut == heap.length) return;
        heap[boyut] = s;
        siftUp(boyut);
        boyut++;
    }

    public Submission next() {
        if (boyut == 0) return null;
        Submission enOncelikli = heap[0];
        heap[0] = heap[boyut - 1];
        heap[boyut - 1] = null;
        boyut--;
        siftDown(0);
        return enOncelikli;
    }

    // Hocanın özellikle istediği "bottom-up build-heap" metodu (O(n) karmaşıklık)
    public void loadBurst(Submission[] burst) {
        for (int i = 0; i < burst.length; i++) {
            heap[i] = burst[i];
        }
        boyut = burst.length;

        for (int i = (boyut / 2) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (dahaOncelikli(heap[index], heap[parent])) {
                Submission temp = heap[index];
                heap[index] = heap[parent];
                heap[parent] = temp;
                index = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (2 * index + 1 < boyut) {
            int solCocuk = 2 * index + 1;
            int sagCocuk = 2 * index + 2;
            int enIyi = index;

            if (solCocuk < boyut && dahaOncelikli(heap[solCocuk], heap[enIyi])) {
                enIyi = solCocuk;
            }
            if (sagCocuk < boyut && dahaOncelikli(heap[sagCocuk], heap[enIyi])) {
                enIyi = sagCocuk;
            }
            if (enIyi != index) {
                Submission temp = heap[index];
                heap[index] = heap[enIyi];
                heap[enIyi] = temp;
                index = enIyi;
            } else {
                break;
            }
        }
    }
}