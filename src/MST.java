
import javafx.util.Pair;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class MST {

    static class Edge {

        int source;
        int destination;
        int weight;

        // constructor 
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
        public String toString(){
            return source+"-"+destination+": "+weight;
        }
    }

    static class HeapNode {

        int vertex;
        int key;
    }

    static class ResultSet {

        int parent;
        int weight;
    }

    // all methods were added in the graph class
    static class Graph {

        int vertices;
        int edges;
        LinkedList<Edge>[] adjacencylist;
        

        // constructor 
        Graph(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
            adjacencylist = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i < vertices; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }

        // this method is used in make_graph() method to add a new edge
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].addFirst(edge);

            edge = new Edge(destination, source, weight);
            adjacencylist[destination].addFirst(edge); //for undirected graph
            
        }

        // Prim's Algorithm using Priprity Queue
        public void primPQ() {
            //start time
            double stime = System.currentTimeMillis();
            boolean[] mst = new boolean[vertices];
            ResultSet[] resultSet = new ResultSet[vertices];
            int[] key = new int[vertices];  //keys used to store the key to know whether priority queue update is required

            //Initialize all the keys to infinity and
            //initialize resultSet for all the vertices
            for (int i = 0; i < vertices; i++) {
                key[i] = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
            }

            //Initialize priority queue
            //override the comparator to do the sorting based keys
            PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(vertices, new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    //sort using key values
                    int key1 = p1.getKey();
                    int key2 = p2.getKey();
                    return key1 - key2;
                }
            });


            //create the pair for for the first index, 0 key 0 index
            key[0] = 0;
            Pair<Integer, Integer> p0 = new Pair<>(key[0], 0);
            //add it to pq
            pq.offer(p0);
            resultSet[0] = new ResultSet();
            resultSet[0].parent = -1;
            
            
            //while priority queue is not empty
            while (!pq.isEmpty()) {
                //extract the min
                Pair<Integer, Integer> extractedPair = pq.poll();

                //extracted vertex
                int extractedVertex = extractedPair.getValue();
                mst[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is not present in mst
                    if (mst[edge.destination] == false) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            //add it to the priority queue
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            //update the resultSet for destination vertex
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            //update the key[]
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //finish time of the algorithm
            double ftime = System.currentTimeMillis();
            //print the total time consumed by the algorithm
            System.out.println("Total runtime of Prim's Algorithm (Usin PQ): "+(ftime-stime)+" ms.");
            //print mst
            printMST(resultSet);
        }

        // Prim's Algorithm using Minheap
        public void primMH() {
            //start time
            double stime = System.currentTimeMillis();
            boolean[] inHeap = new boolean[vertices];
            ResultSet[] resultSet = new ResultSet[vertices];
            //keys[] used to store the key to know whether min hea update is required
            int[] key = new int[vertices];
            //create heapNode for all the vertices
            HeapNode[] heapNodes = new HeapNode[vertices];
            for (int i = 0; i < vertices; i++) {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].key = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
                resultSet[i].parent = -1;
                inHeap[i] = true;
                key[i] = Integer.MAX_VALUE;
            }

            //decrease the key for the first index
            heapNodes[0].key = 0;

            //add all the vertices to the MinHeap
            MinHeap minHeap = new MinHeap(vertices);
            //add all the vertices to priority queue
            for (int i = 0; i < vertices; i++) {
                minHeap.insert(heapNodes[i]);
            }

            //while minHeap is not empty
            while (!minHeap.isEmpty()) {
                //extract the min
                HeapNode extractedNode = minHeap.extractMin();

                //extracted vertex
                int extractedVertex = extractedNode.vertex;
                inHeap[extractedVertex] = false;

                //iterate through all the adjacent vertices
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is present in heap
                    if (inHeap[edge.destination]) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            decreaseKey(minHeap, newKey, destination);
                            //update the parent node for destination
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //finish time of the algorithm
            double ftime = System.currentTimeMillis();
            //print the total time consumed by the algorithm
            System.out.println("Total runtime of Prim's Algorithm (Usin Min Heap): "+(ftime-stime)+" ms.");
            //print mst
            printMST(resultSet);
        }

        public void decreaseKey(MinHeap minHeap, int newKey, int vertex) {

            //get the index which key's needs a decrease;
            int index = minHeap.indexes[vertex];

            //get the node and update its value
            HeapNode node = minHeap.mH[index];
            node.key = newKey;
            minHeap.bubbleUp(index);
        }

        public void printMST(ResultSet[] resultSet) {
            int total_min_weight = 0;
            System.out.println("Minimum Spanning Tree: ");
            for (int i = 1; i < vertices; i++) {
//                System.out.println("Edge: " + i + " - " + resultSet[i].parent
//                        + " weight: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight;
            }
            System.out.println("Total cost: " + total_min_weight);
        }


        // Kruskal's Algorithm
        public void kruskalMST() {
            // start time
            double stime = System.currentTimeMillis();
            String treeV=""; // this variable is used only in tracing
            LinkedList<Edge>[] allEdges = adjacencylist.clone(); // modified data type from ArrayList to LinkedList
            PriorityQueue<Edge> pq = new PriorityQueue<>(edges, Comparator.comparingInt(o -> o.weight));

            //add all the edges to priority queue, //sort the edges on weights
            for (int i = 0; i < allEdges.length; i++) {
                for (int j = 0; j < allEdges[i].size(); j++) {
                    pq.add(allEdges[i].get(j));
                }
            }
            
//                System.out.println("\nSorted list of Edges:");
//                for (Edge edge : pq) {
//                    System.out.println(edge.toString());
//            }
            //create a parent []
            int[] parent = new int[vertices];

            //makeset
            makeSet(parent);

            LinkedList<Edge> mst = new LinkedList<>();

            //process vertices - 1 edges
            int index = 0;
            while (index < vertices - 1 && !pq.isEmpty()) {
                Edge edge = pq.remove();
                //check if adding this edge creates a cycle
                int x_set = find(parent, edge.source);
                int y_set = find(parent, edge.destination);

                if (x_set == y_set) {
                    //ignore, will create cycle
                } else {
                    //add it to our final result
                    mst.add(edge);
                    treeV+=edge.toString()+"\n";
//                    System.out.println("\nTree Vertex:");
//                    System.out.println(treeV);
                    index++;
                    union(parent, x_set, y_set);
                }
//                System.out.println("Sorted list of Edges:");
//                for (Edge e : pq) {
//                    System.out.println(e.toString());
//                }
            }
            
            //finish time of the algorithm
            double ftime = System.currentTimeMillis();
            //print the total time consumed by the algorithm
            System.out.println("Total runtime of Kruskal's Algorithm: "+(ftime-stime)+" ms.");
            //print MST
//            System.out.println("Minimum Spanning Tree: ");
            printGraph(mst);
        }

        public void makeSet(int[] parent) {
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i < vertices; i++) {
                parent[i] = i;
            }
        }

        public int find(int[] parent, int vertex) {
            //chain of parent pointers from x upwards through the tree
            // until an element is reached whose parent is itself
            if (parent[vertex] != vertex) {
                return find(parent, parent[vertex]);
            };
            return vertex;
        }

        public void union(int[] parent, int x, int y) {
            int x_set_parent = find(parent, x);
            int y_set_parent = find(parent, y);
            //make x as parent of y
            parent[y_set_parent] = x_set_parent;
        }

        public void printGraph(LinkedList<Edge> edgeList) {
            int cost = 0;
            for (int i = 0; i < edgeList.size(); i++) {
                Edge edge = edgeList.get(i);
//                System.out.println("Edge-" + i + " source: " + edge.source
//                        + " destination: " + edge.destination
//                        + " weight: " + edge.weight);
                cost += edge.weight;
            }
            System.out.println("Minimum Spanning Tree Cost = " + cost);
        }

        //----------------------------------------------------------------------
        public void make_graph(Graph graph) {
            // instance of Random class
            Random random = new Random();
            // ensure that all vertices are connected
            for (int i = 0; i < vertices-1; i++) {
                    int w = random.nextInt(10) + 1;
                    addEdge(i,i+1,w);
                
            }
            
            // generate random graph with the remaining edges
            int remaning = edges- (vertices-1);
            
            for (int i = 0; i < remaning; i++) {
                int source = random.nextInt(graph.vertices);
                int dest = random.nextInt(graph.vertices);
                if (dest == source || isConnected(source, dest, graph.adjacencylist)) { // to avoid self loops and duplicate edges
                    i--;
                    continue;
                }
                // generate random weights in range 0 to 10
                int weight = random.nextInt(10) + 1;
                // add edge to the graph
                addEdge(source, dest, weight);
//            System.out.println(source + " - " + dest+": "+weight);
            }
//        printGraph(allEdges);
        }

        // checks if the edge is already existed
        public boolean isConnected(int src, int dest, LinkedList<Edge>[] allEdges) {
        for (LinkedList<Edge> i : allEdges) {
            for (Edge edge : i) {
                if ((edge.source == src && edge.destination == dest) || (edge.source == dest && edge.destination == src)) {
                    return true;
                }
            }
        }
        return false;
    }
        
    }

    static class MinHeap {

        int capacity;
        int currentSize;
        HeapNode[] mH;
        int[] indexes; //will be used to decrease the key

        public MinHeap(int capacity) {
            this.capacity = capacity;
            mH = new HeapNode[capacity + 1];
            indexes = new int[capacity];
            mH[0] = new HeapNode();
            mH[0].key = Integer.MIN_VALUE;
            mH[0].vertex = -1;
            currentSize = 0;
        }

        public void display() {
            for (int i = 0; i <= currentSize; i++) {
                System.out.println(" " + mH[i].vertex + "   key   " + mH[i].key);
            }
            System.out.println("________________________");
        }

        public void insert(HeapNode x) {
            currentSize++;
            int idx = currentSize;
            mH[idx] = x;
            indexes[x.vertex] = idx;
            bubbleUp(idx);
        }

        public void bubbleUp(int pos) {
            int parentIdx = pos / 2;
            int currentIdx = pos;
            while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
                HeapNode currentNode = mH[currentIdx];
                HeapNode parentNode = mH[parentIdx];

                //swap the positions
                indexes[currentNode.vertex] = parentIdx;
                indexes[parentNode.vertex] = currentIdx;
                swap(currentIdx, parentIdx);
                currentIdx = parentIdx;
                parentIdx = parentIdx / 2;
            }
        }

        public HeapNode extractMin() {
            HeapNode min = mH[1];
            HeapNode lastNode = mH[currentSize];
//            update the indexes[] and move the last node to the top
            indexes[lastNode.vertex] = 1;
            mH[1] = lastNode;
            mH[currentSize] = null;
            sinkDown(1);
            currentSize--;
            return min;
        }

        public void sinkDown(int k) {
            int smallest = k;
            int leftChildIdx = 2 * k;
            int rightChildIdx = 2 * k + 1;
            if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
                smallest = leftChildIdx;
            }
            if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
                smallest = rightChildIdx;
            }
            if (smallest != k) {

                HeapNode smallestNode = mH[smallest];
                HeapNode kNode = mH[k];

                //swap the positions
                indexes[smallestNode.vertex] = k;
                indexes[kNode.vertex] = smallest;
                swap(k, smallest);
                sinkDown(smallest);
            }
        }

        public void swap(int a, int b) {
            HeapNode temp = mH[a];
            mH[a] = mH[b];
            mH[b] = temp;
        }

        public boolean isEmpty() {
            return currentSize == 0;
        }

        public int heapSize() {
            return currentSize;
        }
    }

    public static void main(String[] args) {
        int n=0,m=0;
        Scanner in = new Scanner(System.in);
        System.out.println("\t\t*** Runtime of Different Minimum Spanning Tree Algorithms ***\n\t1- Kruskal's Algorithm& Prim's Algorithm (based on Priority Queue)"
                + "\n\t2- Prim's Algorithm (based on Min Heap)& Prim's Algorithm(based on Priority Queue)");
        System.out.print("> Enter your choice (1 or 2): ");
        int choice = in.nextInt();
        System.out.println("> Available cases (where n represents # of vertices and m represents # of edges: ");
        System.out.println(" 1-  n=1,000 - m=10,000");
        System.out.println(" 2-  n=1,000 - m=15,000");
        System.out.println(" 3-  n=1,000 - m=25,000");
        System.out.println(" 4-  n=5,000 - m=15,000");
        System.out.println(" 5-  n=5,000 - m=25,000");
        System.out.println(" 6- n=10,000 - m=15,000");
        System.out.println(" 7  n=10,000 - m=25,000");
        System.out.println(" 8- n=20,000 - m=200,000");
        System.out.println(" 9- n=20,000 - m=300,000");
        System.out.println("10- n=50,000 - m=10,000,000");
        System.out.print("> Enter a case to test: ");
        int c = in.nextInt();
        switch (c) {
            case 1: {
                n=1000; m=10000;
            }
            break;
            case 2: {
                n=1000; m=15000;
            }
            break;
            case 3: {
                n=1000; m=25000;
            }
            break;
            case 4: {
                n=5000; m=15000;
            }
            break;
            case 5: {
                n=5000; m=25000;
            }
            break;
            case 6: {
                n=10000; m=15000;
            }
            break;
            case 7: {
                n=10000; m=25000;
            }
            break;
            case 8: {
                n=20000; m=200000;
            }
            break;
            case 9: {
                n=20000; m=300000;
            }
            break;
            case 10: {
                n=50000; m=1000000;
            }
            break;
            default:
                System.out.println("Invalid input!");
        }
        Graph graph = new Graph(n, m);
        graph.make_graph(graph);
        // to perform Task 2 
        if (choice == 1) {
            graph.kruskalMST();
            graph.primPQ();
        } 
        // to perform Task 3
        else if (choice == 2) {
            graph.primMH();
            graph.primPQ();
        }
        else{
            System.out.println("Invalid input!");
        }
    }

}


