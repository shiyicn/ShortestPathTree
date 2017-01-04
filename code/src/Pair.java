/**
 * Created by Huangzuli on 27/12/2016.
 */
/**
 * Tuple class used for unsettled priority queue in
 * shortest path tree calculation
 */
public class Pair implements Comparable{

    Long k;
    Integer v;

    public Pair(Long k, Integer v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public int compareTo(Object o) {
        Pair p = (Pair) o;
        if (p.v < v) return 1;
        else return -1;
    }
}
