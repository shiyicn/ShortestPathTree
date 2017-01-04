import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Graph class containing all edges and vertices
 */

public class Graph {
    private HashMap<Long, Vertex> vertices;
    private HashMap<Long, LinkedList<Long>> neighborsOut;
    private HashMap<Long, LinkedList<Long>> neighborsIn;
    private HashMap<Edge, Integer> distance;

    /**
     * constructor function to build an
     * empty graph
     */
    public Graph() {
        this.vertices = new HashMap<>();
        this.distance = new HashMap<>();
        this.neighborsOut = new HashMap<>();
        this.neighborsIn = new HashMap<>();
    }

    /**
     * use a specific map txt file to construct a graph
     * @param g
     * @param map
     */
    public static void addMap(Graph g, String map) {
        try {
            String filename = map;
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            while (line != null) {
                String[] strings = line.split(" ");

                if (strings[0].equals("v")) {
                    g.addVertex(Long.parseLong(strings[1]),
                            Integer.parseInt(strings[2]),
                            Integer.parseInt(strings[3]));
                }

                if (strings[0].equals("a")) {
                    g.addEdge(Long.parseLong(strings[1]),
                            Long.parseLong(strings[2]),
                            Integer.parseInt(strings[3]));
                }

                line = br.readLine();
            }
            br.close();
            System.out.println("Finish reading graph file and constructing a graph successfully! ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * add a vertex to current graph with
     * its id, longitude and altitude
     * @param id : vertex id
     * @param longitude : longitude of vertex
     * @param altitude : altitude of vertex
     * @return vertex
     */
    public Vertex addVertex(long id, int longitude, int altitude) {
        Vertex v = new Vertex(id, longitude, altitude);
        vertices.put(id, v);
        return v;
    }

    /**
     * add an edge to current graph with its start
     * vertex id, end vertex id and distance
     * at the same time we add successor vertex id to start vertex
     * and we add predecessor vertex id to end vertex
     * @param start : start vertex of the edge
     * @param end : end vertex of the edge
     * @param distance : the length of this edge
     * @return edge
     */
    public Edge addEdge(long start, long end, int distance) {
        Edge edge = new Edge(start, end);
        this.distance.put(edge, distance);

        LinkedList<Long> successors = neighborsOut.get(start);
        if (successors == null) {
            successors = new LinkedList<>();
            successors.add(end);
            neighborsOut.put(start, successors);
        } else {
            successors.add(end);
        }

        LinkedList<Long> predecessor = neighborsIn.get(end);
        if (predecessor == null) {
            predecessor = new LinkedList<>();
            predecessor.add(start);
            neighborsIn.put(end, predecessor);
        } else {
            predecessor.add(start);
        }

        return edge;
    }

    /**
     * get vertex with its id
     * @param id : the id of vertex to search
     * @return vertex
     */
    public Vertex getVertex(long id) {
        return vertices.get(id);
    }

    /**
     * get vertex' neighborsOut with its id
     * @param id : id of the center vertex
     * @param flagOfOut : in or out research flag
     * @return neighborsOut
     */
    public LinkedList<Long> getNeighbors(long id, boolean flagOfOut) {
        if (flagOfOut) return neighborsOut.get(id);
        return neighborsIn.get(id);
    }

    /**
     * get distance between start vertex and end vertex with
     * with their ids
     * @param start : start vertex
     * @param end : end vertex
     * @param flagOfOut : in or out edge flag
     * @return distance
     */
    public int getDistance(long start, long end, boolean flagOfOut) {
        if (flagOfOut)
            return distance.get(new Edge(start, end));
        else
            return distance.get(new Edge(end, start));
    }

    /**
     * visualize the shortest path tree using a center vertex id
     * and all vertices to be plotted
     * @param pointsFile : file name which we register the vertices to plot
     * @param center : source vertex
     * @param plottedPoints : all vertices need to plot
     */
    public void visualization(String pointsFile, long center, LinkedList<Vertex> plottedPoints) {
        File points = new File(pointsFile);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(points));
            out.write("// A sequence of points to be plotted, in standard [latitude, longitude] coordinates, expressed in degrees.\n");
            out.write("var plottedPoints = [" + "\n");
            out.flush();
            for (Vertex v : plottedPoints) {
                out.write(v.toString() + "," + "\n");
                out.flush();
            }
            out.write("];\n" + "\n" + "var centralMarker =");
            out.flush();
            out.write(getVertex(center).toString() + ";");
            out.close();
        }catch (IOException e) {

        }
    }

    public List<Long> ids() {
        List<Long> ids = new ArrayList<>(vertices.keySet());
        return ids;
    }

}
