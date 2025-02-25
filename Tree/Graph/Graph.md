## **📚 Learning Path for Graph Data Structure (Graduate Level)**
Graphs are **fundamental** in Computer Science and are widely used in real-world applications such as **social networks, route planning, recommendation systems, and circuit design**. This structured learning guide will take you **from basics to advanced graph theory**, covering **concepts, algorithms, and applications**.

---

## **1️⃣ Fundamentals of Graph Theory**
### **What is a Graph?**
A **Graph** is a data structure that consists of:
- **Nodes (Vertices)**: Represent entities.
- **Edges**: Connections between nodes.

Graphs can be **Directed or Undirected**, **Weighted or Unweighted**.

### **Types of Graphs**
✔ **Undirected Graph** – Edges have no direction.  
✔ **Directed Graph (Digraph)** – Edges have a specific direction.  
✔ **Weighted Graph** – Edges have weights (distances, costs).  
✔ **Unweighted Graph** – Edges have no weight.  
✔ **Cyclic Graph** – Contains at least one cycle.  
✔ **Acyclic Graph** – No cycles.  
✔ **Connected Graph** – Every node is reachable from any other node.  
✔ **Disconnected Graph** – Some nodes are isolated.  
✔ **Bipartite Graph** – Vertices can be divided into two independent sets.

### **Mathematical Representation of Graphs**
Graphs are represented as:
1. **Adjacency Matrix** – A **2D array** where `matrix[i][j] = 1` if an edge exists.
2. **Adjacency List** – A **hashmap or list of lists** where each vertex points to its neighbors.
3. **Edge List** – A list of **(u, v, weight)** pairs.

---

## **2️⃣ Graph Representation in Code**
### **Adjacency Matrix Implementation (Python)**
```python
class Graph:
    def __init__(self, size):
        self.matrix = [[0] * size for _ in range(size)]
        self.size = size

    def add_edge(self, u, v):
        self.matrix[u][v] = 1
        self.matrix[v][u] = 1  # Remove for Directed Graph

    def display(self):
        for row in self.matrix:
            print(row)

g = Graph(4)
g.add_edge(0, 1)
g.add_edge(1, 2)
g.display()
```
✅ **Time Complexity:** `O(1)` for edge lookup, but `O(V^2)` space.

### **Adjacency List Implementation (Python)**
```python
from collections import defaultdict

class Graph:
    def __init__(self):
        self.graph = defaultdict(list)

    def add_edge(self, u, v):
        self.graph[u].append(v)
        self.graph[v].append(u)  # Remove for Directed Graph

    def display(self):
        for node in self.graph:
            print(f"{node} -> {self.graph[node]}")

g = Graph()
g.add_edge(0, 1)
g.add_edge(1, 2)
g.display()
```
✅ **Time Complexity:** `O(1)` for edge addition, `O(V+E)` traversal.

---

## **3️⃣ Graph Traversal Algorithms**
### **1. Breadth-First Search (BFS)**
**Algorithm:**
- Uses **Queue (FIFO)**
- Explores neighbors level by level.
- Good for **shortest path (unweighted graphs).**

✅ **Python Implementation**
```python
from collections import deque

def bfs(graph, start):
    visited = set()
    queue = deque([start])

    while queue:
        node = queue.popleft()
        if node not in visited:
            print(node, end=" ")
            visited.add(node)
            queue.extend(graph[node])

graph = {
    0: [1, 2],
    1: [2, 3],
    2: [3],
    3: []
}

bfs(graph, 0)
```
✅ **Time Complexity:** `O(V + E)`

### **2. Depth-First Search (DFS)**
**Algorithm:**
- Uses **Stack (LIFO)**
- Explores deep paths first.
- Good for **detecting cycles, topological sorting, connected components**.

✅ **Python Implementation**
```python
def dfs(graph, node, visited=set()):
    if node not in visited:
        print(node, end=" ")
        visited.add(node)
        for neighbor in graph[node]:
            dfs(graph, neighbor, visited)

graph = {
    0: [1, 2],
    1: [2, 3],
    2: [3],
    3: []
}

dfs(graph, 0)
```
✅ **Time Complexity:** `O(V + E)`

---

## **4️⃣ Shortest Path Algorithms**
### **1. Dijkstra’s Algorithm (Single Source Shortest Path)**
- **Greedy Algorithm**
- Uses **Priority Queue (Min Heap)**
- Works for **Weighted Graphs** (no negative weights).
```python
import heapq

def dijkstra(graph, start):
    pq = [(0, start)]  # (distance, node)
    distances = {node: float('inf') for node in graph}
    distances[start] = 0

    while pq:
        dist, node = heapq.heappop(pq)
        for neighbor, weight in graph[node]:
            new_dist = dist + weight
            if new_dist < distances[neighbor]:
                distances[neighbor] = new_dist
                heapq.heappush(pq, (new_dist, neighbor))

    return distances

graph = {
    0: [(1, 4), (2, 1)],
    1: [(3, 1)],
    2: [(1, 2), (3, 5)],
    3: []
}
print(dijkstra(graph, 0))
```
✅ **Time Complexity:** `O((V + E) log V)`

### **2. Bellman-Ford Algorithm**
- Handles **negative weights**.
- Slower than Dijkstra.
- **Time Complexity:** `O(VE)`

---

## **5️⃣ Advanced Graph Algorithms**
### **1. Floyd-Warshall (All-Pairs Shortest Path)**
- **Dynamic Programming Approach**
- Time Complexity: `O(V^3)`

### **2. Kruskal’s Algorithm (Minimum Spanning Tree)**
- Uses **Disjoint Set Union (DSU)**
- Time Complexity: `O(E log E)`

### **3. Prim’s Algorithm (Minimum Spanning Tree)**
- Uses **Priority Queue (Min Heap)**
- Time Complexity: `O(E log V)`

### **4. Topological Sorting (For Directed Acyclic Graphs - DAGs)**
- Uses **DFS**
- Used in **Task Scheduling**
- **Time Complexity:** `O(V + E)`

---

## **6️⃣ Graph Theory Applications**
📌 **Real-World Applications of Graphs:**
✔ **Social Networks** (Facebook, LinkedIn Graphs)  
✔ **Google Maps** (Shortest Paths, Routing)  
✔ **Web Crawlers** (PageRank Algorithm)  
✔ **Recommendation Systems** (Amazon, Netflix)  
✔ **Computer Networks** (Packet Routing)  
✔ **AI and Machine Learning** (Graph Neural Networks)  

---

## **7️⃣ LeetCode Graph Problems for Practice**
🔹 **Beginner**
- [733. Flood Fill](https://leetcode.com/problems/flood-fill/)
- [200. Number of Islands](https://leetcode.com/problems/number-of-islands/)
- [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/)

🔹 **Intermediate**
- [207. Course Schedule (Topological Sort)](https://leetcode.com/problems/course-schedule/)
- [133. Clone Graph](https://leetcode.com/problems/clone-graph/)
- [695. Max Area of Island](https://leetcode.com/problems/max-area-of-island/)

🔹 **Advanced**
- [787. Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
- [399. Evaluate Division](https://leetcode.com/problems/evaluate-division/)
- [684. Redundant Connection](https://leetcode.com/problems/redundant-connection/)

---

## **📌 Summary**
✅ **Learn Graph Basics** → **Implement Graph Representations** → **Practice BFS, DFS**  
✅ **Master Shortest Path & MST Algorithms** → **Solve Real-World Problems**  
✅ **Apply Advanced Graph Concepts in Research & Competitive Programming**  

🚀 **Happy Graph Learning!** 🎯 Let me know if you need any specific concepts explained in detail! 😊