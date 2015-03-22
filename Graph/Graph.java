package Final;

// UMUC CMSC 350 
// Class Graph - Defines an undirected graph
// Adapted by Ioan from:
// Liang - Introduction to Java Programming, 9th Edition (Code Examples of Chapter 30 Graphs and Applications)
// Source code of the examples available at: 
// http://www.cs.armstrong.edu/liang/intro9e/examplesource.html

import java.util.*;

public class Graph<V> {
  protected List<V> vertices = new ArrayList<V>(); // Store vertices
  protected List<List<Integer>> neighbors = new ArrayList<List<Integer>>(); // Adjacency lists

  /** Construct an empty graph */
  protected Graph() {
  }
  
  /** Construct a graph from edges and vertices stored in arrays */
  protected Graph(int[][] edges, V[] vertices) {
    for (int i = 0; i < vertices.length; i++)
      this.vertices.add(vertices[i]);
    
    createAdjacencyLists(edges, vertices.length);
  }

  /** Construct a graph from edges and vertices stored in List */
  protected Graph(List<Edge> edges, List<V> vertices) {
  for (int i = 0; i < vertices.size(); i++)
    this.vertices.add(vertices.get(i));
      
  createAdjacencyLists(edges, vertices.size());
  }

  /** Construct a graph for integer vertices 0, 1, 2 and edge list */
  protected Graph(List<Edge> edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      vertices.add((V)(new Integer(i))); // vertices is {0, 1, ...}
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Construct a graph from integer vertices 0, 1, and edge array */
  protected Graph(int[][] edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      vertices.add((V)(new Integer(i))); // vertices is {0, 1, ...}
    
    createAdjacencyLists(edges, numberOfVertices);
  }
  
  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(int[][] edges, int numberOfVertices) {
    // Create a linked list
    for (int i = 0; i < numberOfVertices; i++) {
      neighbors.add(new ArrayList<Integer>());
    }

    for (int i = 0; i < edges.length; i++) {
      int u = edges[i][0];
      int v = edges[i][1];
      neighbors.get(u).add(v);
    }
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(List<Edge> edges, int numberOfVertices) {
    // Create a linked list for each vertex
    for (int i = 0; i < numberOfVertices; i++) {
      neighbors.add(new ArrayList<Integer>());
    }

    for (Edge edge: edges) {
      neighbors.get(edge.u).add(edge.v);
    }
  }

  /** Return the number of vertices in the graph */
  public int getSize() {
    return vertices.size();
  }

  /** Return the vertices in the graph */
  public List<V> getVertices() {
    return vertices;
  }

  /** Return the object for the specified vertex */
  public V getVertex(int index) {
    return vertices.get(index);
  }

  /** Return the index for the specified vertex object */
  public int getIndex(V v) {
    return vertices.indexOf(v);
  }

  /** Return the neighbors of the specified vertex */
  public List<Integer> getNeighbors(int index) {
    return neighbors.get(index);
  }

  /** Return the degree for a specified vertex */
  public int getDegree(int v) {
    return neighbors.get(v).size();
  }

  /** Print the edges */
  public void printEdges() {
    for (int u = 0; u < neighbors.size(); u++) {
      System.out.print(getVertex(u) + " (" + u + "): ");
      for (int j = 0; j < neighbors.get(u).size(); j++) {
        System.out.print("(" + u + ", " +
          neighbors.get(u).get(j) + ") ");
      }
      System.out.println();
    }
  }

  /** Clear graph */
  public void clear() {
    vertices.clear();
    neighbors.clear();
  }
  
  /** Add a vertex to the graph */  
  public void addVertex(V vertex) {
    vertices.add(vertex);
    neighbors.add(new ArrayList<Integer>());
  }

  /** Add an edge to the graph */  
  public void addEdge(int u, int v) {
    neighbors.get(u).add(v);
    neighbors.get(v).add(u);
  }
  
  /** DFS Graph Traversal */
  /** Obtain a DFS List of vertices starting from vertex v */
  /** Original code modified to return List (instead of Tree)*/
  public List<Integer> dfs(int v) {
    List<Integer> searchOrder = new ArrayList<Integer>();
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++)
      parent[i] = -1; // Initialize parent[i] to -1

    // Mark visited vertices
    boolean[] isVisited = new boolean[vertices.size()];

    // Recursively search
    dfs(v, parent, searchOrder, isVisited);

    // Return a search tree
    return searchOrder;
  }

  /** Recursive method for DFS search */
  private void dfs(int v, int[] parent, List<Integer> searchOrder, boolean[] isVisited) {
    // Store the visited vertex
    searchOrder.add(v);
    isVisited[v] = true; // Vertex v visited

    for (int i : neighbors.get(v)) {
      if (!isVisited[i]) {
        parent[i] = v; // The parent of vertex i is v
        dfs(i, parent, searchOrder, isVisited); // Recursive search
      }
    }
  }
  
  /** BFS Graph Traversal */
  /** Obtain a BFS List of vertices starting from vertex v */
  /** Original code modified to return List (instead of Tree)*/
  public List<Integer> bfs(int v) {
    List<Integer> searchOrder = new ArrayList<Integer>();
    int[] parent = new int[vertices.size()];
    for (int i = 0; i < parent.length; i++)
      parent[i] = -1; // Initialize parent[i] to -1

    java.util.LinkedList<Integer> queue =
      new java.util.LinkedList<Integer>(); // list used as a queue
    boolean[] isVisited = new boolean[vertices.size()];
    queue.offer(v); // Enqueue v
    isVisited[v] = true; // Mark it visited

    while (!queue.isEmpty()) {
      int u = queue.poll(); // Dequeue to u
      searchOrder.add(u); // u searched
      for (int w : neighbors.get(u)) {
        if (!isVisited[w]) {
          queue.offer(w); // Enqueue w
          parent[w] = u; // The parent of w is u
          isVisited[w] = true; // Mark it visited
        }
      }
    }
    return searchOrder;
  }
 
  /** Edge inner class inside the Graph class */
  public static class Edge {
    public int u; // Starting vertex of the edge
    public int v; // Ending vertex of the edge

    /** Construct an edge for (u, v) */
    public Edge(int u, int v) {
      this.u = u;
      this.v = v;
    }
  }
}