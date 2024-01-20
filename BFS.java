import java.util.*;
class BFS {
    private int V;

    private LinkedList<Integer> adj[];

    BFS(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adj[i] = new LinkedList();
    }
    void BFS(int c)
    {

        boolean visited[] = new boolean[V];

        LinkedList<Integer> queue
                = new LinkedList<Integer>();

        visited[c] = true;
        queue.add(c);

        while (queue.size() != 0) {

            c = queue.poll();
            System.out.print("visited component" + c);
            Iterator<Integer> i = adj[c].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }
}

