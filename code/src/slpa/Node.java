package slpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Node {
	
	// the index associated with a node
	private long index;
	// the communities (their indexes) associated with each node
	private HashMap<Long, Long> communityIndices;
	// size of the above list
	private long communityIndicesSize;

	// constructor
	public Node() {
		this.index = -999;
		this.communityIndices = new HashMap<>();
		this.communityIndicesSize = 0;
	}

	// copy constructor
	public Node(long index) {
		this.index = index;
		this.communityIndicesSize = 0;
		this.communityIndices = new HashMap<>();
		this.addCommIndex(index);
	}

	// for a given random probability, return the index which 
	// has a probability occurrence greater than the given value
	public long getProbabilisticCommunityIndex(double probability) {
		double ratio = 0;
		for (Entry<Long, Long> entry : communityIndices.entrySet()) {
			ratio += ((double)entry.getValue() / this.communityIndicesSize);
			if (ratio > probability) {
				return entry.getKey();
			}
		}
		return -1;
	}

	// adds a label to the list of community indices
	public void addCommIndex(long commIndex) {
		if (!this.communityIndices.containsKey(commIndex)) {
			communityIndices.put(commIndex, 0L);
		}
		communityIndices.put(commIndex, communityIndices.get(commIndex) + 1);
		this.communityIndicesSize++;
	}
	
	// for a given threshold, returns the labels that has probability occurrence
	// greater than the given value
	public List<Long> getFinalCommunities(double threshold) {
		List<Long> communities = new ArrayList<>();
		for (Entry<Long, Long> entry : communityIndices.entrySet()) {
			double ratio = ((double)entry.getValue() / this.communityIndicesSize);
			if (ratio > threshold) {
				communities.add(entry.getKey());
			}
		}
		return communities;		
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public HashMap<Long, Long> getcommunityIndices() {
		return communityIndices;
	}

	public void setcommunityIndices(HashMap<Long, Long> communityIndices) {
		this.communityIndices = communityIndices;
	}

	public long getCommunityIndicesSize() {
		return communityIndicesSize;
	}

	public void setCommunityIndicesSize(long communityIndicesSize) {
		this.communityIndicesSize = communityIndicesSize;
	}

}
