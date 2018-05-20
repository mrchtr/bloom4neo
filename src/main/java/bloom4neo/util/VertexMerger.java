package bloom4neo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WIP VertexMerging algorithm <br>
 * Constructor gets post order, sets merge group size groupSize and no. of groups d <br>
 * merge() computes vertex merging and returns a Map<mergeID, List<nodeIDs>> mapping merged nodeIDs to their mergeID
 */
public class VertexMerger {
	private int d = 0;
	private int groupSize = 0;
	private List<Long> postOrder;
	private long[] arrayPostOrder;
	private long[][] result;
	
	/**
	 * VertexMerger(arrayPostOrder)
	 * @param arrayPO post order array
	 */
	public VertexMerger(long[] arrayPO) {
		
		this.arrayPostOrder = arrayPO;
		if(arrayPO.length <= 50) {
			this.groupSize = 3;
			this.d = (arrayPO.length + this.groupSize -1) / this.groupSize;
		}
		else if(arrayPO.length < 3200) {
			this.groupSize = 10;
			this.d = (arrayPO.length + this.groupSize -1) / this.groupSize;
		}
		else {
			this.d = 1600;
			this.groupSize = (arrayPO.length + this.d -1) / this.d;
		}
		this.result = new long[d][groupSize];
		
	}
	
	/**
	 * VertexMerger(postOrder): d and groupSize calculated by constructor
	 * @param pO post order
	 */
	public VertexMerger(List<Long> pO) {
		
		this.postOrder = pO;
		if(pO.size() <= 50) {
			this.groupSize = 3;
			this.d = (pO.size() + this.groupSize -1) / this.groupSize;
		}
		else if(pO.size() < 3200) {
			this.groupSize = 10;
			this.d = (pO.size() + this.groupSize -1) / this.groupSize;
		}
		else {
			this.d = 1600;
			this.groupSize = (pO.size() + this.d -1) / this.d;
		}
		this.result = new long[d][groupSize];
		
	}
	
	/**
	 * VertexMerger(d, postOrder): groupSize calculated by constructor
	 * @param d number of mergeIDs
	 * @param pO post order
	 */
	public VertexMerger(int d, List<Long> pO) {
		
		this.postOrder = pO;
		// if d too big (groupSize would be < 2), set groupSize to 3
		if( d * 2 >= pO.size() ) {
			this.groupSize = 3;
			this.d = (pO.size() + this.groupSize -1) / this.groupSize;
		}
		else {
			this.d = d;
			this.groupSize = (pO.size() + this.d -1) / this.d;
		}
		this.result = new long[d][groupSize];
		
	}
	
	/**
	 * VertexMerger(postOrder, groupSize): d calculated by constructor
	 * @param pO post order
	 * @param groupSize size of mergeID groups
	 */
	public VertexMerger(List<Long> pO, int groupSize) {
		
		this.postOrder = pO;
		// if groupSize too big (d would be < 2), set groupSize to 3
		if( groupSize * 2 >= pO.size() ) {
			this.groupSize = 3;
			this.d = (pO.size() + this.groupSize -1) / this.groupSize;
		}
		else {
			this.groupSize = groupSize;
			this.d = (pO.size() + this.groupSize -1) / this.groupSize;
		}
		this.result = new long[d][groupSize];
		
	}
	
	/**
	 * Alternative implementation of merge with 2D array as return type <br>
	 * Made in order to use primitive datatypes, possible because mergeIDs are irrelevant in further steps
	 * @return
	 */
	public long[][] mergeToArray(){
		for(int i = 0; i < this.arrayPostOrder.length; i++) {
			this.result[i / this.groupSize][i % this.groupSize] = this.arrayPostOrder[i];
		}
		long[] lastElement = new long[this.arrayPostOrder.length % this.groupSize];
		System.arraycopy(this.result[this.result.length - 1], 0, lastElement, 0, lastElement.length);
		this.result[this.result.length - 1] = lastElement;
		return this.result;
	}
	
	
	/**
	 * Vertex merging based on parameters defined by VertexMerger constructor
	 * @return Map<mergeID, List<nodeIDs>> mapping merged nodeIDs to their mergeID
	 */
	public Map<Long, List<Long>> merge(){
		List<List<Long>> resultList = new ArrayList<List<Long>>();
		for(int i = 0; i < d; i++) {
			resultList.add(new ArrayList<Long>());
		}
		int mergeCount = -1;
		for(int i = 1; i <= this.postOrder.size(); i++) {
			if(i % this.groupSize == 1) {
				mergeCount++;			
			}
			resultList.get(mergeCount).add(this.postOrder.get(i-1));
		}
		Map<Long, List<Long>> mergeToNode = new HashMap<Long, List<Long>>();
		for(List<Long> nodeList : resultList) {
			mergeToNode.put(nodeList.get(0), nodeList);
			
		}
		return mergeToNode;
	}

}
