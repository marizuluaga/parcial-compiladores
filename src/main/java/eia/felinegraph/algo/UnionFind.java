package eia.felinegraph.algo;


public final class UnionFind {

    private final int[] parent;
    private final int[] size;
    private int componentCount;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        componentCount = n;
    }

    
    public int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
       
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    /**
     * Unions the sets containing {@code a} and {@code b}.
     *
     * @return {@code true} if they were in different sets (a real merge happened),
     *         {@code false} if they were already in the same set.
     */
    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) {
            return false;
        }
        // union by size: attach the smaller tree under the larger one
        if (size[rootA] < size[rootB]) {
            int tmp = rootA;
            rootA = rootB;
            rootB = tmp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        componentCount--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int componentCount() {
        return componentCount;
    }
}
