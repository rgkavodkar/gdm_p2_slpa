package slpa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class SLPA {

	private static HashMap<Long, Node> nodes = new HashMap<>();
	private static HashMap<Long, List<Long>> communityMap = new HashMap<>();
	private static Set<Long> vertices = new HashSet<>();

	public static void main(String[] args) {
		String inputGraphFilename = args[0];
		int iterations = Integer.parseInt(args[1]);
		double threshold = Double.parseDouble(args[2]);
		String outputFilename = args[3];
		
		initialize(inputGraphFilename);
		try {
			slpa(iterations, threshold);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// write the detected communities to a file
			File file = new File(outputFilename);
			FileWriter writer = new FileWriter(file);
			for(Entry<Long, List<Long>> entry: communityMap.entrySet()) {
				StringBuilder sb = new StringBuilder();
				for(long index: entry.getValue()) {
					sb.append(index).append(" ");
				}
 				writer.append(sb.toString()).append(System.lineSeparator());
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Output written to file: " + outputFilename);
	}

	private static void slpa(int iterations, double threshold) throws Exception {

		// preprocess
		while (iterations >= 0) {
			for (Entry<Long, Node> entry : nodes.entrySet()) {
				long node = entry.getKey();

				List<Long> labels = new ArrayList<>();
				// The listening: neighbourIndex is the listener here
				for (Long neighborIndex : AdjacencyList.getNeighbors(node)) {
					double random = new Random().nextDouble();
					Node neighbor = nodes.get(neighborIndex);
					// Based on the random number above, get the community
					// with the highest probability occurrence
					// Multiplying by 0.75 to make the probability achievable
					long probCommIndex = neighbor.getProbabilisticCommunityIndex(0.75 * random);
					if (probCommIndex != -1) {
						labels.add(probCommIndex);
					}
				}
				long maxValue = 0;
				long maxLabel = -999;
				
				// From all the labels added in the above process, select the one which is 
				// the most popular and add it to the list of labels of the current node
				for (long label : labels) {
					long frequency = Collections.frequency(labels, label);
					if (maxValue < frequency) {
						maxValue = frequency;
						maxLabel = label;
					} 
				}
				entry.getValue().addCommIndex(maxLabel);
			}
			iterations--;
		}

		// postprocess
		// For every label that is available amongst the nodes, add the nodes that
		// belong to that community; if it has a probability dist greater than the threshold
		for (Entry<Long, Node> entry : nodes.entrySet()) {
			Node node = entry.getValue();
			for (Long community : node.getFinalCommunities(threshold)) {
				if (!communityMap.containsKey(community)) {
					communityMap.put(community, new ArrayList<Long>());
				}
				communityMap.get(community).add(node.getIndex());
			}
		}

	}

	private static void initialize(String filename) {
		
		// File containing the input graph data
		File file = new File(filename);
		BufferedReader br = null;
		try {
			String edge;
			br = new BufferedReader(new FileReader(file));
			// ignoring the first line containing the number of edges and vertices
			edge = br.readLine();
			while ((edge = br.readLine()) != null) {
				// create an adjacency list
				long vertexA = Long.parseLong(edge.split(" ")[0]);
				long vertexB = Long.parseLong(edge.split(" ")[1]);
				AdjacencyList.addEdge(vertexA, vertexB);
				if (!vertices.contains(vertexA)) {
					vertices.add(vertexA);
					nodes.put(vertexA, new Node(vertexA));
				}
				if (!vertices.contains(vertexB)) {
					vertices.add(vertexB);
					nodes.put(vertexB, new Node(vertexB));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
