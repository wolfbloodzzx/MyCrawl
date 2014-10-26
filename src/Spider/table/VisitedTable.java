package Spider.table;
/**
 * Description: 访问过的url列表，用于排重（基于HashSet实现功能）
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 * */

import java.util.HashSet;
import java.util.Set;

public class VisitedTable{
	private static Set<String> visitedURL = new HashSet<String>();
	
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