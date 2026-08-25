import java.util.NoSuchElementException;

public class VersionStack {

    // Kendi Node yapimiz
    private class Node {
        VersionRecord data;
        Node birSonraki; // next yerine birSonraki

        public Node(VersionRecord data) {
            this.data = data;
            this.birSonraki = null;
        }
    }

    private Node tepeNoktasi; // top yerine tepeNoktasi
    private int gecerliBoyut; // size yerine

    public VersionStack() {
        this.tepeNoktasi = null;
        this.gecerliBoyut = 0;
    }

    public void push(VersionRecord v) {
        Node yeniEklenen = new Node(v);
        // Yeni geleni en uste koyuyoruz
        yeniEklenen.birSonraki = tepeNoktasi;
        tepeNoktasi = yeniEklenen;
        gecerliBoyut++;
    }

    public VersionRecord pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack bombos, geri alinacak versiyon kalmadi!");
        }

        VersionRecord silinecekKayıt = tepeNoktasi.data;
        tepeNoktasi = tepeNoktasi.birSonraki; // bir asagi kaydir
        gecerliBoyut--;

        return silinecekKayıt;
    }

    public boolean isEmpty() {
        return (tepeNoktasi == null);
    }

    public int size() {
        return gecerliBoyut;
    }
}