/**
 * Description: 访问过的url列表，用于排重（基于HashSet实现功能）
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 * */

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Queue;

public class VisitedList{
	private static Set visitedURL = new HashSet();
	
	public boolean isVisited(String url){
		if(visitedURL.contains(url))
			return true;
		else
			return false;
	}
	
	public void addVisitedURL(String url){
		visitedURL.add(url);
	}
	
	public void pushToFile(){}
}