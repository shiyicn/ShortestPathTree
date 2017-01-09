import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph();

        String map;
        /*
        String points =
                "/Users/Huangzuli/Informatique/INF421/shortest_path/code/RoadNetworks/vis/points.js";
        */
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please load a map");
            System.out.println("Two options : \nidf.in   france.in\nType one of the options!\n");
            map = scanner.nextLine();
            if (map.equals("france.in") || map.equals("idf.in")) break;
        }

        String option =
                "\nFour commands options: \n q1.1   q1.2   q1.3   reach quit\nType one of the options!\n";

        Graph.addMap(g, map);
        String command;
        long center = 298251056;
        while (true) {
            System.out.println(option);
            command = scanner.nextLine();
            if (command.equals("q1.1")) question1(g, center, ShortestPathTree.HOUR);
            else if (command.equals("q1.2")) question2(g, center, 2 * ShortestPathTree.HOUR);
            else if (command.equals("q1.3")) question3(g, center, 2 * ShortestPathTree.HOUR, ShortestPathTree.HOUR);
            else if (command.equals("reach")) ShortestPathTree.reach(g.getRandom(), g);
            else if (command.equals("quit")) break;
            else {
                System.out.println("Illegal command, try again");
            }
        }

        //question1(g, points, center, 2 * ShortestPathTree.HOUR);
        //question2(g, points, center, 2*ShortestPathTree.HOUR);
        //question3(g, points, center, 2*ShortestPathTree.HOUR, ShortestPathTree.HOUR);
    }

    public static void question1(Graph g, long center, int distance) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        LinkedList<Vertex> l = spt.verticesDistanced(center, distance);
        System.out.println(l.size());
        //g.visualization(points, center, l);
        g.save("Question 1.1", "q1.1.txt", center, l);
        System.out.println("Find all 1h points in the file \"q1.1.txt\"");
    }

    public static void question2(Graph g, long center, int distance) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        LinkedList<Vertex> l = spt.verticesDistanced(center, distance);
        //g.visualization(points, center, l);
        g.save("Question 1.2", "q1.2.txt", center, l);
        System.out.println("Find all 2h points in the file \"q1.2.txt\"");
    }

    public static void question3(Graph g, long center, int distance, int backward) {
        ShortestPathTree spt = new ShortestPathTree(g, ShortestPathTree.OUT_FLAG);
        HashSet<Vertex> backPoints = spt.backwardPoints(
                center, distance, backward);
        LinkedList<Vertex> vertices = new LinkedList<>();
        for (Vertex v : backPoints) {
            vertices.add(v);
        }
        //g.visualization(points, center, vertices);
        g.save("Question 1.3", "q1.3.txt", center, vertices);
        System.out.println("Points with 1h distance to v during a travel to a destination at least 2h away from v \"q1.3.txt\"");
    }
}
