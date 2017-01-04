/**
 * vertex class to describe a point in graph
 */

public class Vertex {

    private final long id;
    private final long endId;
    private int longitude;
    private int altitude;
    private int intermediateDistance;

    /**
     * constructor for a vertex
     * @param id : id of vertex
     * @param longitude : longitude of the vertex
     * @param altitude : the altitude of the vertex
     */
    public Vertex(long id, int longitude, int altitude) {
        this.id = id;
        this.longitude = longitude;
        this.altitude = altitude;
        intermediateDistance = -1;
        endId = -1;
    }

    /**
     * constructor for an verticesDist point
     * @param longitude : longitude of the vertex
     * @param altitude : altitude of the vertex
     * @param originId : origin vertex id of the intermediate vertex
     * @param endId : end vertex id of the intermediate vertex
     * @param intermediateDistance : distance to origin vertex
     */
    public Vertex(int longitude, int altitude, long originId, long endId, int intermediateDistance) {
        this.longitude = longitude;
        this.altitude = altitude;
        this.id = originId;
        this.endId = endId;
        this.intermediateDistance = intermediateDistance;
    }


    /**
     * access to private variables
     * longitude, altitude, id(origin vertex id), distance to originId, endId
     * @return
     */
    public int getLongitude() {
        return longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public long getId() {
        return id;
    }

    public int getIntermediateDistance() {
        return intermediateDistance;
    }

    /**
     * calculate a vertex with certain distance from start vertex
     * in the edge of start vertex and endId vertex
     * @param g
     * @param start
     * @param end
     * @param dist
     * @return vertex
     */
    public static Vertex intermediatePoint(Graph g, long start, long end, int dist, boolean flagOfOut) {
        Vertex s = g.getVertex(start);
        Vertex e = g.getVertex(end);
        int d = g.getDistance(start, end, flagOfOut);
        if (dist == 0) return g.getVertex(start);
        return new Vertex(
                (int) (s.getLongitude() + (e.getLongitude() - s.getLongitude()) * (1.0 * dist / d)),
                (int) (s.getAltitude() + (e.getAltitude() - s.getAltitude()) * (1.0 * dist / d)),
                start,
                end,
                dist
        );
    }

    /**
     * calculate a vertex with certain distance from start vertex
     * in the edge of start vertex and intermediate point
     * @param g
     * @param start
     * @param interEnd
     * @param dist
     * @return
     */
    public static Vertex intermediatePoint(Graph g, long start, Vertex interEnd, int dist) {
        Vertex s = g.getVertex(start);
        int d = interEnd.getIntermediateDistance();
        if (dist == 0) return g.getVertex(start);
        return new Vertex(
                (int) (s.getLongitude() + (interEnd.getLongitude() - s.getLongitude()) * (1.0 * dist / d)),
                (int) (s.getAltitude() + (interEnd.getAltitude() - s.getAltitude()) * (1.0 * dist / d)),
                s.getId(),
                interEnd.endId,
                dist
        );
    }

    /**
     * to determine if a current point is an verticesDist one
     * @return true or false
     */
    public boolean isIntermediate() {
        return intermediateDistance != -1;
    }

    @Override
    public int hashCode() {
        return (((int) id) + intermediateDistance) & 0x7fffffff;
    }

    @Override
    public boolean equals(Object o) {
        Vertex v = (Vertex) o;
        if (!isIntermediate()) return this.id == v.id;
        else return
                this.id == v.id &&
                this.intermediateDistance == v.intermediateDistance;
    }

    @Override
    public String toString() {
        return "[" + altitude/1000000.0 + ", " + longitude/1000000.0 + "]";
    }
}
