import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Dijkstra class to calculate the shortest path distance
 * between a given source vertex et la destination
 */
public class Dijkstra {

    private Graph g;
    private boolean flagOfOut;
    private PriorityQueue<Pair> unsettled;
    private HashSet<Long> settled;
    private HashMap<Long, Integer> shortestDistances;

    /**
     * constructor function of Dijkstra graph search function
     * @param g : graph
     * @param flagOfOut : in or out research flag
     */
    public Dijkstra(Graph g, boolean flagOfOut) {
        this.g = g;
        this.flagOfOut = flagOfOut;
        unsettled = new PriorityQueue<>();
        settled = new HashSet<>();
        shortestDistances = new HashMap<>();
    }

    /**
     * calculate all vertices with certain shortest prev distance from(to)
     * the start(destination) vertex
     * @param start : source vertex
     * @param end : destination vertex we look for
     * @return distance between two vertex or -1 if the destination is not reachable
     */
    public int distance(Vertex start, Vertex end) {

        int distance = 0;
        long s = start.getId();
        long e = end.getId();

        if (start.isIntermediate()) {
            distance += start.getIntermediateDistance();
        }

        if (end.isIntermediate())
            distance += end.getIntermediateDistance();

        //allocate needed temporary variables
        Pair p;
        LinkedList<Long> neighbors;
        int distCurrent;

        //add start vertex to unsettled set
        unsettled.add(new Pair(s,0));
        while(!unsettled.isEmpty()) {
            //select the first vertex in the unsettled set
            p = unsettled.poll();

            //we possibly continue to add new distances for settled vertices
            if (settled.contains(p.k)) continue;

            settled.add(p.k);
            shortestDistances.put(p.k, p.v);

            //if the destination is reached we break the loop
            if (p.k == e) break;

            neighbors = g.getNeighbors(p.k, flagOfOut);
            if (neighbors != null) {
                for (Long v : neighbors) {

                    if (settled.contains(v)) continue;

                    distCurrent = p.v + g.getDistance(p.k, v, flagOfOut);
                    //if vertex v isn't yet reached or current distance is smaller than its former one
                    if (shortestDistances.get(v) == null || distCurrent < shortestDistances.get(v)) {
                        //update its distance to the source
                        shortestDistances.put(v, distCurrent);
                        //add it to the unsettled set
                        unsettled.add(new Pair(v, distCurrent));
                    }
                }
            }
        }

        if (shortestDistances.containsKey(e)) {
            distance += shortestDistances.get(e);
            return distance;
        } else return -1;

    }

    /**
     * verify whether there is a point a inSet and a point in outSet that their distance is
     * equal to a given distance
     * @param inSet : to source research result
     * @param outSet : from source research result
     * @param distance : distance we need to verify
     * @return : optimal distance equals to distance or not
     */
    public boolean checkDistance(HashSet<Vertex> inSet, HashSet<Vertex> outSet, int distance) {
        boolean breakFlag = false;
        for (Vertex vi : inSet) {
            for (Vertex vj : outSet) {
                if (distance(vi, vj) == distance) {
                    breakFlag = true;
                    break;
                }
            }
            if (breakFlag) break;
        }

        return breakFlag;
    }

    /**
     * clear former research data
     */
    public void clear() {
        this.unsettled = new PriorityQueue<>();
        this.settled = new HashSet<>();
        this.shortestDistances = new HashMap<>();
    }

}
