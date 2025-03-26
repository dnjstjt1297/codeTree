import java.util.*;
import java.io.*;

class Node{
    int v;
    int cost;
    public Node(int v, int cost){
        this.v = v;
        this.cost = cost;
    }
}
class Item{
    int id;
    int revenue;
    int dest;
    public Item(int id, int revenue, int dest){
        this.id = id;
        this.revenue = revenue;
        this.dest = dest;
    }

    public String toString(){
        return id + " " + revenue + " " + dest;
    }
}

public class Main {
    static final int INF = 1000000007;

    static int n,m;
    static ArrayList<Node>[] graph;
    static PriorityQueue<Item> items;
    static int[] degree = new int[n];
    static ArrayList<Integer> answer = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine());
        for(int i = 0; i<q;i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            if(num == 100){
                n = Integer.parseInt(st.nextToken());
                m = Integer.parseInt(st.nextToken());
                int[][] edges = new int[m][3];
                for(int j = 0; j<m; j++){
                    int v = Integer.parseInt(st.nextToken());
                    int u = Integer.parseInt(st.nextToken());
                    int w = Integer.parseInt(st.nextToken());
                    edges[j][0] = v;
                    edges[j][1] = u;
                    edges[j][2] = w;
                }
                init(edges);
                dijkstra(0);
            }
            else if(num==200) {
                int id = Integer.parseInt(st.nextToken());
                int revenue = Integer.parseInt(st.nextToken());
                int dest = Integer.parseInt(st.nextToken());
                addItem(id, revenue, dest);
            }
            else if(num==300) {
                int id = Integer.parseInt(st.nextToken());
                deleteItem(id);
            }
            else if(num==400) {
                findBestItem();
            }
            else if(num==500) {
                int s = Integer.parseInt(st.nextToken());
                moveStartPoint(s);
            }
        }
        for(int ans : answer){
            System.out.println(ans);
        }
    }

    public static void dijkstra(int start){
        for(int i = 0; i<n; i++) degree[i] = INF;
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.cost - o2.cost;
            }
        });

        pq.add(new Node(start,0));
        degree[start] = 0;

        while(!pq.isEmpty()){
            Node cur = pq.poll();
            for(Node next : graph[cur.v]){
                int newDegree = degree[cur.v] + next.cost;

                if(newDegree < degree[next.v]){
                    degree[next.v] = newDegree;
                    pq.add(new Node(next.v,newDegree));
                }
            }
        }
    }

    public static void init(int[][] edges){
        graph = new ArrayList[n];
        for(int i = 0; i<n; i++) graph[i] = new ArrayList<>();

        for(int i = 0; i<m; i++){
            graph[edges[i][0]].add(new Node(edges[i][1],edges[i][2]));
            graph[edges[i][1]].add(new Node(edges[i][0],edges[i][2]));
        }

        items = new PriorityQueue<>(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int p1 = o1.revenue - degree[o1.dest];
                int p2 = o2.revenue - degree[o2.dest];
                if(p1 == p2){
                    return o1.id- o2.id;
                }
                return p2-p1;
            }
        });
        degree = new int[n];
    }

    public static void addItem(int id, int revenue, int dest){
        items.add(new Item(id,revenue,dest));
    }

    public static void deleteItem(int id){
        ArrayList<Item> tmp = new ArrayList<>();
        while(!items.isEmpty()){
            Item cur = items.poll();
            if(cur.id==id) break;
            tmp.add(cur);
        }
        items.addAll(tmp);
    }

    public static void findBestItem(){
        ArrayList<Item> tmp = new ArrayList<>();
        int addId = -1;
        while(!items.isEmpty()) {
            Item cur = items.poll();
            if(degree[cur.dest] != INF && degree[cur.dest]<=cur.revenue){
                addId = cur.id;
                break;
            }
            tmp.add(cur);
        }
        answer.add(addId);
        items.addAll(tmp);
    }

    public static void moveStartPoint(int s){
        dijkstra(s);
        ArrayList<Item> tmp = new ArrayList<>();
        while(!items.isEmpty()){
            Item cur = items.poll();
            tmp.add(cur);
        }
        items.addAll(tmp);
    }

}