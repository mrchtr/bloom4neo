package bloom4neo.util;

//import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

public class IndexGeneratorV2 extends IndexGenerator {
	
	/**
	 * Indexing algorithm based on "Reachability Querying: Can It Be Even Faster?" paper <br>
	 * 
	 * Step 1: DFS through DB to compute Ldis and Lfin of vertices and create post-order <br>
	 * Step 2: Vertices merging based on post-order, MergeIDs of vertices calculated <br>
	 * Step 3: Use hash function to compute Bloom filter index for each MergeID <br>
	 * Step 4: Computation of Bloom filters Lin and Lout for vertices <br>
	 * 
	 * Indexing info:  <br>
	 * 	long Ldis, Lfin;  int BFID; byte[] Lout, Lin stored as properties on SCC representatives and non-SCC nodes. <br>
	 * 	long[] cycleMembers stored as property on SCC representatives <br>
	 * 	long cycleRepID, inDegree, outDegree stored as property on SCC members
	 */
	public static void generateIndex(GraphDatabaseService dbs) {
		
		// Step 1: DFS through DB to compute Ldis and Lfin of vertices and create post-order
		DepthFirstSearch dfs = new DepthFirstSearch(dbs);
		List<Long> postOrder = dfs.executeIterativeDFS();
//		long[] arrayPostOrder = dfs.executeIterativeDFS();
		// Step 2: Vertex merging
//		List<Long> postOrder = new ArrayList<Long>();
//		for(long l : arrayPostOrder) {
//			postOrder.add(l);
//		}
		VertexMerger vm = new VertexMerger(postOrder);
		List<List<Long>> mergeMap = vm.merge();
//		VertexMerger vm = new VertexMerger(arrayPostOrder);
//		long[][] mergeArray = vm.mergeToArray();
		// Step 3: Bloom filter hashing
		BloomFilter.doBFHashToGraph(mergeMap, dbs);
//		long[][] bfArray = BloomFilter.doArrayBFHash(mergeArray);
		// Storing Bloom filter index to each node in DB
//		int s = bfLists.size();
//		int s = bfLists.length;
//		for(int i = 0; i < s; i++) {
//			List<Long> nodeList = bfLists.get(i);
//			for(int j = 0; j < nodeList.size(); j++) {
//				dbs.getNodeById(nodeList.get(j)).setProperty("BFID", i);
//			}
//			for(long nodeID : bfArray[i]) {
//				if(nodeID == -1) {
//					break;
//				}
//				dbs.getNodeById(nodeID).setProperty("BFID", i);	
//			}
//		}
		// Step 4: Computing Lin and Lout for each node
		BloomFilter.computeBFs(dbs);
		
	}
	
	
}