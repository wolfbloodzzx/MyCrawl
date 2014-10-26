package Spider.table;
/**
 * Title:爬虫队列
 * Description:构建爬虫的url队列。本版本使用链表实现队列（尚无权值排序）
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 */

import java.util.LinkedList;
import Spider.define.*;

public class RunTable {
	private LinkedList<UrlNode> queue = new LinkedList<UrlNode>();

	public int size(){
		//System.out.println(queue.size());
		return queue.size();
	}
	// 入队列
	public void enQueue(UrlNode t) {
		queue.addLast(t);
	}

	// 出队列
	public UrlNode deQueue() {
		return queue.removeFirst();
	}

	// 判断队列是否为空
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	// 判断队列是否包含t
	public boolean contians(UrlNode t) {
		return queue.contains(t);
	}

	public boolean empty() {
		return queue.isEmpty();
	}

}
