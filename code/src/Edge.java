/**
 * Edge class of graph
 * it's composed of the start point id and end point id
 * one edge represents the structure " vertex s ---> vertex e "
 */

public class Edge {
    private long start;
    private long end;

    public Edge(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int hashCode() {
        return ((int) (start + end)) & 0x7fffffff;
    }

    @Override
    public boolean equals(Object o) {
        Edge e = (Edge) o;
        return this.start == e.start && this.end == e.end;
    }

    @Override
    public String toString() {
        return "edge : " + start + " --> " + end;
    }
}
