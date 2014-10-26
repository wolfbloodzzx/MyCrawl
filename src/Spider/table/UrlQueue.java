package Spider.table;
/**
 * url队列
 */
import Spider.define.*;

public class UrlQueue {
	
	public static RunTable runTable = new RunTable();
	public static VisitedTable visTable = new VisitedTable();
	
	public synchronized static void pushNode(UrlNode node){
		if(!visTable.isVisited(node.url))
		{
			//System.out.println("push success");
			runTable.enQueue(node);
		}
	}
	
	public synchronized static UrlNode popNode(){
		UrlNode node = runTable.deQueue();
		visTable.addVisitedURL(node.url);
		return node;
	}
	
	public synchronized static boolean isEmpty(){
		return runTable.empty();
	}
	
	public synchronized static int size(){
		return runTable.size();
	}
}
