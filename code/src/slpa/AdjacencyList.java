package slpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class AdjacencyList {

	// HashMap containing the adjacency list 
	private static HashMap<Long, List<Long>> adjList = new HashMap<>();
	
	public static void addEdge(long a, long b) {
		if(!adjList.containsKey(a)) {
			adjList.put(a, new ArrayList<Long>());
		}
		adjList.get(a).add(b);
		
		if(!adjList.containsKey(b)) {
			adjList.put(b, new ArrayList<Long>());
		}
		adjList.get(b).add(a);
	}
	
	// returns the list of nodes that a given node is connected to 
	public static List<Long> getNeighbors(long vertexA) throws Exception {
		if(!adjList.containsKey(vertexA)) {
			throw new Exception("Vertex " + vertexA + " not found!");
		}
		return adjList.get(vertexA);
	}
	
	// print the adjacency list
	public static void printAdjList() {
		for(Entry<Long, List<Long>> entry: adjList.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
}
