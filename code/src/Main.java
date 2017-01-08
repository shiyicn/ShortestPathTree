import java.util.HashSet;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph();

        String map =
                "/Users/Huangzuli/Informatique/INF421/shortest_path/code/RoadNetworks/data/france.in";
        String points =
                "/Users/Huangzuli/Informatique/INF421/shortest_path/code/RoadNetworks/vis/points.js";
        Graph.addMap(g, map);

        ShortestPathTree.randomVerticesReach(1000, g);

        long center = 298251056;

        //question1(g, points, center, ShortestPathTree.HOUR);
        //question2(g, points, center, 2*ShortestPathTree.HOUR);
        //question3(g, points, center, 2*ShortestPathTree.HOUR, ShortestPathTree.HOUR);
    }

    public static void question1(Graph g, String points, long center, int distance) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        LinkedList<Vertex> l = spt.verticesDistanced(center, distance);
        Dijkstra check = new Dijkstra(g, ShortestPathTree.OUT_FLAG);
        System.out.println(l.size());
        g.visualization(points, center, l);
        g.save("Question 1.1", "q1.1.txt", center, l);
        System.out.println("Find all 1h points!");
    }

    public static void question2(Graph g, String points, long center, int distance) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        LinkedList<Vertex> l = spt.verticesDistanced(center, distance);
        g.visualization(points, center, l);
        g.save("Question 1.2", "q1.2.txt", center, l);
        System.out.println("Find all 2h points!");
    }

    public static void question3(Graph g, String points, long center, int distance, int backward) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        HashSet<Vertex> backPoints = spt.backwardPoints(
                center, distance, backward);
        LinkedList<Vertex> vertices = new LinkedList<>();
        HashSet<Vertex> vertexHashSet = new HashSet<>();
        //Dijkstra check = new Dijkstra(g, ShortestPathTree.OUT_FLAG);
        for (Vertex v : backPoints) {
            //System.out.println(check.distance(g.getVertex(center), v));
            vertices.add(v);
        }
        g.visualization(points, center, vertices);
        g.save("Question 1.3", "q1.3.txt", center, vertices);
        System.out.println("Find all 1h to 2h destination points!");
    }
}
