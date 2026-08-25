public class SubmissionTimeline {

    private class Node {
        Submission data;
        Node left, right;
        int height;

        public Node(Submission data) {
            this.data = data;
            this.height = 1; // Yeni düğüm yaprak olarak başlar, yüksekliği 1'dir
        }
    }

    private Node root;
    private int visitedNodeCount; // Aralık sorgusunda ziyaret edilen düğüm sayısını saymak için

    public SubmissionTimeline() {
        this.root = null;
    }

    public int height() {
        return height(root);
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    // --- 4 ROTATION CASES ---
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Rotasyon
        x.right = y;
        y.left = T2;

        // Boyutları güncelle
        updateHeight(y);
        updateHeight(x);

        return x; // Yeni kök
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Rotasyon
        y.left = x;
        x.right = T2;

        // Boyutları güncelle
        updateHeight(x);
        updateHeight(y);

        return y; // Yeni kök
    }

    public void insert(Submission s) {
        root = insertRec(root, s);
    }

    private Node insertRec(Node node, Submission s) {
        // 1. Standart BST ekleme
        if (node == null) {
            return new Node(s);
        }

        long currentKey = s.getTimestampMs();
        long nodeKey = node.data.getTimestampMs();

        if (currentKey < nodeKey) {
            node.left = insertRec(node.left, s);
        } else if (currentKey > nodeKey) {
            node.right = insertRec(node.right, s);
        } else {
            return node; // AVL ağacında benzersiz anahtarlar esastır
        }

        // 2. Yüksekliği güncelle
        updateHeight(node);

        // 3. Denge durumunu kontrol et ve gerekli rotasyonları uygula
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && currentKey < node.left.data.getTimestampMs()) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && currentKey > node.right.data.getTimestampMs()) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && currentKey > node.left.data.getTimestampMs()) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && currentKey < node.right.data.getTimestampMs()) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Ödev kuralı: Rekürsif, aralık dışı kalan alt ağaçları (subtrees) atlayan (skip) arama metodu
    public Submission[] submittedBetween(long t1, long t2) {
        visitedNodeCount = 0;
        // Önce eşleşenleri toplamak için geçici bir liste mantığı (dizi boyutu için sayım yapabiliriz
        // veya basitçe dinamik dizi mantığı uygulayabiliriz). Java.util.ArrayList core pipeline data
        // için yasak ancak range query dönüş tipi standart dizi (Submission[]) istendiği için burada
        // iç akışta geçici sayım yapabiliriz.

        java.util.List<Submission> results = new java.util.ArrayList<>();
        searchRangeRec(root, t1, t2, results);

        Submission[] arr = new Submission[results.size()];
        for(int i=0; i<results.size(); i++) {
            arr[i] = results.get(i);
        }
        return arr;
    }

    private void searchRangeRec(Node node, long t1, long t2, java.util.List<Submission> results) {
        if (node == null) {
            return;
        }
        visitedNodeCount++;
        long key = node.data.getTimestampMs();

        // Eğer düğümün anahtarı t1'den büyükse, sol alt ağaçta aranacak eleman olma ihtimali vardır.
        // Bu optimizasyon, aralığa girmeyen alt ağaçları komple atlar (skip impossible subtrees).
        if (key > t1) {
            searchRangeRec(node.left, t1, t2, results);
        }

        // Eğer anahtar aralıktaysa sonuca ekle
        if (key >= t1 && key <= t2) {
            results.add(node.data);
        }

        // Eğer düğümün anahtarı t2'den küçükse, sağ alt ağaçta aranacak eleman olma ihtimali vardır.
        if (key < t2) {
            searchRangeRec(node.right, t1, t2, results);
        }
    }

    public int getVisitedNodeCount() {
        return visitedNodeCount;
    }
}