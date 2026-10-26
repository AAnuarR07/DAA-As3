/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3);
        d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        check(!d.union(2, 2), "union of an element with itself returns false");
        check(d.count() == 3, "count stays unchanged after a redundant union");

        DisjointSet empty = new DisjointSet(0);
        check(empty.count() == 0, "empty DSU has zero sets");

        int n = 100000;
        DisjointSet large = new DisjointSet(n);

        for (int i = 1; i < n; i++) {
            large.union(i - 1, i);
        }

        check(large.count() == 1, "large DSU has one set after all unions");
        check(large.connected(0, n - 1), "first and last elements are connected");
        check(!large.union(0, n - 1), "union in a large connected set returns false");
        check(large.count() == 1, "count stays one after a redundant large union");

        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}