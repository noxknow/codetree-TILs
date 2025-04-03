import java.util.*;
import java.io.*;

public class Main {

    static class Package implements Comparable<Package> {

        int id, revenue, destination, profit;

        Package(int id, int revenue, int destination, int profit) {
            this.id = id;
            this.revenue = revenue;
            this.destination = destination;
            this.profit = profit;
        }

        @Override
        public int compareTo(Package o) {
            if (this.profit == o.profit) {
                return this.id - o.id;
            }

            return o.profit - this.profit;
        }
    }

    static class Node implements Comparable<Node> {

        int to, dist;

        Node (int to, int dist) {
            this.to = to;
            this.dist = dist;
        }

        @Override
        public int compareTo(Node o) {
            return this.dist - o.dist;
        }
    }

    final static int INF = 123456789;
    final static int MAX_ID = 30002;

    static BufferedReader br;
    static boolean[] isMade = new boolean[MAX_ID];
    static boolean[] isCancel = new boolean[MAX_ID];
    static int startNode, q, n, m;
    static List<List<Node>> graphs;
    static boolean[] visited;
    static int[] distances;
    static PriorityQueue<Package> packages;
    public static void main(String[] args) throws Exception {

      br = new BufferedReader(new InputStreamReader(System.in));

        q = stoi(br.readLine());
        packages = new PriorityQueue<>();
        startNode = 0;
        for (int i = 0; i < q; i++) {

            StringTokenizer inputs = new StringTokenizer(br.readLine());
            int command = stoi(inputs.nextToken());

            if (command == 100) {
                buildLandMark(inputs);
                dijkstra();
            } else if (command == 200) {
                int id = stoi(inputs.nextToken());
                int revenue = stoi(inputs.nextToken());
                int destination = stoi(inputs.nextToken());

                addPackage(id, revenue, destination);
            } else if (command == 300) {
                deleteId(inputs);
            } else if (command == 400) {
                System.out.println(sellPackage());
            } else if (command == 500) {
                changeStart(inputs);
            }
        }

        br.close();
    }

    private static void buildLandMark(StringTokenizer inputs) {

        n = stoi(inputs.nextToken());
        m = stoi(inputs.nextToken());

        graphs = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graphs.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int from = stoi(inputs.nextToken());
            int to = stoi(inputs.nextToken());
            int dist = stoi(inputs.nextToken());

            graphs.get(from).add(new Node(to, dist));
            graphs.get(to).add(new Node(from, dist));
        }
    }

    private static void dijkstra() {

        visited = new boolean[n];
        distances = new int[n];
        Arrays.fill(distances, INF);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startNode, 0));
        distances[startNode] = 0;

        while (!pq.isEmpty()) {

            Node curNode = pq.poll();
            visited[curNode.to] = true;

            for (Node nextNode : graphs.get(curNode.to)) {
                if (!visited[nextNode.to] && distances[nextNode.to] > distances[curNode.to] + nextNode.dist) {
                    distances[nextNode.to] = distances[curNode.to] + nextNode.dist;
                    pq.add(new Node(nextNode.to, distances[nextNode.to]));
                }
            }
        }
    }

    private static void addPackage(int id, int revenue, int destination) {

        int profit = revenue - distances[destination];
        isMade[id] = true;

        packages.add(new Package(id, revenue, destination, profit));
    }

    private static void deleteId(StringTokenizer inputs) {

        int id = stoi(inputs.nextToken());

        if (isMade[id]) {
            isCancel[id] = true;
        }
    }

    private static int sellPackage() {

        while (!packages.isEmpty()) {

            Package p = packages.peek();
            if (p.profit < 0) {
                break;
            }

            p = packages.poll();
            if (!isCancel[p.id]) {
                return p.id;
            }
        }

        return -1;
    }

    private static void changeStart(StringTokenizer inputs) {

        startNode = stoi(inputs.nextToken());
        dijkstra();
        List<Package> newPackages = new ArrayList<>();

        while (!packages.isEmpty()) {
            newPackages.add(packages.poll());
        }

        for (Package newPackage : newPackages) {
            addPackage(newPackage.id, newPackage.revenue, newPackage.destination);
        }
    }

    private static int stoi(String input) {
        return Integer.parseInt(input);
    }
}