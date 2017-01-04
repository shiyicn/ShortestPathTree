import java.util.*;

public class ShortestPathTree {

    final static boolean OUT_FLAG = true;
    final static boolean IN_FLAG = false;
    final static int HOUR = 3600000;

    private Graph g;
    private boolean flagOfOut;
    private PriorityQueue<Tuple> unsettled;
    private PriorityQueue<Tuple> unsettledBuffer;
    private HashSet<Long> settled;
    private HashMap<Long, Integer> shortestDistances;
    private HashMap<Long, Long> prev;
    private LinkedList<Vertex> verticesDist;

    /**
     * constructor of ShortestPathTree class
     * unsettled and settled set of vertices aim to generate the shortest prev
     * tree during the process of algorithm
     * prev stores the shortest prev from(to) a center
     * shortestDistances stores the distance from(to) a center
     * flagOfOut designs "from center" or "to destination" problem sort
     * @param g : Graph instance built
     * @param flagOfOut : the flag to signify 'in' or 'out' research
     */
    public ShortestPathTree(Graph g, boolean flagOfOut) {
        this.g = g;
        unsettled = new PriorityQueue<>();
        settled = new HashSet<>();
        shortestDistances = new HashMap<>();
        prev = new HashMap<>();
        verticesDist = new LinkedList<>();
        this.flagOfOut = flagOfOut;
        unsettledBuffer = new PriorityQueue<>();
    }


    /**
     * calculate all vertices with certain shortest prev distance from(to)
     * the start(destination) vertex
     * @param start : source vertex
     * @param travelTime : max limit of time
     * @return vertices list of possible points
     */
    public LinkedList<Vertex> verticesDistanced(long start, int travelTime) {

        //allocate mandatory temporary variables
        Tuple p;
        Tuple p1, p2;
        LinkedList<Long> neighbors;
        int distCurrent;

        //add start source vertex to unsettled set
        unsettled.add(new Tuple(start, start, 0));
        shortestDistances.put(start, 0);

        while(!unsettled.isEmpty()) {
            //select the closest vertex in the unsettled et settledBuffer set
            p1 = unsettled.poll();
            p2 = unsettledBuffer.poll();
            if (p2 == null || p2.v > p1.v) p = p1;
            else p = p2;

            //we possibly continue to add redundant distances for settled vertices
            if (!shortestDistances.containsKey(p.k) || (p.v <= shortestDistances.get(p.k) && p.v > travelTime)) {
                //add appropriate vertex to verticesDist set
                if (shortestDistances.get(p.prev) <= travelTime) {
                    verticesDist.add(
                            Vertex.intermediatePoint(
                                    g,
                                    p.prev,
                                    p.k,
                                    travelTime - shortestDistances.get(prev.get(p.k)),
                                    flagOfOut
                            )
                    );
                }
            }

            if (!settled.contains(p.k)) {
                settled.add(p.k);
                shortestDistances.put(p.k, p.v);
            } else continue;

            neighbors = g.getNeighbors(p.k, flagOfOut);
            if (neighbors != null) {
                for (Long v : neighbors) {
                    distCurrent = p.v + g.getDistance(p.k, v, flagOfOut);
                    //if vertex v isn't yet reached or current distance is no more than its former one
                    //if (shortestDistances.get(v) == null || distCurrent < shortestDistances.get(v)) {
                    if (!settled.contains(v)) {
                        //update its distance to the source
                        if (shortestDistances.get(v) == null || distCurrent < shortestDistances.get(v)) {
                            shortestDistances.put(v, distCurrent);
                            //if the distance is more than travel time, we add it to unsettledBuffer set
                            if (p.v > travelTime)
                                unsettledBuffer.add(new Tuple(v, p.k, distCurrent));
                            else
                                unsettled.add(new Tuple(v, p.k, distCurrent));
                            //update its predecessor
                            prev.put(v, p.k);
                        }
                    } else if (distCurrent == shortestDistances.get(v)) {
                        //if distCurrent is already equal to a former one, we don't need to update its predecessor
                        if (p.v > travelTime)
                            unsettledBuffer.add(new Tuple(v, p.k, distCurrent));
                        else
                            unsettled.add(new Tuple(v, p.k, distCurrent));
                    }
                }
            }
        }

        return verticesDist;
    }

    /**
     * given a center vertex id, calculate all points we can pass from this
     * center with a certain distance and backward distance
     * @param center : source vertex
     * @param distance : distance to search
     * @param backward : back propagation distance
     * @return vertices list founded
     */
    public HashSet<Vertex> backwardPoints(long center, int distance, int backward) {
        HashSet<Vertex> verticesBack = new HashSet<>();
        LinkedList<Vertex> l = verticesDistanced(center, distance);
        Vertex temp;
        for (Vertex vertex : l) {
            temp = pointFromPoint(vertex, distance - backward);
            if (verticesBack.contains(temp)) continue;
            verticesBack.add(temp);
        }
        return verticesBack;
    }

    /**
     * get a point with certain distance to(from) certain point
     * @param v : source vertex
     * @param distance : retrograde distance
     * @return : the vertex recessed
     */
    public Vertex pointFromPoint(Vertex v, int distance) {
        long id;

        if (v.isIntermediate()) {
            int intermediateDistance = v.getIntermediateDistance();
            if(intermediateDistance > distance) {
                return Vertex.intermediatePoint(g, v.getId(), v, intermediateDistance - distance);
            }
            distance -= intermediateDistance;
        }

        id = v.getId();

        int d = g.getDistance(prev.get(id), id, flagOfOut);
        while(distance > d) {
            distance -= d;
            id = prev.get(id);
//System.out.println(prev.get(id) + " ----- " + id + " ---- " + flagOfOut);
            d = g.getDistance(prev.get(id), id, flagOfOut);
        }

        if (distance == 0) return g.getVertex(id);
        else return Vertex.intermediatePoint(g, prev.get(id), id, d - distance, flagOfOut);

    }

    /**
     * calculate the reach of a certain vertex with a approximation
     * factor in a given graph
     * @param v : center vertex id
     * @param g : graph
     * @return : approximation value of reach
     */
    public static int reach(long v, Graph g) {

        int low = 1;
        int high = 1;

        //allocate out search shortest path tree and in search one
        //also a Dijkstra one to verify the distance
        ShortestPathTree sptOut = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        ShortestPathTree sptIn = new ShortestPathTree(g, ShortestPathTree.IN_FLAG);
        Dijkstra check = new Dijkstra(g, ShortestPathTree.OUT_FLAG);

        //firstly find a bound of reach
        HashSet<Vertex> inSet;
        HashSet<Vertex> outSet;

        do {
            //clear former search data
            sptOut.clear();
            sptIn.clear();
            check.clear();

//System.out.println(low + " <= r(" + v + ")");

            high = high * 2;
            inSet = sptIn.backwardPoints(v, high, high/2);
            outSet = sptOut.backwardPoints(v, high, high/2);
            boolean breakFlag = check.checkDistance(inSet, outSet, high);
            // if there exists a optimal distance between two sets equals distance to verify
            // according to the reach property, reach surpasses high/2
            if (breakFlag) low = high / 2;
            // if not 'high' is already a upper bound of reach
            else break;
        } while (true);

//System.out.println(low + " <= r(" + v + ") <= " + high);

        sptOut.clear();
        sptIn.clear();
        check.clear();

        inSet = sptIn.backwardPoints(v, low * 3, (low * 3) /2);
        outSet = sptOut.backwardPoints(v, low * 3, (low * 3) /2);

        boolean flag = check.checkDistance(inSet, outSet, high);
        //if the distance is verified to be optimal, 1.5 * low is the lower bound
        //of the the reach
        if (flag) low = (int) (1.5 * low);
        //if not '3 * low' is a upper bound of reach
        else high = 3 * low;

//System.out.println(low + " <= r(" + v + ") <= " + high);

System.out.println("r(" + v + ") = " + (high + low) / 2);
        return (high + low) / 2;
    }

    /**
     * sample n examples from graph g
     * @param n : number of examples
     * @param g : graph
     */
    public static void randomVerticesReach(int n, Graph g) {
        List<Long> keys = g.ids();
        Random r = new Random();
        for (int i=0; i<n; i+=1) {
            reach(keys.get(r.nextInt(keys.size())), g);
        }
    }

    /**
     * print the prev of a shortest prev from start to end
     * @param start : start vertex
     * @param end : end vertex
     * @return : path from start to end vertex
     */
    public LinkedList<Vertex> getPathTrace(long start, long end) {
        LinkedList<Vertex> l = new LinkedList<>();
        if (shortestDistances.containsKey(end)) {
            System.out.println(shortestDistances.get(end));
            do {
                l.addFirst(g.getVertex(end));
                System.out.println(end + " <-- " + (end = prev.get(end)));
            } while (end != start);
            return l;
        } else {
            System.out.println("Unreachable from " + start + " to " + end);
            return l;
        }
    }

    /**
     * clear generated data during the search process
     */
    public void clear() {
        this.unsettled = new PriorityQueue<>();
        this.settled = new HashSet<>();
        this.shortestDistances = new HashMap<>();
        this.prev = new HashMap<>();
        this.verticesDist = new LinkedList<>();
    }

}
