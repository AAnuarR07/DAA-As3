import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int k = 0; k < w.count(); k++) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count++] = k;

                if (count == w.cells() - 1) {
                    break;
                }
            }
        }

        return Arrays.copyOf(broken, count);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int n = w.cells();
        int m = w.count();

        Integer[] order = new Integer[m];
        for (int i = 0; i < m; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (i, j) -> Integer.compare(w.cost()[i], w.cost()[j]));

        DisjointSet dsu = new DisjointSet(n);
        int[] broken = new int[n - 1];
        int count = 0;

        for (int k : order) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count++] = k;

                if (count == n - 1) {
                    break;
                }
            }
        }

        return Arrays.copyOf(broken, count);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        int m = w.count();

        int[] degree = new int[n];

        for (int k = 0; k < m; k++) {
            degree[w.a()[k]]++;
            degree[w.b()[k]]++;
        }

        int[][] incident = new int[n][];
        for (int i = 0; i < n; i++) {
            incident[i] = new int[degree[i]];
        }

        int[] filled = new int[n];

        for (int k = 0; k < m; k++) {
            int a = w.a()[k];
            int b = w.b()[k];

            incident[a][filled[a]++] = k;
            incident[b][filled[b]++] = k;
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(
                (i, j) -> Integer.compare(w.cost()[i], w.cost()[j])
        );

        boolean[] visited = new boolean[n];
        visited[0] = true;

        for (int k : incident[0]) {
            queue.offer(k);
        }

        int visitedCount = 1;
        long totalCost = 0;

        while (!queue.isEmpty() && visitedCount < n) {
            int k = queue.poll();

            int a = w.a()[k];
            int b = w.b()[k];

            if (visited[a] && visited[b]) {
                continue;
            }

            int next = visited[a] ? b : a;

            visited[next] = true;
            visitedCount++;
            totalCost += w.cost()[k];

            for (int edge : incident[next]) {
                int other = w.a()[edge] == next ? w.b()[edge] : w.a()[edge];

                if (!visited[other]) {
                    queue.offer(edge);
                }
            }
        }

        if (visitedCount != n) {
            throw new IllegalStateException("The maze graph is not connected");
        }

        return totalCost;
    }
}