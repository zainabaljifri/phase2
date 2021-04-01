
import java.util.ArrayList;

// Dijkstra's Algorithm in Java

public class Dijkstra {

    static String[] cities = {"Jeddah", "Makkah", "Madinah", "Riyadh", "Dammam", "Taif", "Abha", "Tabuk", "Qasim", "Hail", "Jizan", "Najran"};
//    static String[] cities = {"a", "b", "c", "d", "e","f"};
    static String tree = "";
    static String visited = "";
    static ArrayList<edge> Edges= new ArrayList();
    static int INF = Integer.MAX_VALUE;
    // for the console output colors, comment out from line 15-23 if it is not supported
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void dijkstra(int[][] graph, int source) {
        int count = graph.length;
        boolean[] visitedVertex = new boolean[count];
        int[] distance = new int[count];
        // initialize visitedVertex and distance arrays
        for (int i = 0; i < count; i++) {
            visitedVertex[i] = false;
            distance[i] = Integer.MAX_VALUE;
             Edges.add(new edge(source));
        }

        // Distance of self loop is zero
        distance[source] = 0;
        for (int i = 0; i < count; i++) {
            // Update the distance between neighbouring vertex and source vertex
            int u = findMinDistance(distance, visitedVertex);
            visitedVertex[u] = true;
            
            
            // Update all the neighbouring vertex distances
            for (int v = 0; v < count; v++) {
                if (!visitedVertex[v] && graph[u][v] != 0 && graph[u][v] != INF &&(distance[u] + graph[u][v] < distance[v])) {
                    distance[v] = distance[u] + graph[u][v];
                    Edges.get(v).setSrc(u); // save the intermediate source
                    visited+=cities[v];  // save the visited nodes labels
                   
                }
            }
            // printing tree vertex 
            System.out.println(ANSI_GREEN+"\nTree Vertices VT"+ANSI_RESET);
            tree += cities[u] + "(" + (cities[source].equals(cities[u])?"-":cities[Edges.get(u).src]) + "," + distance[u] + ")\n";
            System.out.println(tree);
            // printing remaining vertices
            System.out.println(ANSI_PURPLE+"Remaining Vertices V-VT"+ANSI_RESET);
            for (int j = 0; j < count; j++) {
                if (!visitedVertex[j]) {
                    System.out.println(cities[j] + "("+(visited.contains(cities[j])?cities[Edges.get(j).src]:"-")+","+(distance[j]==INF?"∞":distance[j])+")");
                }
            }
            System.out.println(ANSI_BLACK+"\n-----------------------------------------"+ANSI_RESET);

        }
        // printing final distances
        System.out.println(ANSI_CYAN+"\nShortest distances from "+ cities[source] +" to all other cities are:"+ANSI_RESET);
        for (int i = 0; i < distance.length; i++) {
            if(!cities[source].equals( cities[i]))
            System.out.println(String.format("Distance from %-6s to %-7s is %-4d km", cities[source], cities[i], distance[i]));
        }

    }

    // Finding the minimum distance
    private static int findMinDistance(int[] distance, boolean[] visitedVertex) {
        int minDistance = Integer.MAX_VALUE;
        int minDistanceVertex = -1;
        for (int i = 0; i < distance.length; i++) {
            if (!visitedVertex[i] && distance[i] < minDistance) {
                minDistance = distance[i];
                minDistanceVertex = i;
               
            }
        }
        return minDistanceVertex;
    }

    public static void main(String[] args) {
        int graph[][] = new int[][]{
            //Jed  Mkh   Mad  Rydh   Dam  Taif  Abha  Tabuk Qasim  Hail Jizan Najran
            {   0,   79,  420,  949, 1343,  167,  625, 1024,  863,  777,  710,  905},  //Jeddah
            {  79,    0,  358,  870, 1265,   88,  627, 1037,  876,  790,  685,  912},  //Makkah
            { 420,  358,    0,  848, 1343,  446,  985,  679,  518,  432, 1043, 1270},  //Madinah
            { 949,  870,  848,    0,  395,  782, 1064, 1304,  330,  640, 1272,  950},  //Riyadh
            {1343, 1265, 1343,  395,    0, 1177, 1495, 1729,  725, 1035, 1667, 1345},  //Dammam
            { 167,   88,  446,  782, 1177,    0,  561, 1204,  936,  957,  763,  864},  //Taif
            { 625,  627,  985, 1064, 1459,  561,    0, 1649, 1488, 1402,  202,  280},  //Abha
            {1024, 1037,  679, 1304, 1729, 1204, 1649,    0,  974,  664, 1722, 1929},  //Tabuk
            { 863,  876,  518,  330,  725,  936, 1488,  974,    0,  310, 1561, 1280},  //Qasim
            { 777,  790,  432,  640, 1035,  957, 1402,  664,  974,    0, 1475, 1590},  //Hail
            { 710,  685, 1043, 1272, 1667,  763,  202, 1722, 1561, 1475,    0,  482},  //Jizan
            { 905,  912, 1270,  950, 1345,  864,  280, 1929, 1280, 1590,  482,    0}  //Najran
        };
//        
//        int graph2[][] = new int[][]{
//            {  0,   3, INF, INF,   6,   5},
//            {  3,   0,   1, INF, INF,   4},
//            {INF,   1,   0,   6, INF,   4},
//            {INF, INF,   6,   0,   8,   5},
//            {  6, INF, INF,   8,   0,   2},
//            {  5,   4,   4,   5,   2,   0}
//        };
        Dijkstra.dijkstra(graph, 0);
    }
    
    static class edge{
        int vert;
        int src;
        edge(int src){
            this.src=src;
        }

        public int getVert() {
            return vert;
        }

        public void setVert(int vert) {
            this.vert = vert;
        }

        public int getSrc() {
            return src;
        }

        public void setSrc(int src) {
            this.src = src;
        }
    }
}



















