import java.util.HashSet;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph();

        String map =
                "/Users/Huangzuli/Informatique/INF421/shortest_path/code/RoadNetworks/data/idf.in";
        String points =
                "/Users/Huangzuli/Informatique/INF421/shortest_path/code/RoadNetworks/vis/points.js";
        Graph.addMap(g, map);

        ShortestPathTree.randomVerticesReach(1000, g);

        //question1(g, points, center, 100000);
        //question2(g, points, center, 2*ShortestPathTree.HOUR);
        //question3(g, points, center, 1000000, 500000);
    }

    public static void question1(Graph g, String points, long center, int distance) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        LinkedList<Vertex> l = spt.verticesDistanced(center, distance);
        Dijkstra check = new Dijkstra(g, ShortestPathTree.OUT_FLAG);
        System.out.println(l.size());
        g.visualization(points, center, l);
        System.out.println("Find all 1h points!");
    }

    public static void question2(Graph g, String points, long center, int distance) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.IN_FLAG);
        LinkedList<Vertex> l = spt.verticesDistanced(center, distance);
        g.visualization(points, center, l);
        System.out.println("Find all 2h points!");
    }

    public static void question3(Graph g, String points, long center, int distance, int backward) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        HashSet<Vertex> backPoints = spt.backwardPoints(
                center, distance, backward);
        LinkedList<Vertex> vertices = new LinkedList<>();
        Dijkstra check = new Dijkstra(g, ShortestPathTree.OUT_FLAG);
        for (Vertex v : backPoints) {
            System.out.println(check.distance(g.getVertex(center), v));
            vertices.add(v);
        }
        g.visualization(points, center, vertices);
        System.out.println("Find all 1h to 2h destination points!");
    }
}
