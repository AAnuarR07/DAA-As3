/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int exit = n - 1;
        int[][] graph = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int[] neighbours : graph) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] stackCell = new int[n];
        int[] stackDirection = new int[n];
        java.util.Arrays.fill(parent, -1);

        int top = 0;
        stackCell[0] = 0;
        stackDirection[0] = 0;
        visited[0] = true;
        int visitedCount = 1;

        while (top >= 0 && !visited[exit]) {
            int cell = stackCell[top];

            if (stackDirection[top] == 4) {
                top--;
                continue;
            }

            int direction = stackDirection[top]++;
            int next;

            if (direction == 0) {
                next = cell - cols;
                if (next < 0) continue;
            } else if (direction == 1) {
                next = cell + 1;
                if (cell % cols == cols - 1) continue;
            } else if (direction == 2) {
                next = cell + cols;
                if (next >= n) continue;
            } else {
                next = cell - 1;
                if (cell % cols == 0) continue;
            }

            boolean open = false;
            for (int neighbour : graph[cell]) {
                if (neighbour == next) {
                    open = true;
                    break;
                }
            }

            if (open && !visited[next]) {
                visited[next] = true;
                parent[next] = cell;
                visitedCount++;

                top++;
                stackCell[top] = next;
                stackDirection[top] = 0;
            }
        }

        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }

        int length = 1;
        for (int cell = exit; cell != 0; cell = parent[cell]) {
            length++;
        }

        int[] path = new int[length];
        int cell = exit;

        for (int i = length - 1; i >= 0; i--) {
            path[i] = cell;
            cell = parent[cell];
        }

        return new Result(path, visitedCount, deadEnds);
    }
}