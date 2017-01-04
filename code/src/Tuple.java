/**
 * Tuple class used for unsettled priority queue in
 * shortest path tree calculation
 */
public class Tuple implements Comparable{

    Long k;
    Long prev;
    Integer v;

    public Tuple(Long k, Long prev, Integer v) {
        this.prev = prev;
        this.k = k;
        this.v = v;
    }

    @Override
    public int compareTo(Object o) {
        Tuple p = (Tuple) o;
        if (p.v < v) return 1;
        else return -1;
    }
}
