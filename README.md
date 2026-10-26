# Assignment 3. Maze Escape

Student ID: SE-2510  
Name: Anuar Amangeldyyev  
Group: SE-2510  
Maze size (rows x columns): 8 x 10

## Program output

The 13 lines your program prints on your data (`java -cp out Main`):

    Broken: 79
    Kept: 63
    Last broken wall: 100
    MST cost: 25437
    Heaviest broken wall: 776
    Path length: 24
    DFS visited: 49
    Dead ends: 24
    Zones: 11
    Largest zone: 16
    Is DAG: 0
    Flow stages: 9
    Fastest route cost: 2

My maze:

![maze](output/maze.png)

## Questions

### Mission 0. What do find and union do, and what makes them fast in your code?

`find` returns the representative (root) of the set containing an element. `union` joins two different sets and returns false if they are already connected. My implementation uses union by size and path compression, making operations amortized almost constant time, O(α(n))

### Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?

A maze has R*C cells, and all cells must remain connected. Breaking a wall only when it joins two different components creates a spanning tree, which has exactly R*C - 1 edges. The grid starts with R(C-1) + C(R-1) walls, so the number kept is R(C-1) + C(R-1) - (R*C - 1) = (R-1)(C-1)

### Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:

Both Kruskal and Prim find a minimum spanning tree of the same connected, weighted graph. Kruskal sorts edges by weight and adds an edge only when it does not create a cycle, while Prim grows a tree by choosing the cheapest edge that connects it to an unvisited vertex. Since both algorithms find a minimum spanning tree, their total costs are equal

My MST cost: 25437

### Mission 3. Why is there exactly one path between two cells? How did you avoid a stack overflow on a huge maze?

The maze is a spanning tree: it is connected and contains no cycles. Therefore, exactly one simple path exists between any two cells. I implemented DFS iteratively with arrays that store the stack of cells and each cell's next direction, instead of using recursive calls, so a large maze does not overflow the call stack

### Mission 4. Why does the second pass of Kosaraju go on the reversed graph? Why do the slides have no cycle exactly when Zones equals the number of cells?

The first DFS records vertices in finishing order. Processing them in reverse finishing order on the reversed graph lets each DFS identify one complete strongly connected component without merging it with another component. Because there are no self-loops, a directed cycle involving multiple cells creates a component containing more than one cell; therefore, the slides have no cycles exactly when every cell is its own zone and Zones equals the number of cells

### Mission 5. Why is it enough to go through the zones in the order of their numbers? What does this pass cost?

Kosaraju numbers the zones in topological order, so every edge between different zones goes from a lower-numbered zone to a higher-numbered zone. Processing the zones in that order ensures that all possible incoming updates have been considered before a zone is processed, allowing both the longest chain and the cheapest route to be calculated without a priority queue. The route calculation takes expected O(m + z) time, where m is the number of slides and z is the number of zones, assuming expected constant-time HashMap operations

## AI use

ChatGPT was used to help explain the tasks and the ideas that were required in the assignment

## Timing (Mission 7)

Milliseconds, 5 runs each, from `java -cp out Bench`.

| maze | Kruskal | Prim |
|---|---:|---:|
| 100 x 100 | 13 ms average | 8 ms average |
| 300 x 300 | 67 ms average | 58 ms average |
| 1000 x 1000 | 1117 ms average | 1824 ms average |

The running time increases as the maze gets larger because the algorithms process more vertices and walls. Prim was faster in these measurements for the 100 x 100 and 300 x 300 mazes, while Kruskal was faster for the 1000 x 1000 maze. These are measurements from one computer and can vary between runs

## Complexity

Here, n is the number of cells and m is the number of walls or slides, as applicable

| algorithm | time | space | assumptions |
|---|---|---|---|
| DSU find and union | O(α(n)) amortized per operation | O(n) | Union by size and path compression |
| Kruskal | O(m log m + m α(n)) | O(n + m) | Sorting the walls dominates the running time |
| Prim | O(m log m) | O(n + m) | Adjacency lists and a priority queue |
| DFS | O(n + m) | O(n + m) | Iterative DFS; graph storage is included |
| Kosaraju | O(n + m) | O(n + m) | Two iterative DFS passes on the graph and its reverse |
| Route in the zone graph | Expected O(m + z) | O(m + z) | z is the number of zones; HashMap operations take expected O(1) time |

## Before you submit

- [ ] `data/walls.txt`, `data/slides.txt` and `output/maze.png` are in the repository
- [ ] `java tools/Check.java` shows all examples right
- [ ] This README is filled in, the program output and the section AI use included
- [ ] The work is pushed to main (at least one commit per mission) and the repository is public
- [ ] The link is attached in Moodle
